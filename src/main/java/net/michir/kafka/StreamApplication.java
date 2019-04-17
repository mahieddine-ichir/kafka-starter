package net.michir.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.michir.kafka.config.*;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class StreamApplication {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-app");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        GlobalKTable<String, String> global = global(streamsBuilder);
        stream(streamsBuilder, global);

        Topology topology = streamsBuilder.build();
        System.out.println(topology.describe());
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

        kafkaStreams.start();
    }

    static void stream(StreamsBuilder streamsBuilder, GlobalKTable<String, String> global) {

        //GlobalKTable<String, String> globalTable = streamsBuilder.globalTable("envelope_labels-store", Consumed.with(Serdes.String(), Serdes.String()));

        KStream<Integer, Envelope> stream = streamsBuilder.stream(ProducerApplication.TOPIC,
                Consumed.with(Serdes.Integer(), new EnvelopeSerdes())
                );

        stream
                .leftJoin(global, (leftKey, leftValue) -> leftValue.getStatus().name(), (leftValue, rightValue) -> {
                    leftValue.setLabel(rightValue);
                    return leftValue;
                })
                .peek((key, value) -> System.out.println(value))
                .to("output", Produced.with(Serdes.Integer(), new EnvelopeSerdes()));

        streamsBuilder.stream("output", Consumed.with(Serdes.Integer(), new EnvelopeSerdes()))
                .selectKey((key, value) -> value.getStatus().name())
                .groupByKey(Grouped.with(Serdes.String(), new EnvelopeSerdes()))
                .aggregate(EnvelopeSummary::new, (key, value, aggregate) -> aggregate.increment(value),
                        Materialized.with(Serdes.String(), new EnvelopeSummarySerdes()))
                .toStream()
                .mapValues((readOnlyKey, value) -> schema(value))
                .peek((key, value) -> System.out.println(value))
                .to("counts", Produced.with(Serdes.String(), Serdes.String()));

    }

    private static String schema(EnvelopeSummary envelopeSummary) {
        Schema schema = new Schema();
        schema.setName("summary");
        {
            Field e1 = new Field();
            e1.setType("string");
            e1.setField("name");
            schema.getFields().add(e1);
        }
        {
            Field e1 = new Field();
            e1.setType("int32");
            e1.setField("count");
            schema.getFields().add(e1);
        }

        try {
            Map<String, Object> all = new HashMap<>();
            all.put("schema", schema);
            all.put("payload", envelopeSummary);
            return new ObjectMapper().writeValueAsString(all);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    static GlobalKTable<String, String> global(StreamsBuilder streamsBuilder) {

        streamsBuilder.stream("test-sqlite-jdbc-envelope_labels",
                Consumed.with(Serdes.String(), new EnvelopeStateAndLabelSerdes()))
                .filterNot((key, value) -> value == null)
                .map((key, value) -> KeyValue.pair(value.getName(), value.getLabel()))
                .peek((key, value) -> System.out.println(value))
                .to("envelope_labels", Produced.with(Serdes.String(), Serdes.String()));

        return streamsBuilder.globalTable("envelope_labels",
                Consumed.with(Serdes.String(), Serdes.String()),
                Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as("envelope_labels-store")
                        .withKeySerde(Serdes.String()).withValueSerde(Serdes.String())
        );
    }
}

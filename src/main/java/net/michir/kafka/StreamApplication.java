package net.michir.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Properties;

public class StreamApplication {

    private static final String REFERENTIAL_STORE = "referentiel-store";

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-app");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        GlobalKTable<String, String> referentiel = streamsBuilder.globalTable(ReferentielProducerApplication.REFERENTIEL,
                Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(REFERENTIAL_STORE)
                    .withKeySerde(Serdes.String())
                    .withValueSerde(Serdes.String())
        );

        KStream<Integer, Envelope> stream = streamsBuilder.stream(ProducerApplication.TOPIC, Consumed.with(Serdes.Integer(), new JsonSerdes()));
        stream
                .leftJoin(referentiel, (key, value) -> {
                    return value.getStatus().toString();
                }, (left, right) -> {
                    left.setLibelle(right);
                    return left;
                })
                .to("output", Produced.with(Serdes.Integer(), new JsonSerdes()));


        Topology topology = streamsBuilder.build();
        System.out.println(topology.describe());

        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
        kafkaStreams.start();

        //ReadOnlyKeyValueStore<Object, Object> store = kafkaStreams.store(ReferentialApplication.REFERENTIAL_STORE, QueryableStoreTypes.keyValueStore());
        //System.out.println(store.get(Envelope.State.CREE.name()));

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}

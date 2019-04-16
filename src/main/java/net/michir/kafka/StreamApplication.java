package net.michir.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.io.IOException;
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
        KStream<Integer, String> stream = streamsBuilder.stream(ProducerApplication.TOPIC);

        stream
                .mapValues((readOnlyKey, value) -> toEnvelope(value))
                .filterNot((key, value) -> value == null)
                .filter((key, value) -> value.getStatus() == Envelope.State.NPAI)
                .mapValues((key, value) -> toString(value))
                .to("output");

        Topology topology = streamsBuilder.build();
        System.out.println(topology.describe());

        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));

        kafkaStreams.start();
    }

    private static Envelope toEnvelope(String value) {
        try {
            return OBJECT_MAPPER.readValue(value, Envelope.class);
        } catch (IOException ignored) {
            //e.printStackTrace();
            return null;
        }
    }

    private static String toString(Envelope envelope) {
        try {
            return OBJECT_MAPPER.writeValueAsString(envelope);
        } catch (IOException ignored) {
            //e.printStackTrace();
            return null;
        }
    }
}

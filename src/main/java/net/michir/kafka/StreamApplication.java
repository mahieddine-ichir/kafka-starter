package net.michir.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Configuration
public class StreamApplication {

    private static final String REFERENTIAL_STORE = "referentiel-store";

    private KafkaStreams kafkaStreams;

    @Bean
    public KafkaStreams kafkaStreams() {
        return kafkaStreams;
    }

    @PostConstruct
    public void main() {

        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-app");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<Integer, Envelope> stream = streamsBuilder.stream(ProducerApplication.TOPIC, Consumed.with(Serdes.Integer(), new JsonSerdes()));
        stream.selectKey((key, value) -> value.getStatus())
                .groupByKey()
                .aggregate(EnvelopeSummary::new, (key, value, aggregate) -> {
                    aggregate.setCount(aggregate.getCount()+1);
                    return aggregate;
                }, Materialized.as("summary"))
                .toStream()
                .to("output");

        Topology topology = streamsBuilder.build();
        System.out.println(topology.describe());

        kafkaStreams = new KafkaStreams(topology, properties);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}

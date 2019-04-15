package net.michir.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProducerApplication {

    static List<Envelope> envelopes = new ArrayList<>();
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static final String TOPIC = "test-topic";
    public static final String TOPIC_COMPRESSED = "test-topic-compressed";

    public static void main(String[] args) throws InterruptedException, JsonProcessingException {

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(properties);

        Properties propertiesForCompressed = new Properties();
        propertiesForCompressed.putAll(properties);
        propertiesForCompressed.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        KafkaProducer<Integer, String> producerForCompressed = new KafkaProducer<>(propertiesForCompressed);

        int numberOfMessages = 0;
        while (true) {

            int i = (int) Math.floor(Math.random() * (envelopes.size() - 1));
            Envelope envelope = envelopes.get(i);
            envelope.setStatus(Envelope.State.values()[i%Envelope.State.values().length]);
            String value = objectMapper.writeValueAsString(envelope);

            System.out.println("Sending "+envelope);
            // un-compressed
            producer.send(new ProducerRecord<>(TOPIC, envelope.getId(), value));
            // compressed
            producerForCompressed.send(new ProducerRecord<>(TOPIC_COMPRESSED, envelope.getId(), value));

           // Thread.sleep(1);

            if ((++numberOfMessages) > 10000) {
                break;
            }
        }
    }

    static {
        for (int i=0; i<10; i++) {
            envelopes.add(Envelope.builder()
                    .id(i)
                    .postalAddress(PostalAddress.builder()
                            .line1("Jean Dupont_"+i)
                            .line2(i+" Rue de lavoie")
                            .zipCode(String.format("750%02d Paris", i))
                            .country("FR")
                            .build())
                    .build());
        }
    }
}

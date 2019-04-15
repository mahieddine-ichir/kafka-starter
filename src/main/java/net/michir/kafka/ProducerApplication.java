package net.michir.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProducerApplication {

    static List<Envelope> envelopes = new ArrayList<>();
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static final String TOPIC = "test-topic";

    public static void main(String[] args) throws InterruptedException, JsonProcessingException {

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerdes.JsonSerializer.class);
        //properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 32*1024*1024);
        //properties.put(ProducerConfig.LINGER_MS_CONFIG, 100);

        KafkaProducer<Integer, Envelope> producer = new KafkaProducer<>(properties);
        Runtime.getRuntime().addShutdownHook(new Thread(producer::close));

        while (true) {

            int i = (int) Math.floor(Math.random() * (envelopes.size() - 1));
            Envelope envelope = envelopes.get(i);
            envelope.setStatus(Envelope.State.values()[i%Envelope.State.values().length]);

            ProducerRecord<Integer, Envelope> record = new ProducerRecord<>(TOPIC, envelope.getId(), envelope);
            System.out.println("Sending "+envelope);

            producer.send(record);
            Thread.sleep(3000);
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

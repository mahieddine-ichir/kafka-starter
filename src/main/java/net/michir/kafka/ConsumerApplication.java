package net.michir.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerApplication {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws InterruptedException, JsonProcessingException {

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-test-app");

        String topic = "test-topic";
        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(ProducerApplication.TOPIC));

        while (true) {
            consumer.poll(Duration.ofMillis(3000))
                    .forEach(record -> {
                        Integer key = record.key();
                        String value = record.value();

                        Envelope envelope = null;
                        try {
                            envelope = objectMapper.readValue(value, Envelope.class);
                            System.out.println("Received: key="+key+", value="+envelope+", from partition "+record.partition());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}

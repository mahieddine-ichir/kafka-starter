package net.michir.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.util.Map;

public class JsonSerdes implements Serde<Envelope> {

    Serializer<Envelope> envelopeSerializer = new JsonSerializer();
    Deserializer<Envelope> envelopeDeserializer = new JsonDeserializer();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public void close() {

    }

    @Override
    public Serializer<Envelope> serializer() {
        return envelopeSerializer;
    }

    @Override
    public Deserializer<Envelope> deserializer() {
        return envelopeDeserializer;
    }

    public static class JsonSerializer implements Serializer<Envelope> {

        ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {

        }

        @Override
        public byte[] serialize(String topic, Envelope data) {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() {

        }
    }

    public static class JsonDeserializer implements Deserializer<Envelope> {

        ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
        }

        @Override
        public Envelope deserialize(String topic, byte[] data) {
            try {
                return objectMapper.readValue(data, Envelope.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() {

        }
    }
}

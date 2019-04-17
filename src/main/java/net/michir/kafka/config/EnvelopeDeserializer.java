package net.michir.kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.michir.kafka.Envelope;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class EnvelopeDeserializer implements Deserializer<Envelope> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Envelope deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Envelope.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void close() {

    }
}

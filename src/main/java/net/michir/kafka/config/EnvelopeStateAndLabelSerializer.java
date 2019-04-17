package net.michir.kafka.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.michir.kafka.EnvelopeStateAndLabel;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class EnvelopeStateAndLabelSerializer implements Serializer<EnvelopeStateAndLabel> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) { }

    @Override
    public byte[] serialize(String topic, EnvelopeStateAndLabel data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {}
}
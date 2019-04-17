package net.michir.kafka.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.michir.kafka.EnvelopeSummary;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class EnvelopeSummarySerializer implements Serializer<EnvelopeSummary> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) { }

    @Override
    public byte[] serialize(String topic, EnvelopeSummary data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public void close() {}
}
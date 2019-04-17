package net.michir.kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.michir.kafka.EnvelopeSummary;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class EnvelopeSummaryDeserializer implements Deserializer<EnvelopeSummary> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public EnvelopeSummary deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, EnvelopeSummary.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void close() {

    }
}

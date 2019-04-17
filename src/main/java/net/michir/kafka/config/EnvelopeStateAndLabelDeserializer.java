package net.michir.kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.michir.kafka.EnvelopeStateAndLabel;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class EnvelopeStateAndLabelDeserializer implements Deserializer<EnvelopeStateAndLabel> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public EnvelopeStateAndLabel deserialize(String topic, byte[] data) {
        try {
            System.out.println(new String(data));
            //Map map = objectMapper.readValue(data, Map.class);
            //return objectMapper.convertValue(map.get("payload"), EnvelopeStateAndLabel.class);
            return objectMapper.readValue(data, EnvelopeStateAndLabel.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {

    }
}

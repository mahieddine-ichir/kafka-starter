package net.michir.kafka.config;

import net.michir.kafka.EnvelopeStateAndLabel;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class EnvelopeStateAndLabelSerdes implements Serde<EnvelopeStateAndLabel> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public void close() {

    }

    @Override
    public Serializer<EnvelopeStateAndLabel> serializer() {
        return new EnvelopeStateAndLabelSerializer();
    }

    @Override
    public Deserializer<EnvelopeStateAndLabel> deserializer() {
        return new EnvelopeStateAndLabelDeserializer();
    }
}

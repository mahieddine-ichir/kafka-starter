package net.michir.kafka.config;

import net.michir.kafka.Envelope;
import net.michir.kafka.config.EnvelopeDeserializer;
import net.michir.kafka.config.EnvelopeSerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class EnvelopeSerdes implements Serde<Envelope> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public void close() {

    }

    @Override
    public Serializer<Envelope> serializer() {
        return new EnvelopeSerializer();
    }

    @Override
    public Deserializer<Envelope> deserializer() {
        return new EnvelopeDeserializer();
    }
}

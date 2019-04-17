package net.michir.kafka.config;

import net.michir.kafka.EnvelopeSummary;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class EnvelopeSummarySerdes implements Serde<EnvelopeSummary> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public void close() {

    }

    @Override
    public Serializer<EnvelopeSummary> serializer() {
        return new EnvelopeSummarySerializer();
    }

    @Override
    public Deserializer<EnvelopeSummary> deserializer() {
        return new EnvelopeSummaryDeserializer();
    }
}

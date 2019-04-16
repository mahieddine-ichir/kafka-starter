package net.michir.kafka;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnvelopeSummaryService {

    @Autowired
    KafkaStreams kafkaStreams;

    public EnvelopeSummary get(Envelope.State state) {
        return kafkaStreams.store("summary", QueryableStoreTypes.<String, EnvelopeSummary>keyValueStore()).get(state.name());
    }

}

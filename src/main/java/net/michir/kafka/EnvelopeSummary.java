package net.michir.kafka;

import lombok.Data;

@Data
public class EnvelopeSummary {

    private String name;

    private Integer count = 0;

    public EnvelopeSummary increment(Envelope envelope) {
        this.name = envelope.getStatus().name();

        this.count++;
        return this;
    }
}

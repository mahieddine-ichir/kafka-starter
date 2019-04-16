package net.michir.kafka;

import lombok.Data;

@Data
public class EnvelopeSummary {

    private Envelope.State state;

    private Integer count = 0;
}

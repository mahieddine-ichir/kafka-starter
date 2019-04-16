package net.michir.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/envelopes")
public class Api {

    @Autowired
    EnvelopeSummaryService service;

    @GetMapping
    public EnvelopeSummary envelopeSummary(Envelope.State state) {
        return service.get(state);
    }

}

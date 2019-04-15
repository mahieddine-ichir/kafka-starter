package net.michir.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Envelope {

    private Integer id;

    private Date creationDate = new Date();

    private State status = State.CREE;

    private PostalAddress postalAddress;


    public enum State {
        CREE,
        ENVOYEE,
        RECUE,
        NPAI
    }
}

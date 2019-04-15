package net.michir.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostalAddress {

    private String line1;
    private String line2;
    private String line3;

    private String zipCode;
    private String country;
}

package net.michir.kafka.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Schema {

    private String type = "struct";

    private String name;

    private List<Field> fields = new ArrayList<>();
}

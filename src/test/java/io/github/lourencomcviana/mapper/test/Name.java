package io.github.lourencomcviana.mapper.test;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Name {
    String firstName;
    String lastName;
}

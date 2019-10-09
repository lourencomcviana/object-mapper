package io.github.lourencomcviana.mapper.test;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Address {
    String street;
    String city;
}

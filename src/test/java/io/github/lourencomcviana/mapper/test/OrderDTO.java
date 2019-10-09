package io.github.lourencomcviana.mapper.test;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    String customerFirstName;
    String customerLastName;
    String billingStreet;
    String billingCity;
}

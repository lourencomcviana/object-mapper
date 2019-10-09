package io.github.lourencomcviana.mapper.test;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Order extends BaseOrder {
    Customer customer;
    Address billingAddress;

    Boolean payd;

    Boolean sauce;

    public boolean hasSauce(){
        return this.sauce;
    }
}

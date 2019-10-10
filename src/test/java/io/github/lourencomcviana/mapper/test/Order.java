package io.github.lourencomcviana.mapper.test;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class Order extends BaseOrder {
    Customer customer;
    Address billingAddress;
    Address[] billingAddressArray;
    Iterable<Address> billingAddressIterable;
    Boolean payd;
    Boolean sauce;

    public boolean hasSauce(){
        return this.sauce;
    }
}

package io.github.lourencomcviana;

import io.github.lourencomcviana.mapper.test.Customer;
import io.github.lourencomcviana.mapper.test.Name;
import io.github.lourencomcviana.mapper.test.Order;
import io.github.lourencomcviana.mapper.test.OrderDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyReaderTest {

    private static Order order;

    @BeforeAll
    public static void build(){
        order = Order.builder()
                    .payd(false)
                    .sauce(true)
                    .customer(
                        Customer.builder()
                            .name(
                                Name.builder()
                                    .firstName("maria")
                                    .lastName("antoni")
                                .build()
                            )
                        .build())
                .build();
    }

    @DisplayName("test if method finding is working properly")
    @org.junit.jupiter.api.Test
    void getMethodByNameIsWorking() {
        assertTrue(PropertyReader.getPropertyMethod("hasSauce",Order.class).isPresent()
                ,"get by splicit name");
        assertTrue(PropertyReader.getPropertyMethod("payd",Order.class).isPresent()
                ,"get by field name");
        assertTrue(PropertyReader.getPropertyMethod("value",Order.class).isPresent()
                ,"get superclass method");
        assertFalse(PropertyReader.getPropertyMethod("onlySetter",Order.class).isPresent()
                ,"this method should not be found");
   }


}

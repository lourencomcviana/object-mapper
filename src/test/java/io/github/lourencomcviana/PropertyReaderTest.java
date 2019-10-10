package io.github.lourencomcviana;

import io.github.lourencomcviana.mapper.test.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyReaderTest {

    private  Order order;

    @BeforeEach
    public  void build(){
        order = Order.builder()
                    .payd(false)
                    .sauce(true)
                    .customer(
                        Customer.builder()
                            .name(
                                Name.builder()
                                    .firstName("maria")
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



    @DisplayName("test if method finding is working properly")
    @org.junit.jupiter.api.Test
    void getProperty() {

        assertEquals("maria",PropertyReader.get(order,"customer.name.firstName")
                ,"get firstName from customer insider order");

        assertEquals("maria",PropertyReader.get(order,new String[]{"customer","name","firstName"})
                ,"get firstName from customer insider order");
    }

    @DisplayName("test if method finding is working properly")
    @org.junit.jupiter.api.Test
    void setProperty() {
        PropertyReader.set(order,"customer.name.firstName","maria2");

        assertEquals("maria2",order.getCustomer().getName().getFirstName()
                ,"get firstName from customer insider order");


        PropertyReader.set(order,"customer.name.lastName","ultimo nome");
        assertEquals("ultimo nome",order.getCustomer().getName().getLastName()
                ,"get firstName from customer insider order");

    }

    @DisplayName("test if method finding is working properly")
    @org.junit.jupiter.api.Test
    void setPropertyOnNonInstantiatedObject() {
        PropertyReader.set(order,"billingAddress.street","rua1");
        assertEquals("rua1",order.getBillingAddress().getStreet()
                ,"set adress street in order");

    }

    @DisplayName("test if method finding is working properly")
    @org.junit.jupiter.api.Test
    void setPropertyOnNonInstantiatedListObject() {

        PropertyReader.set(order,"billingAddressArray.street","rua1");
        assertEquals("rua1",order.getBillingAddressArray()[0].getStreet()
                ,"set adress street in order");


        //TODO: not implemented
        assertEquals(Iterable.class, PropertyReader.get(order,"billingAddressArray.street").getClass()
                ,"return must be an iterable of all the itens on the array");
    }

}




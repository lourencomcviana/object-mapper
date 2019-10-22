package io.github.lourencomcviana;


import io.github.lourencomcviana.mapper.test.Customer;
import io.github.lourencomcviana.mapper.test.Name;
import io.github.lourencomcviana.mapper.test.Order;
import io.github.lourencomcviana.mapper.test.OrderDTO;
import org.junit.jupiter.api.DisplayName;
import org.modelmapper.ModelMapper;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperTest {


    @DisplayName("string to LocalDate parse")
    @org.junit.jupiter.api.Test
    void simpleMap() {
        Order order = Order.builder().customer(
            Customer.builder()
                .name(
                    Name.builder()
                        .firstName("maria")
                        .lastName("antoni")
                        .build() )
                .build())
        .build();



        ModelMapper modelMapper = new ModelMapper();
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);


        assertEquals(PropertyReader.getPropertyValue(order,"customer.name.firstName"), orderDTO.getCustomerFirstName());
        assertEquals(order.getCustomer().getName().getLastName(), orderDTO.getCustomerLastName());
        assertEquals(PropertyReader.getPropertyValue(order,"billingAdress.street"), orderDTO.getBillingStreet());
        assertEquals(PropertyReader.getPropertyValue(order,"getBillingAdress.street"), orderDTO.getBillingStreet());
//        assertEquals(order.getBillingAddress().getCity(), orderDTO.getBillingCity());
    }



    @DisplayName("same class mapping")
    @org.junit.jupiter.api.Test
    void sameClassMapping() {
        Order order = Order.builder().customer(
                Customer.builder()
                        .name(
                                Name.builder()
                                        .firstName("maria")
                                        .lastName("antoni")
                                        .build() )
                        .build())
                .build();


        Order newOrder = Order.builder().payd(false).sauce(true).build();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(order,newOrder);



        assertEquals(false, newOrder.getPayd());
        assertEquals(true, newOrder.getSauce());
        assertEquals("maria", newOrder.getCustomer().getName().getFirstName());
    }

}

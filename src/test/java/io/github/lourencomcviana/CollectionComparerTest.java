package io.github.lourencomcviana;

import io.github.lourencomcviana.mapper.test.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Collection Comparer")
class CollectionComparerTest {

    private static List<Order> oldList;

    @BeforeAll
    public static void createData(){
        oldList = Arrays.asList(
                Order.builder().id(1L).build(),
                Order.builder().id(2L).build(),
                Order.builder().id(3L).build(),
                Order.builder().id(4L).build(),
                Order.builder().id(5L).build(),
                Order.builder().id(6L).build()
            );
    }


    @DisplayName("only edit")
    @org.junit.jupiter.api.Test
    void edit() {
        List<Order> newList= Arrays.asList(
                Order.builder().id(1L).build(),
                Order.builder().id(2L).build(),
                Order.builder().id(3L).build(),
                Order.builder().id(4L).build(),
                Order.builder().id(5L).build(),
                Order.builder().id(6L).build()
        );

        CollectionComparer<Order> comparission =  CollectionComparer.compare(oldList,newList,"id");

        assertEquals(0,comparission.getInserted().size(),"must not have inserted itens");
        assertEquals(6,comparission.getEdited().size(),"must have edited itens");
        assertEquals(0,comparission.getRemoved().size(),"must not have removed itens");
    }

    @DisplayName("only edit")
    @org.junit.jupiter.api.Test
    void insert() {
        List<Order> newList= Arrays.asList(
                Order.builder().id(1L).build(),
                Order.builder().id(2L).build(),
                Order.builder().id(3L).build(),
                Order.builder().id(4L).build(),
                Order.builder().id(5L).build(),
                Order.builder().id(6L).build(),
                Order.builder().build()
        );

        CollectionComparer<Order> comparission =  CollectionComparer.compare(oldList,newList,"id");

        assertEquals(1,comparission.getInserted().size(),"must have inserted itens");
        assertEquals(6,comparission.getEdited().size(),"must have edited itens");
        assertEquals(0,comparission.getRemoved().size(),"must not have removed itens");
    }

    @DisplayName("change list")
    @org.junit.jupiter.api.Test
    void changeList() {
        List<Order> newList= Arrays.asList(
                Order.builder().build(),
                Order.builder().build()
        );


        CollectionComparer<Order> comparission =  CollectionComparer.compare(oldList,newList,"id");

        assertEquals(2,comparission.getInserted().size(),"must have inserted itens");
        assertEquals(0,comparission.getEdited().size(),"must not have edited itens");
        assertEquals(6,comparission.getRemoved().size(),"must have removed itens");
    }




    @DisplayName("remove one item, edit five")
    @org.junit.jupiter.api.Test
    void remove() {
        List<Order> newList= Arrays.asList(
                Order.builder().id(1L).build(),
                Order.builder().id(2L).build(),
                Order.builder().id(3L).build(),
                Order.builder().id(4L).build(),
                Order.builder().id(5L).build()
        );

        CollectionComparer<Order> comparission =  CollectionComparer.compare(oldList,newList,"id");

        assertEquals(0,comparission.getInserted().size(),"must not have inserted itens");
        assertEquals(5,comparission.getEdited().size(),"must have edited itens");
        assertEquals(1,comparission.getRemoved().size(),"must have removed itens");
    }


    @DisplayName("add one item that has id and not exists in old list")
    @org.junit.jupiter.api.Test
    void addItemWithId() {
        List<Order> newList= Arrays.asList(
                Order.builder().id(6L).build(),
                Order.builder().id(7L).build()
        );

        CollectionComparer<Order> comparission =  CollectionComparer.compare(oldList,newList,"id");

        assertEquals(1,comparission.getInserted().size(),"must have inserted itens");
        assertEquals(1,comparission.getEdited().size(),"must not have edited itens");
        assertEquals(5,comparission.getRemoved().size(),"must have removed itens");
    }

}
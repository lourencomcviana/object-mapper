package io.github.lourencomcviana;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;


import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("A DateUtil")
class DateUtilTest {

    static final HashMap<LocalDate,String[]> testSource= new  HashMap<>();


    @BeforeAll
    public static void createData(){
        testSource.put(LocalDate.of(2018,1,2), new String[]{
                "2018-01-02",
                "2018-1-2",
                "2018-01-02 00:00:00"
        });
    }

    DateUtil dateUtil;

    @DisplayName("string to LocalDate parse")
    @org.junit.jupiter.api.Test
    void parseStringToDateTime() {


        testSource.forEach((originalDate, testSource)->{
            Arrays.stream(testSource).forEach(strDate ->{
                LocalDate saida = DateUtil.parseStringtoDate(strDate);
                assertNotNull(saida,"date '"+strDate+"' was not parsed correclty ");
                assertEquals(originalDate,saida,"date was diferent tham needed");
            });
        });

    }


}
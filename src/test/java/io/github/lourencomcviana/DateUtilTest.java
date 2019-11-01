package io.github.lourencomcviana;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;


import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("A DateUtil")
class DateUtilTest {

    static final HashMap<LocalDate,String[]> testSource= new  HashMap<>();

    static final HashMap<LocalDateTime,String[]> testSourceTime= new  HashMap<>();
    @BeforeAll
    public static void createData(){


        testSource.put(LocalDate.of(2019,7,10), new String[]{
                "10/07/2019",
                "10/07/2019 01:00:01"
        });

        testSource.put(LocalDate.of(2019,10,29), new String[]{
                "2019-10-29", "2019-10-29 00:01:02"
        });

        testSource.put(LocalDate.of(2018,1,2), new String[]{
                "2018-01-02",
                "2018-1-2",
                "2018-01-02 00:00:00"
        });

        testSourceTime.put(LocalDateTime.of(2018,1,2,22,34,52,00), new String[]{
                "2018-01-02T22:34:52",
                "2018-1-2T22:34:52",
                "2018-01-02T22:34:52"
        });

    }

    DateUtil dateUtil;

    @DisplayName("string to LocalDate parse")
    @org.junit.jupiter.api.Test
    void parseStringToDate() {


        testSource.forEach((originalDate, testSource)->{
            Arrays.stream(testSource).forEach(strDate ->{
                LocalDate saida = DateUtil.parseStringtoDate(strDate);
                assertNotNull(saida,"date '"+strDate+"' was not parsed correclty ");
                assertEquals(originalDate,saida,"date was diferent tham needed");
            });
        });

    }


    @DisplayName("string to LocalDateTime parse")
    @org.junit.jupiter.api.Test
    void parseStringToDateTime() {

        testSourceTime.forEach((originalDate, testSource)->{
            Arrays.stream(testSource).forEach(strDate ->{
                LocalDateTime saida = DateUtil.parseStringToDateTime(strDate);
                assertNotNull(saida,"date time '"+strDate+"' was not parsed correclty ");
                assertEquals(originalDate,saida,"date was diferent tham needed");
            });
        });

    }

}
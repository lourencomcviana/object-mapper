package io.github.lourencomcviana;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("A DateUtil")
class DateUtilTest {
    static final String[] sourceDates = new String[]{
            "2018-01-02",
            "2018-1-2",
            "2018-01-02 00:00:00"
    };


    DateUtil dateUtil;

    @DisplayName("string to LocalDate parse")
    @org.junit.jupiter.api.Test
    void parseStringToDateTime() {
        Arrays.stream(sourceDates).forEach(date ->{
            assertNotNull(DateUtil.parseStringtoDate(date),"date '"+date+"' was not parsed correclty ");
        });
    }


}
//package io.github.lourencomcviana;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DisplayName("A DateUtil")
//class DateUtilTest {
//    static final String[] sourceDates = new String[]{"2018-01-02","2018-01-02 00:00:00"};
//
//
//    DateUtil dateUtil;
//
////    @Test
////    @DisplayName("is instantiated with builder DateUtil")
////    void isInstantiatedWithNew() {
////        DateUtil.builder().build();
////    }
//
//
//    @DisplayName("string to LocalDate parse")
//    @org.junit.jupiter.api.Test
//    void parseStringToDateTime() {
//        Arrays.stream(sourceDates).forEach(date ->{
//            assertNotNull(DateUtil.parseStringtoDate(date),"date '"+date+"' was not parsed correclty ");
//        });
//    }
//
////    @Nested
////    @DisplayName("when new")
////    class WhenNew {
//
//
//
////        @org.junit.jupiter.api.Test
////        void parseStringtoDate() {
////        }
////
////        @org.junit.jupiter.api.Test
////        void parseDateTimeToString() {
////        }
////
////        @org.junit.jupiter.api.Test
////        void testParseDateTimeToString() {
////        }
////
////        @org.junit.jupiter.api.Test
////        void parseDateToString() {
////        }
////    }
//
//}
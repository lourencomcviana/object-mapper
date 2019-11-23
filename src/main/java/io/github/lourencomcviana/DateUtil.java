package io.github.lourencomcviana;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Builder
public class DateUtil{

    @Getter
    @Setter
    private static ZoneId defaultZoneId = ZoneId.systemDefault();


    Integer  ano;
    Integer  mes;
    Integer  dia;
    int  hora;
    int  minuto;
    int  segundo;
    int  milisegundo;

    public boolean isValid(){return ano != null && mes != null && dia != null;}
    public LocalDate toLocalDate(){
        if(isValid())
            return LocalDate.of(ano,mes,dia);
        return null;
    }

    public LocalDateTime toLocalDateTime(){
        if(isValid())
            return LocalDateTime.of(ano,mes,dia,hora,minuto,segundo);
        return null;
    }

    //(([01]\d|2[0-4]):([0-5]\d):([0-5]\d))

    private static final HashMap<Pattern,DateOrder> patterns= new HashMap<>();

    private static final LinkedList<SimpleDateFormat> formats= new LinkedList<>();

    private static final String CONST_HORA ="([01]\\d|2[0-4])";

    private static final String MINUTO_SEGUNDO="([0-5]\\d)";
    private static final String MILISEGUNDOS="(\\d*)";

    private static final String TIME="("+ CONST_HORA +":"+MINUTO_SEGUNDO+":"+MINUTO_SEGUNDO+"\\.?"+MILISEGUNDOS+")";

    private static final String ONLY_NUMBER_TIME="("+ CONST_HORA +MINUTO_SEGUNDO+MINUTO_SEGUNDO+MILISEGUNDOS+")";

    private static final String CONST_ANO ="(\\d{4})";
    private static final String CONST_MES ="(0?[1-9]|1[012])";
    private static final String CONST_DIA ="([12]\\d|0\\d|3[01]|[0-9])";


    public static final Pattern ISO_DATE_PATTERN=Pattern.compile("("+ CONST_ANO +"-"+ CONST_MES +"-"+ CONST_DIA +")([T ]"+TIME+")?.*");
    public static final Pattern DATE_BR_PATTERN=Pattern.compile("("+ CONST_DIA +"\\/"+ CONST_MES +"\\/"+ CONST_ANO +")( "+TIME+")?.*");
    public static final Pattern ONLY_NUMBER_PATTERN=Pattern.compile("("+ CONST_ANO + CONST_MES + CONST_DIA +")( ?"+ONLY_NUMBER_TIME+")?.*");


    static {
        patterns.put(ISO_DATE_PATTERN,DateOrder.ANO_MES_DIA);
        patterns.put(DATE_BR_PATTERN,DateOrder.DIA_MES_ANO);
        patterns.put(ONLY_NUMBER_PATTERN,DateOrder.ANO_MES_DIA);
    }




    private static int tryGetDefaultTime(Matcher matcher, int group){
        try{
            return Integer.parseInt(matcher.group(group));
        }catch(Exception e){
            return 0;
        }
    }

    private static void setDefaultTime(DateUtil date,Matcher matcher){
        date.setHora(tryGetDefaultTime(matcher,7));
        date.setMinuto(tryGetDefaultTime(matcher,8));
        date.setSegundo(tryGetDefaultTime(matcher,9));
    }


    private static DateUtil parse (String value,Pattern pattern,DateOrder order){
        if(value!=null){
            String str=value;
            Matcher matcher = pattern.matcher(str);

            if(matcher.find()){
                try{
                    DateUtil date = null;
                    switch (order){
                        case ANO_MES_DIA:
                            date = matcherAnoMesDia(matcher);
                            break;
                        case DIA_MES_ANO:
                            date = matcherDiaMesAno(matcher);
                            break;
                        case MES_DIA_ANO:
                            date = matcherMesDiaAno(matcher);
                            break;
                    }
                    setDefaultTime(date,matcher);
                    return date;
                }catch(Exception e){
                    return null;
                }
            }
            return null;

        }
        return null;
    }

    private static  DateUtil matcherAnoMesDia(Matcher matcher){
        return DateUtil
                .builder()
                .ano(Integer.parseInt(matcher.group(2)))
                .mes(Integer.parseInt(matcher.group(3)))
                .dia(Integer.parseInt(matcher.group(4)))
                .build();
    }

    private static  DateUtil matcherDiaMesAno(Matcher matcher){
        return DateUtil
                .builder()
                .ano(Integer.parseInt(matcher.group(4)))
                .mes(Integer.parseInt(matcher.group(3)))
                .dia(Integer.parseInt(matcher.group(2)))
                .build();
    }

    private static  DateUtil matcherMesDiaAno(Matcher matcher){
        return  DateUtil
                .builder()
                .ano(Integer.parseInt(matcher.group(4)))
                .mes(Integer.parseInt(matcher.group(2)))
                .dia(Integer.parseInt(matcher.group(3)))
                .build();
    }

    private  static DateUtil parseDate(String value){
        DateUtil date;

        for(var patternAndOrder : patterns.entrySet()){
            try {
                date = parse(value, patternAndOrder.getKey(), patternAndOrder.getValue());
            }catch (Exception e){
                date = null;
            }
            if(date!=null) return date;
        }

        return DateUtil.builder().build();
    }

    public static LocalDateTime parseStringToDateTime(String value){
        return parseDate(value).toLocalDateTime();
    }
    public static LocalDate parseStringtoDate(String value){
        return parseDate(value).toLocalDate();
    }


    public static String parseDateTimeToString(LocalDateTime value){

          return parseDateTimeToString(value,DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static String parseDateTimeToString(LocalDateTime value,DateTimeFormatter formatter){
        try{
            if(value!=null)
                return value.format(formatter);
        }catch(Exception e){
            return null;
        }
        return null;
    }

    public static String parseDateToString(LocalDate value){
        return parseDateToString(value,DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static String parseDateToString(LocalDate value,DateTimeFormatter formatter){
        try{
            if(value!=null)
              return value.format(formatter);
          }catch(Exception e){
            return null;
          }
          return null;
    }


    public static LocalDate parseDateToLocalDate(Date value){
        return parseDateToLocalDate(value,defaultZoneId);
    }

    public static LocalDate parseDateToLocalDate(Date value,ZoneId zoneId){
        return parseDateToZoneDateTime(value,zoneId).toLocalDate();
    }

    public static LocalDateTime parseDateToLocalDateTime(Date value){
        return parseDateToLocalDateTime(value,defaultZoneId);
    }

    public static LocalDateTime parseDateToLocalDateTime(Date value,ZoneId zoneId){
        return parseDateToZoneDateTime(value,zoneId).toLocalDateTime();
    }

    public static ZonedDateTime parseDateToZoneDateTime(Date value, ZoneId zoneId){
        return Instant.ofEpochMilli(value.getTime())
                .atZone(zoneId);
    }

    enum DateOrder{
        ANO_MES_DIA, DIA_MES_ANO, MES_DIA_ANO
    }
}


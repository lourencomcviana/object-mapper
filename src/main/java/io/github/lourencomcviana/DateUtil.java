package io.github.lourencomcviana;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Builder
public class DateUtil{

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

    private static final String HORA="([01]\\d|2[0-4])";

    private static final String MINUTO_SEGUNDO="([0-5]\\d)";
    private static final String MILISEGUNDOS="(\\d*)";

    private static final String TIME="("+HORA+":"+MINUTO_SEGUNDO+":"+MINUTO_SEGUNDO+"\\.?"+MILISEGUNDOS+")";

    private static final String ONLY_NUMBER_TIME="("+HORA+MINUTO_SEGUNDO+MINUTO_SEGUNDO+MILISEGUNDOS+")";

    //private static final String TIME="(([01]\\d|2[0-4]):([0-5]\\d):([0-5]\\d)\\.?(\\d*))";
    private static final String ANO="(\\d{4})";
    private static final String MES="([01]\\d)";
    private static final String DIA="(0\\d|[12]\\d|3[01])";


    public static final Pattern ISO_DATE_PATTERN=Pattern.compile("("+ANO+"-"+MES+"-"+DIA+")([T ]"+TIME+")?.*");
    public static final Pattern DATE_BR_PATTERN=Pattern.compile("("+DIA+"\\/"+MES+"\\/"+ANO+")( "+TIME+")?.*");
    public static final Pattern ONLY_NUMBER_PATTERN=Pattern.compile("("+ANO+MES+DIA+")( ?"+ONLY_NUMBER_TIME+")?.*");


    static {
        patterns.put(ISO_DATE_PATTERN,DateOrder.AnoMesDia);
        patterns.put(DATE_BR_PATTERN,DateOrder.DiaMesAno);
        patterns.put(ONLY_NUMBER_PATTERN,DateOrder.AnoMesDia);
    }




    private static void setDefaultTime(DateUtil date,Matcher matcher){
        try{
            date.setHora(Integer.parseInt(matcher.group(7)));
            date.setMinuto(Integer.parseInt(matcher.group(8)));
            date.setSegundo(Integer.parseInt(matcher.group(9)));
        }catch(Exception e){
        }
    }

    private static DateUtil parse (String value,Pattern pattern,DateOrder order){
        if(value!=null){
            String str=value;
            Matcher matcher = pattern.matcher(str);

            if(matcher.find()){
                try{
                    DateUtil date = null;
                    switch (order){
                        case AnoMesDia:
                            date = matcherAnoMesDia(matcher);
                            break;
                        case DiaMesAno:
                            date = matcherDiaMesAno(matcher);
                            break;
                        case MesDiaAno:
                            date = matcherMesDiaAno(matcher);
                            break;
                    }
                    setDefaultTime(date,matcher);
                    return date;
                }catch(Exception e){}
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
        DateUtil date = null;

        for(var patternAndOrder : patterns.entrySet()){
            try {
                date = parse(value, patternAndOrder.getKey(), patternAndOrder.getValue());
            }catch (Exception e){
            }
            if(date!=null) return date;
        }
//        date = parseDateIso(value);
//        if(date!=null) return date;
//
//        date = parseDateBr(value);
//        if(date!=null) return date;

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
        }
        return null;
    }

    public static String parseDateToString(LocalDate value){
        try{
            if(value!=null)
              return value.format(DateTimeFormatter.ISO_LOCAL_DATE);
          }catch(Exception e){
          }
          return null;
    }

    enum DateOrder{
        AnoMesDia, DiaMesAno, MesDiaAno
    }
}


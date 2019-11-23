package io.github.lourencomcviana;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringUtil {

    private StringUtil (){

    }
    public static String replaceMoneyVal(String money){
        if(money.isEmpty()){
            return null;
        }
        if(money.matches("(\\d+\\.?)+,\\d+")){
            return money.replaceAll("\\.", "").replace(",", ".");
        }else if(money.matches("(\\d+\\,?)+.\\d+")){
            return money.replaceAll("\\,", "");
        }else{
            return money.replaceAll("\\D", "");
        }
    }

    public static String normalizerStrong(String s)  {

        return Normalizer.normalize(normalizer(s), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static String normalizer(String param) {
        String t1 = "áãàâäçéèëêùûüúóôöõïîíìÁÀÂÄÃÇÉÈËÊÙÛÜÚÓÔÖÕÏÎÍÌ";
        String t2 = "aaaaaceeeeuuuuooooiiiiAAAAACEEEEUUUUOOOOIIII";
        StringBuilder s = new StringBuilder( param);
        for (int i = 0; i < t1.length(); i++) {
            s.append("replace(").append(s).append(",'").append(t1.charAt(i)).append("','").append(t2.charAt(i)).append("')");
        }
        return s.toString();
    }

    public static String objectToJson(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        }catch (JsonProcessingException e){
            try {
                HashMap<String, String> detail = new HashMap<>();
                detail.put("erro","não foi possível converter objeto para string");
                detail.put("objeto",obj.getClass().getName());
                detail.put("exception", e.getMessage());
                if(e.getCause()!=null) {
                    detail.put("cause", e.getCause().getMessage());
                }
                IntStream.range(0, e.getStackTrace().length-1).parallel().forEach(i ->
                    detail.put("stacktrace "+i,e.getStackTrace()[i].toString())
                );

                return mapToJson(detail);
            }catch (Exception ee){
                return "{\"erro\":\"não foi possível converter objeto para string\"}";
            }
        }
    }


    public static String mapToJson( Map<String,String> details){
        StringBuilder json= new StringBuilder();
        json.append("{");

        String param= details.entrySet().stream().map(entry->
             (stringToJsonParameter(entry.getKey(),entry.getValue()))
        ).collect(Collectors.joining(", "));
        json.append(param);

        json.append("}");

        return json.toString();
    }

    private static String stringToJsonParameter(String key,Object value){
        String finalValue="";
        if(value!=null) {
            if (isNumeric(value.toString()) || isBoolean(value.toString())) {
                finalValue = value.toString();
            } else {
                finalValue = "\""+value.toString().replaceAll("\"", "\\\\\"")+"\"";
            }
        }
        return  "\""+key+"\":"+finalValue;
    }

    public static boolean isBoolean(String str)
    {
        String compareStr=str.toLowerCase().trim();
        return compareStr.equals("true")|| compareStr.equals("false");
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("([-+]?)(\\d+)(\\.(\\d+))?");  //match a number with optional '-' and decimal.
    }

}

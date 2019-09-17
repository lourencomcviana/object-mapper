package io.github.lourencomcviana.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.stream.Collectors;

public class StringUtil {
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

    public static String normalizer(String s)  {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static String replacer(String param) {
        String t1 = "áãàâäçéèëêùûüúóôöïîíÁÀÂÄÃÇÉÈËÊÙÛÜÚÓÔÖÏÎÍ";
        String t2 = "aaaaaceeeeuuuuoooiiiAAAAACEEEEUUUUOOOIII";
        String s = param;
        for (int i = 0; i < t1.length(); i++) {
            s = "replace(" + s + ",'" + t1.charAt(i) + "','" + t2.charAt(i) + "')";
        }
        return s;
    }

    public static String objectToJson(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        }catch (JsonProcessingException e){
            return "{\"erro\":\"não foi possível converter objeto para string\"}";
        }
    }

    public static String mapToJson( HashMap<String,String> details){
        StringBuilder json= new StringBuilder();
        json.append("{");

        String param= details.entrySet().stream().map((entry)->{
            return (stringToJsonParameter(entry.getKey(),entry.getValue()));
        }).collect(Collectors.joining(", "));
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

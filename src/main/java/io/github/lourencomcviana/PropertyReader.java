//package com.br.object.path;
//
//import lombok.extern.slf4j.Slf4j;
//
//
//import java.beans.Statement;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import java.util.*;
//
//@Slf4j
//public class PropertyReader{
//
//    private static LinkedList<String> parsePath(String path){
//        String[] parts = path.split("\\.");
//        if(parts.length >0){
//            LinkedList<String> arr=new LinkedList<String>(Arrays.asList(parts));
//            return arr;
//        }
//        return new LinkedList<>();
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <T> T getPropertyValue(Object obj, String string) {
//        return getPropertyValue(obj,parsePath(string));
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <T> T getPropertyValue(Object obj, LinkedList<String> parts) {
//        return propertyValue(obj,parts,null);
//    }
//
//    @SuppressWarnings("unchecked")
//    public static void setPropertyValue(Object obj, String string,Object value) {
//        setPropertyValue(obj,parsePath(string),value);
//    }
//
//    public static void setPropertyValue(Object obj, LinkedList<String> parts,Object value){
//        propertyValue(obj,parts,value);
//    }
//    @SuppressWarnings("unchecked")
//    private static <T> T propertyValue(Object obj, LinkedList<String> parts,Object value) {
//        Object ret = obj;
//
//
//        while(parts.size()>0){
//            if(ret ==null){
//                return null;
//            }
//
//            String field=parts.pop();
//
//            try {
//
//                Class<?> clazz = ret.getClass();
//                if(Iterable.class.isAssignableFrom(clazz)){
//                    if(field.matches("\\d+")){
//
//                        try{
//                            Integer id=Integer.parseInt(field);
//                            ret=((Collection)ret).toArray()[id];
//                        }catch(ClassCastException  e){
//                            return null;
//                        }
//                        return getPropertyValue(ret,parts);
//                    }else{
//                        LinkedList objList= new LinkedList();
//                        for(Object item :(Iterable)ret){
//                            LinkedList<String> newParts=(LinkedList<String>)parts.clone();
//                            newParts.addFirst(field);
//                            Object objssaida =getPropertyValue(item,newParts);
//                            // caso a saida tb tenha sido uma lista
//                            if(Iterable.class.isAssignableFrom(objssaida.getClass())){
//                                for(Object subItem :(Iterable)objssaida){
//                                    objList.add(subItem);
//                                }
//                            }else{
//                                objList.add(objssaida);
//                            }
//                        }
//                        return (T)objList;
//                    }
//                }else{
//                    if(value!=null ) {
//                        if(parts.size() == 0){
//                            setValue(field,ret,value);
//                        }
//
//                        Object temp = getValue(field,ret);
//                        if(temp == null && parts.size() != 0){
//                            temp = setNewValue(field,ret);
//                        }
//                        ret = temp;
//                    }else{
//                        ret = getValue(field,ret);
//                    }
//
//                }
//            }catch( SecurityException e) {
//                return null;
//            }
//        }
//
//        return (T) ret;
//    }
//
//    public static Optional<Method> findMethod(String methodName, Class clazz){
//        Optional<Method> foundMethod = Arrays.stream(clazz
//                .getDeclaredMethods()).filter(item -> item.getName().equals(methodName) && item.getParameters().length==1)
//                .findFirst();
//        if(foundMethod.isPresent()){
//            return foundMethod;
//        } else if(clazz.getSuperclass()!= Object.class){
//            return findMethod(methodName,clazz.getSuperclass());
//        } else {
//            return Optional.empty();
//        }
//    }
//
//    private static Object setNewValue(String field, Object objectMethod){
//        String methodName = toSetPropertyName(field);
//        try {
//            Optional<Method> foundMethod = findMethod(methodName,objectMethod.getClass());
////                    Arrays.stream(objectMethod.getClass()
////                    .getDeclaredMethods()).filter(item -> item.getName().equals(methodName) && item.getParameters().length==1)
////                    .findFirst();
//
////
////            java.lang.reflect.Method method = objectMethod.getClass().getMethod(methodName, value.getClass());
//
//            if(foundMethod.isPresent()) {
//                Method method = foundMethod.get();
//                Parameter[] params = method.getParameters();
//                Object newObj = params[0].getType().newInstance();
//                method.invoke(objectMethod, newObj);
//
//                return newObj;
//            }
//        }catch(Exception e) {
//            return null;
//        }
//        return null;
//    }
//
//    private static boolean setValue(String field, Object objectMethod, Object value){
//        String methodName = toSetPropertyName(field);
//        try {
//            java.lang.reflect.Method method = objectMethod.getClass().getMethod(methodName, value.getClass());
//            method.invoke(objectMethod, value);
//        }catch(Exception e) {
//            return false;
//        }
//        return true;
//    }
//
//    private static Object getValue(String field, Object objectMethod){
//        String methodName = toGetPropertyName(field);
//        try {
//            java.lang.reflect.Method method = objectMethod.getClass().getMethod(methodName, (Class<?>[])null);
//            return method.invoke(objectMethod, (Object[]) null);
//        }catch(Exception e) {
//            return null;
//        }
//    }
//
//
//    public static String toGetPropertyName(String field){
//        return "get" +field.substring(0, 1).toUpperCase() + field.substring(1);
//    }
//
//    public static String toSetPropertyName(String field){
//        return "set" +field.substring(0, 1).toUpperCase() + field.substring(1);
//    }
//
//    public static String toFieldName(String property){
//        property = property.substring(3);
//        return property.substring(0, 1).toLowerCase() + property.substring(1);
//    }
//
//    public static Map<String,String>  fields(Object obj){
//        Map<String,String> list = new HashMap<String,String>();
//        for (Field f : obj.getClass().getDeclaredFields()) {
//            String property=f.getName();
//            try{
//                String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1, property.length());
//                java.lang.reflect.Method method = obj.getClass().getMethod(methodName, (Class<?>[])null);
//                Object returnValue = method.invoke(obj, (Object[])null);
//                if(returnValue!=null){
//                    list.put(property,returnValue.toString());
//                }
//            }
//            catch(Exception e){
//                list.put(property,e.getMessage());
//            }
//        }
//        return list;
//    }
//
//    public static String fieldsStr(Object obj){
//        StringBuilder sb= new StringBuilder();
//        String first="";
//        for (Map.Entry<String, String>  paramMapEntry  : fields(obj).entrySet()) {
//            sb.append(first);
//            sb.append("\"");
//            sb.append(paramMapEntry.getKey());
//            sb.append("\":\"");
//            sb.append(paramMapEntry.getValue().replaceAll("\"", "\\\\\""));
//            sb.append("\"");
//            first=",";
//        }
//        return "{"+sb.toString()+"}";
//    }
//
//
//
//    public static <T> void updateFirst(T first, T second,boolean force)  {
//        PropertyReader.mergeObjects(first,first,second,force);
//    }
//    public static <T> void updateFirst(T first, T second)  {
//        PropertyReader.mergeObjects(first,first,second,false);
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <T> T mergeObjectss(T first, T second) throws IllegalAccessException, InstantiationException {
//        Class<?> clazz = first.getClass();
//        Object returnValue = clazz.newInstance();
//        return mergeObjects((T) returnValue,first,second,false);
//    }
//
//    @SuppressWarnings("unchecked")
//    private static <T> T mergeObjects(T returnValue,T first, T second,boolean force) {
//        if(returnValue!=null&&first!=null&&second!=null) {
//            Class<?> clazz = first.getClass();
//            Method[] methods = clazz.getMethods();
//
//            for (Method getMethodFirst : methods) {
//                if(getMethodFirst.getName().startsWith("get")) {
//
//                    String getPropertyName = getMethodFirst.getName();
//                    String fieldName = toFieldName(getPropertyName);
//                    String setPropertyName = toSetPropertyName( fieldName);
//
//                    Object value1 ;
//                    Object value2 ;
//
//                    try {
//                        value1 = getPropertyValue(first,fieldName);
//                    } catch (SecurityException e) {
//                        log.warn("metodo set de "+fieldName+" nao esta acessivel em "+clazz.getName());
//                        continue;
//                    }
//                    try {
//                        value2 = getPropertyValue(second,fieldName);
//                    } catch (SecurityException e) {
//                        log.warn("metodo set de "+getPropertyName+" nao esta acessivel em "+second.getClass().getName());
//                        continue;
//                    }
//
//                    catch (Exception e){
//                        log.error("erro desconhecido ao acessar proriedade "+getPropertyName+" nao existe em "+clazz.getName(),e);
//                        continue;
//                    }
//
//                    Object value;
//                    if (force && value2 != null) {
//                        value = value2;
//                    } else {
//                        value = (value1 != null) ? value1 : value2;
//                    }
//
//                    //Method setMethod;
//                    try {
//                        if(returnValue!=null && setPropertyName!=null) {
//                            Statement stmt = new Statement(returnValue, setPropertyName, new Object[]{value});
//                            stmt.execute();
//                        }
//                    }catch (NullPointerException e){
//                        //log.warn("metodo "+setPropertyName+" é nullo em "+clazz.getName());
//                        continue;
//                    }catch(java.lang.IllegalArgumentException e ){
//                        //log.warn("metodo "+setPropertyName+" nao existe em "+clazz.getName());
//                        continue;
//                    } catch (SecurityException e) {
//                        //log.warn("metodo "+setPropertyName+" nao esta acessivel em "+clazz.getName());
//                        continue;
//                    }
//                    catch (NoSuchMethodException e) {
//                        //log.warn("metodo "+setPropertyName+" nao existe em "+clazz.getName());
//                        continue;
//                    }
//                    catch (Exception e){
//                        log.error("erro desconhecido ao acessar proriedade "+setPropertyName+" nao existe em "+clazz.getName(),e);
//                        continue;
//                    }
//
//                }
//            }
//        }
//        return returnValue;
//    }
//    public static <T> T copyData(T a, Object b){
//
//        updateFirst(a,b);
//        return a;
//    }
//}
package io.github.lourencomcviana;

import io.github.lourencomcviana.exceptions.MethodNotFoundException;
import io.github.lourencomcviana.exceptions.PropertyNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;


import java.beans.Statement;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class PropertyReader{

    private static final String[] GET_METHOD_ALIASES =  new String []{"get","is","has"};

    public static <T> T get(Object obj, String string) {
        return getPropertyValue(obj,parsePath(string));
    }

    public static <T> void set(Object obj, String string,T value) {
        setPropertyValue(obj,parsePath(string),value);
    }

    public static <T> T get(Object obj, Iterable<String> parts) {
        return getPropertyValue(obj,castToLinkedList(parts));
    }

    public static <T> void set(Object obj,Iterable<String> parts,T value) {
        setPropertyValue(obj,castToLinkedList(parts),value);
    }

    public static <T> T get(Object obj, String[] parts) {
        return getPropertyValue(obj, new LinkedList<>(Arrays.asList(parts)));
    }

    public static <T> void set(Object obj,String[] parts,T value) {
        setPropertyValue(obj, new LinkedList<>(Arrays.asList(parts)),value);
    }


    @SuppressWarnings("unchecked")
    public static <T> T getPropertyValue(Object obj, String string) {
        return getPropertyValue(obj,parsePath(string));
    }
    @SuppressWarnings("unchecked")
    public static <T> T getPropertyValue(Object obj, LinkedList<String> parts) {
        return propertyValue(obj,parts,null);
    }

    @SuppressWarnings("unchecked")
    public static void setPropertyValue(Object obj, String string,Object value) {
        setPropertyValue(obj,parsePath(string),value);
    }

    public static void setPropertyValue(Object obj, LinkedList<String> parts,Object value){
        propertyValue(obj,parts,value);
    }
    @SuppressWarnings("unchecked")
    private static <T> T propertyValue(Object obj, LinkedList<String> parts,Object value) {
        Object ret = obj;


        while(parts.size()>0){
            if(ret ==null){
                return null;
            }

            String field=parts.pop();

            try {

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
//
//
//                }

                if(value!=null ) {
                    if(parts.size() == 0){
                        setValue(field,ret,value);
                    }
                    ret  =setNewValueCheck(field,ret);
                } else {
                    ret = getValue(field,ret);
                }
            }catch( SecurityException e) {
                return null;
            }
        }

        return (T) ret;
    }

    public static Optional<Method> findMethod(String methodName, Class clazz, int paramCount){
        Optional<Method> foundMethod = Arrays.stream(clazz
                .getDeclaredMethods()).filter(item -> item.getName().equals(methodName) && item.getParameters().length==paramCount)
                .findFirst();
        if(foundMethod.isPresent()){
            return foundMethod;
        } else if(clazz.getSuperclass()!= Object.class){
            return findMethod(methodName,clazz.getSuperclass(),paramCount);
        } else {
            return Optional.empty();
        }
    }


    private static Object setNewValue(String field, Object objectMethod){
        String methodName = toSetPropertyName(field);
        try {
            Optional<Method> foundMethod = findMethod(methodName,objectMethod.getClass(),1);

            if(foundMethod.isPresent()) {
                Method method = foundMethod.get();
                Parameter[] params = method.getParameters();

                Object newObj;

                if(params[0].getType().isArray()){
                    Class arrayInnerClass = getArrayInnerClass(params[0].getType())
                            //should never happen. If so i am dumber tham i expected.
                            .orElseThrow(()->new PropertyNotFoundException(params[0].getType().getName()+" could not be cast to array"));

                    newObj = Array.newInstance(arrayInnerClass ,1);

                    ((Object[])newObj)[0]=arrayInnerClass.newInstance();
                } else{
                    newObj = params[0].getType().newInstance();
                }

                method.invoke(objectMethod, newObj);

                return newObj;
            }
        }catch(Exception e) {
            return null;
        }
        return null;
    }

    private static Optional<Class> getArrayInnerClass (Class clazz){
        if(clazz.isArray()) {
            try {
                String fatherClassName = clazz.getName().substring(2);
                fatherClassName = fatherClassName.substring(0, fatherClassName.length() - 1);
                return Optional.of( Class.forName(fatherClassName));
            }catch (Exception e){
            }
        }
        return Optional.empty();
    }

    private static Object setNewValueCheck(String field, Object objectMethod){
        Object temp = getValue(field,objectMethod);
        if(temp == null){
            return setNewValue(field,objectMethod);
        }
        return temp;
    }

    private static boolean setValue(String field, Object objectMethod, Object value){
        try {


            if(objectMethod.getClass().isArray()){
                Object[] array = ((Object[]) objectMethod);
                for (Object o : array) {
                    setNewValueCheck(field,o);
                    setValue(field, o, value);
                }

            }else{
                setNewValueCheck(field,objectMethod);
                Optional<Method> method = setPropertyMethod(field,objectMethod.getClass(),value.getClass());

                if(!method.isPresent()){
                    return false;
                }
                method.get().invoke(objectMethod, value);
            }


        }catch(Exception e) {
            return false;
        }
        return true;
    }

    private static<T> Object getValue(String field, T objectMethod){

       if(objectMethod.getClass().isArray()){

           getArrayInnerClass(objectMethod.getClass());
           return  (T[])Arrays
                   .stream(((T[]) objectMethod))
                   .map(item -> getValue(field,item))
                   .toArray();
       }
       Optional<Method> method = getPropertyMethod(field,objectMethod.getClass());
       if(!method.isPresent()){
           return null;
       }
        try {
            return (T)method.get().invoke(objectMethod, (T[]) null);
        }catch(Exception e) {
            return null;
        }
    }

    public static String toGetPropertyName(String field){
        return toGetPropertyName(field,null);
    }
    public static String toGetPropertyName(String field, Class clazz){
        Optional<Method> method =getPropertyMethod(field,clazz);
        return method.map(Method::getName)
                .orElse("get"+fieldNameToPropName(field));
    }

    public static Optional<Method> getPropertyMethod(String fieldOrMethodName,Class clazz){
        String prop;

        if(clazz == null){
            return Optional.empty();
        }

        if( Arrays.stream(GET_METHOD_ALIASES).anyMatch(fieldOrMethodName::startsWith)){
            return findMethod( fieldOrMethodName,clazz,0);
        }

        return getPropertyMethod(fieldNameToPropName(fieldOrMethodName),0,clazz);
    }

    private static Optional<Method> getPropertyMethod(@NonNull String field,int id, @NonNull Class clazz){
        if(id>=GET_METHOD_ALIASES.length){
            return Optional.empty();
        }

        Optional<Method> found = findMethod(GET_METHOD_ALIASES[id]+field,clazz,0);
        if(found.isPresent()){
            return found;
        }
        return getPropertyMethod(field, id + 1, clazz);
    }

    private  static String fieldNameToPropName(String field){
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }


    public static Optional<Method> setPropertyMethod(@NonNull String field, @NonNull Class clazz, @NonNull Class parameter){

        String methodName = toSetPropertyName(field);
        try {
            java.lang.reflect.Method method = clazz.getMethod(methodName, parameter);
            return Optional.of(method);
        }catch(Exception e) {
            return Optional.empty();
        }
    }

    public static String toSetPropertyName(String field){
        if(field.startsWith("set")){
            return field;
        }
        return "set" + fieldNameToPropName(field);
    }





    public static String toFieldName(String property){
        property = property.substring(3);
        return property.substring(0, 1).toLowerCase() + property.substring(1);
    }

    public static Map<String,String>  fields(Object obj){
        Map<String,String> list = new HashMap<String,String>();
        for (Field f : obj.getClass().getDeclaredFields()) {
            String property=f.getName();
            try{
                String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1, property.length());
                java.lang.reflect.Method method = obj.getClass().getMethod(methodName, (Class<?>[])null);
                Object returnValue = method.invoke(obj, (Object[])null);
                if(returnValue!=null){
                    list.put(property,returnValue.toString());
                }
            }
            catch(Exception e){
                list.put(property,e.getMessage());
            }
        }
        return list;
    }

    public static String fieldsStr(Object obj){
        StringBuilder sb= new StringBuilder();
        String first="";
        for (Map.Entry<String, String>  paramMapEntry  : fields(obj).entrySet()) {
            sb.append(first);
            sb.append("\"");
            sb.append(paramMapEntry.getKey());
            sb.append("\":\"");
            sb.append(paramMapEntry.getValue().replaceAll("\"", "\\\\\""));
            sb.append("\"");
            first=",";
        }
        return "{"+sb.toString()+"}";
    }



    public static <T> void updateFirst(T first, T second,boolean force)  {
        PropertyReader.mergeObjects(first,first,second,force);
    }
    public static <T> void updateFirst(T first, T second)  {
        PropertyReader.mergeObjects(first,first,second,false);
    }

    @SuppressWarnings("unchecked")
    public static <T> T mergeObjectss(T first, T second) throws IllegalAccessException, InstantiationException {
        Class<?> clazz = first.getClass();
        Object returnValue = clazz.newInstance();
        return mergeObjects((T) returnValue,first,second,false);
    }

    @SuppressWarnings("unchecked")
    private static <T> T mergeObjects(T returnValue,T first, T second,boolean force) {
        if(returnValue!=null&&first!=null&&second!=null) {
            Class<?> clazz = first.getClass();
            Method[] methods = clazz.getMethods();

            for (Method getMethodFirst : methods) {
                if(getMethodFirst.getName().startsWith("get")) {

                    String getPropertyName = getMethodFirst.getName();
                    String fieldName = toFieldName(getPropertyName);
                    String setPropertyName = toSetPropertyName( fieldName);

                    Object value1 ;
                    Object value2 ;

                    try {
                        value1 = getPropertyValue(first,fieldName);
                    } catch (SecurityException e) {
                        log.warn("metodo set de "+fieldName+" nao esta acessivel em "+clazz.getName());
                        continue;
                    }
                    try {
                        value2 = getPropertyValue(second,fieldName);
                    } catch (SecurityException e) {
                        log.warn("metodo set de "+getPropertyName+" nao esta acessivel em "+second.getClass().getName());
                        continue;
                    }

                    catch (Exception e){
                        log.error("erro desconhecido ao acessar proriedade "+getPropertyName+" nao existe em "+clazz.getName(),e);
                        continue;
                    }

                    Object value;
                    if (force && value2 != null) {
                        value = value2;
                    } else {
                        value = (value1 != null) ? value1 : value2;
                    }

                    //Method setMethod;
                    try {
                        if(returnValue!=null && setPropertyName!=null) {
                            Statement stmt = new Statement(returnValue, setPropertyName, new Object[]{value});
                            stmt.execute();
                        }
                    }catch (NullPointerException e){
                        //log.warn("metodo "+setPropertyName+" é nullo em "+clazz.getName());
                        continue;
                    }catch(java.lang.IllegalArgumentException e ){
                        //log.warn("metodo "+setPropertyName+" nao existe em "+clazz.getName());
                        continue;
                    } catch (SecurityException e) {
                        //log.warn("metodo "+setPropertyName+" nao esta acessivel em "+clazz.getName());
                        continue;
                    }
                    catch (NoSuchMethodException e) {
                        //log.warn("metodo "+setPropertyName+" nao existe em "+clazz.getName());
                        continue;
                    }
                    catch (Exception e){
                        log.error("erro desconhecido ao acessar proriedade "+setPropertyName+" nao existe em "+clazz.getName(),e);
                        continue;
                    }

                }
            }
        }
        return returnValue;
    }
    public static <T> T copyData(T a, Object b){

        updateFirst(a,b);
        return a;
    }

    public static boolean validField(Object objeto,String fieldName){
        if(objeto != null && fieldName != null && fieldName.length()>0) {
            String propertyName = "get";
            String isPropertyName = "is";
            if(Character.isLowerCase(fieldName.charAt(0))){
                char chars[] = fieldName.toCharArray();
                chars[0] = Character.toUpperCase(chars[0]);
                propertyName +=  new String(chars);
                isPropertyName +=new String(chars);
            }else{
                propertyName += fieldName;
                isPropertyName +=new String(fieldName);
            }
            Method method;
            try {
                method = objeto.getClass().getMethod(propertyName, (Class<?>) null);
            }catch (Exception e){
                try {
                    method = objeto.getClass().getMethod(isPropertyName, (Class<?>) null);
                }catch (Exception ex){
                    return false;
                }
            }

            if (method!=null) {
                try {
                    Object result = method.invoke(objeto );
                    return isValid(result);

                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    public static boolean isValid(Object object){
        if(object == null){
            return  false;
        }
        if(object.getClass() == String.class && ((String)object).isEmpty()) {
            return false;
        }

        if (object instanceof Collection) {
            return !((Collection<?>) object).isEmpty();
        }

        return true;
    }


    private static LinkedList<String> parsePath(String path){
        String[] parts = path.split("\\.");
        if(parts.length >0){
            LinkedList<String> arr=new LinkedList<String>(Arrays.asList(parts));
            return arr;
        }
        return new LinkedList<>();
    }

    private static<T> LinkedList<T> castToLinkedList(Iterable<T> itens){
        return StreamSupport.stream(itens.spliterator(), false)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
package io.github.lourencomcviana;


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

public class EnumMapper<T extends Enum<T>> {

//    private Class<T> clazz;
//    public EnumMapper(){
//        this.clazz = new GenericClass<T>().get();
//    }
//
//    public EnumMapper(Class<T> clazz){
//        this.clazz = clazz;
//    }
//
//    HashMap<T, Method> methods;
//    private void generateMapping(){
//        Arrays.stream(clazz.getEnumConstants());
//
//    }
//
//
//    public static boolean validaExistencia(Object object){
//        return Arrays
//                .stream(IndicadorTipoAgricultura.values())
//                .filter(item ->  getItem(item,object).isPresent())
//                .count() == IndicadorTipoAgricultura.values().length;
//    }
//
//
//
//    public static<T> Optional<T> getItem(IndicadorTipoAgricultura item, Object object){
//        try {
//            Object saida = item.get.invoke(object, null);
//            if(ClassInfo.isValid(saida)){
//                return Optional.of( (T)saida );
//            }
//        }catch (Exception e){
//        }
//        return Optional.empty();
//    }
}

package io.github.lourencomcviana;

import io.github.lourencomcviana.PropertyReader;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.*;

import static io.github.lourencomcviana.PropertyReader.getPropertyValue;


@Slf4j
public abstract class BaseMapper<T,N> {

    public BaseMapper(Class<T> firstClass, Class<N> secondClass){
        this.isEqual = firstClass == secondClass;
        this.firstClass=firstClass;
        this.secondClass=secondClass;
    }
    @Getter
    @Setter
    private Map<String,String > mappings= new HashMap<>();
    @Getter
    private Boolean isEqual;
    private Class<T> firstClass;
    private Class<N> secondClass;


    public void addMapping(String FirstField,String SecondField){
        mappings.put(FirstField,SecondField);
    }

    protected T convertSecondToFirst(N item ){
        if(item!=null) {
            //ignora código de conversão se for a mesma classe para Second e entidade
            if (isEqual) {
                return (T)item;
            }

            T newItem = PropertyReader.copyData(newFirst(), item);
            mapSecondToFirst(newItem,item);
            return newItem;
        }
        return null;
    }


    protected N convertFirstToSecond(T item ){
        if(item!=null) {
            if (isEqual) {
                return (N) item;
            }

            N second = PropertyReader.copyData(newSecond(), item);
            mapFirstToSecond(second,item);
            return second ;
        }
        return null;
    }

    protected Collection<T> firstToList(Optional<T> item){
        return itemToList(item);
    }

    protected Collection<N> secondToList(Optional<N> item){
        return itemToList(item);
    }

    private Collection itemToList(Optional item){
        if(item.isPresent()) {
            var list = new LinkedList<>();
            list.add(item.get());
            return list;
        }
        return null;
    }

    protected  Collection<N> convertFirstToListSecond(Optional<T> item ){
        return convertListFirstToListSecond(firstToList(item));
    }

    protected  Collection<T> convertSecondToListFirst(Optional<N> item ){
        return convertListSecondToListFirst(secondToList(item));
    }
    protected Collection<N> convertListFirstToListSecond(Collection<T> itens ) {
        if (itens != null) {
            var newList = new LinkedList<N>();
            if (isEqual)
                return (Collection<N>) itens;

            for (var item : itens) {
                newList.push(convertFirstToSecond(item));
            }

            return newList;
        } else {
            return null;
        }
    }

    protected Collection<T> convertListSecondToListFirst(Collection<N> itens ){
        if(itens!=null  ) {
            if (isEqual) {
                return (Collection<T>) itens;
            }

            var list=new LinkedList<T>();
            for (var item : itens) {
                list.add(convertSecondToFirst(item));
            }

            return list;
        }else{
            return  null;
        }
    }

    /***
     * Utiliza mapeamento manual de Second para entidade
     * @param first quem receberá o mapeamento
     * @param second fonte do mapeamento
     */
    protected void mapSecondToFirst(Object first, Object second){

        for(Map.Entry<String, String>  param :mappings.entrySet()){
            Object SecondValue;
            try{
                SecondValue=getPropertyValue(second,param.getValue());
                if(SecondValue!=null){
                    PropertyReader.setPropertyValue(first,param.getKey(),SecondValue);
                }
            }catch(NullPointerException e){
                log.debug( "parameter "+param.getKey()+" has  no value");
            }
            catch(Exception e){
                log.error( "parameter "+param.getKey()+" cannot be added",e);
            }
        }
    }

    /***
     * Utiliza mapeamento manual de entidade para Second
     * @param second quem receberá o mapeamento
     * @param first fonte do mapeamento
     */
    protected void mapFirstToSecond( Object second,Object first){
        for(Map.Entry<String, String>  param :mappings.entrySet()){
            Object FirstValue;
            try{
                FirstValue=getPropertyValue(first,param.getKey());
                if(FirstValue!=null){
                    PropertyReader.setPropertyValue(second,param.getValue(),FirstValue);
                }
            }catch(NullPointerException e){
                log.debug( "parameter "+param.getValue()+" has  no value");
            }
            catch(Exception e){
                log.error( "parameter "+param.getValue()+" cannot be added",e);
            }
        }
    }

    protected T newFirst(){
        try {
            return firstClass.newInstance();
        }catch (Exception e){
            log.error("não foi possível gerar entidade",e);
            return null;
        }
    }
    protected N newSecond(){
        try {
            return secondClass.newInstance();
        }catch (Exception e){
            log.error("não foi possível gerar Second",e);
            return null;
        }
    }

}

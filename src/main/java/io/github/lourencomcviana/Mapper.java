package io.github.lourencomcviana;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Optional;

@Slf4j
public class Mapper<T,N> extends BaseMapper<T,N> {

    public Mapper(Class<T> firstClass, Class<N> secondClass){
        super(firstClass,secondClass);
    }


    @Override
    public T convertSecondToFirst(N item ){
      return super.convertSecondToFirst(item);
    }

    @Override
    public N convertFirstToSecond(T item ){
        return super.convertFirstToSecond(item);
    }

    @Override
    public Collection<N> convertFirstToListSecond(Optional<T> item ){
        return super.convertFirstToListSecond(item);
    }

    @Override
    public Collection<N> convertListFirstToListSecond(Collection<T> itens ) {
        return super.convertListFirstToListSecond(itens);
    }

    @Override
    public  Collection<T> convertListSecondToListFirst(Collection<N> itens ){
       return super.convertListSecondToListFirst(itens);
    }

    @Override
    public void mapSecondToFirst(Object first, Object second){
        super.mapSecondToFirst(first,second);
    }

    @Override
    public void mapFirstToSecond( Object second,Object first){
        super.mapFirstToSecond(first,second);
    }
}

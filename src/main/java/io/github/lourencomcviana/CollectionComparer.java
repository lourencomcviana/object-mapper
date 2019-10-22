package io.github.lourencomcviana;


import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CollectionComparer<T> {

    @Getter
    private Collection<T> inserted = new LinkedList<>();
    @Getter
    private Collection<T> edited = new LinkedList<>();
    @Getter
    private Collection<T> removed = new LinkedList<>();

    private String idFieldName;

    public CollectionComparer(String idFieldName) {
        this.idFieldName = idFieldName;
    }


    public static <T> CollectionComparer<T> compare(Collection<T> oldList, Collection<T> newList, String idFieldPath) {
        CollectionComparer<T> comparer = new CollectionComparer<>(idFieldPath);
        comparer.compare(oldList, newList);
        return comparer;
    }

    private void compare(Collection<T> oldList, Collection<T> newList) {
        if (oldList != null && newList == null) {
            edited = oldList;

        } else if (oldList == null && newList != null) {
            inserted = oldList;

        } else if (oldList != null && newList != null) {
            // any item that has no id is considered inserted
            List<T> insertedList = newList.stream()
                    .filter(item -> id(item,oldList).isNew())
                    .collect(Collectors.toList());

            // any item that exists in the old list but do not exists in the new list is removed
            List<T> removedList = oldList.stream()
                    .filter(oldItem -> newList.stream()
                            .filter(newItem ->  id(newItem,oldList).isNotNew())
                            .noneMatch(newItem ->compareItem(oldItem,newItem,oldList))
                    ).collect(Collectors.toList());


            // any item that exist in the old list and was not removed
            List<T> editedList = oldList.stream()
                    .filter(oldItem ->
                            removedList.stream().noneMatch(removedItem -> removedItem == oldItem)
                    )
                    .collect(Collectors.toList());

            this.inserted = insertedList;
            this.removed = removedList;
            this.edited = editedList;
        }
    }


    private boolean compareItem(Object oldItem,Object newItem, Collection<T> oldItens){
        New newId = id(newItem,oldItens);
        New oldId = id(oldItem,oldItens);

        if(!newId.isPresent() || !oldId.isPresent()){
            return false;
        }
        return  newId.getItem().get().equals( oldId.getItem().get());
//
//        PropertyReader.getPropertyValue(newItem, idFieldName)
//                .equals(
//                        PropertyReader.getPropertyValue(oldItem, idFieldName)
//                )

    }
    private New<Object> id(Object object, Collection<T> oldItens){

        if(this.idFieldName!=null) {
            Object value= PropertyReader.getPropertyValue(object, this.idFieldName);

            if(value==null){
                return New.is();
            }
            if(oldItens!=null && oldItens.stream().noneMatch(item->id(item,null).getItem().orElse(false).equals(value))){
                return New.is(value);
            }

            return New.isNot(value);
        } else{
            if(object==null){
                return New.is();
            }
            return New.isNot(object);
        }
    }
    public Collection<T> getInsertedAndEdited() {
        Collection<T> joined = new LinkedList<>();
        joined.addAll(inserted);
        joined.addAll(edited);
        return joined;
    }


    private static class New<T>{
        public static<T> New<T> isNot(T item){
            New<T> newObject = new New<>();
            if(item==null){
                newObject.item= Optional.empty();
            }else{
                newObject.item= Optional.of(item);
            }
            newObject.isNew=false;
            return newObject;
        }

        public static<T> New<T> is(T item){
            New<T> newObject = new New<>();
            if(item==null){
                newObject.item= Optional.empty();
            }else{
                newObject.item= Optional.of(item);
            }
            newObject.isNew=true;
            return newObject;
        }

        public static<T> New<T> is(){
            return is(null);
        }

        public static<T> New<T> isNot(){
            return isNot(null);
        }

        @Getter
        private Optional<T> item;

        @Getter
        private boolean isNew;

        public boolean isNotNew(){
            return !isNew;
        }

        public boolean isPresent(){
            return item.isPresent();
        }

    }
}

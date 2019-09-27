package io.github.lourencomcviana;

import io.github.lourencomcviana.exceptions.GenericClassNotFoundException;

import java.lang.reflect.ParameterizedType;

public class GenericClass<T> {
    public Class<T> get() {
        try {
          return ((Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0]);
        } catch (Exception e) {
            throw new GenericClassNotFoundException("Generic parameter passed to " + EnumMapper.class.getName() + " was not parsed correctly, please pass it manually and maybe you can debug why is not working", e);
        }

    }

}

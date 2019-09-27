package io.github.lourencomcviana.exceptions;

public class GenericClassNotFoundException extends RuntimeException {
    public GenericClassNotFoundException(String err, Exception e){
        super(err,e);
    }
}

package io.github.lourencomcviana.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MethodNotFoundException extends  Exception {
    public MethodNotFoundException(String message){
        super(message);
    }
}

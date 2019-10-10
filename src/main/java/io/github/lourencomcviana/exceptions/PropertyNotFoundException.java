package io.github.lourencomcviana.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PropertyNotFoundException extends  Exception {
    public PropertyNotFoundException(String message){
        super(message);
    }
}

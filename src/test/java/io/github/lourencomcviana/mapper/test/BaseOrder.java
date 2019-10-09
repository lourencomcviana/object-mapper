package io.github.lourencomcviana.mapper.test;

import lombok.*;

public abstract class BaseOrder  {


    @Getter
    @Setter
    String value;

    @Getter
    String onlyGetter;


    @Setter
    String onlySetter;
}

package com.br.object.path;

import lombok.Getter;
import lombok.var;

public class Main {

    @Getter
    private String parada ="aa";
    public static void main(String args[]){

        var a = new Main();
        a.getParada();
    }

}

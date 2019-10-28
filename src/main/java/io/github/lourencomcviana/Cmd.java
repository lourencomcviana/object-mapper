package io.github.lourencomcviana;

import java.util.Arrays;

public class Cmd {
    public static void main (String[] args){

        if(args!=null && args.length>0){
           if (Arrays.asList(args).contains("-v")){

               String version  = Cmd.class.getPackage().getImplementationVersion();
               System.out.println(version);
           }
        }
    }

}

package io.github.lourencomcviana;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Cmd {

    static final String PARAM_DATE_PARSE = "-d *([\\w\\/-\\\\]+)";
    public static void main (String[] args){
        try{

            boolean run =false;
            if(args!=null && args.length>0){
                List<String> foundArgs;
                if (Arrays.asList(args).contains("-v")){

                   String version  = Cmd.class.getPackage().getImplementationVersion();
                   System.out.println(version);
                    run = true;
                }

                foundArgs = Arrays.stream(args).filter(item -> item.matches(PARAM_DATE_PARSE))
                        .collect(Collectors.toList());
                if (!foundArgs.isEmpty()){
                    System.out.println("parsing dates");
                    executeDateParse(foundArgs);
                    foundArgs=null;
                    run = true;
                }

            }
            if(!run){
                System.out.println("-v -> for version");
                System.out.println("-d (your date here) -> for parse date");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void executeDateParse(Collection<String> args){

        final Pattern pattern = Pattern.compile(PARAM_DATE_PARSE);


        args.forEach(str->{
            Matcher matcher = pattern.matcher(str);
            if(matcher.find()) {
                String arg = matcher.group(1);

                LocalDateTime dt = DateUtil.parseStringToDateTime(arg);

                if (dt == null) {
                    System.out.println("invalid date");
                } else {
                    System.out.println(dt.toString());
                }
            }

        });


    }
}

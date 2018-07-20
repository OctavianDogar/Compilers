package ADT;

import util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Octavian on 10/23/2016.
 */
public class Test {

    public static void print(Object toPrint){
        System.out.println(toPrint);
    }

    public static void main(String [] args){

        //ident
        String probe ="identifi";
        System.out.println("Ident "+ (probe.matches("[a-zA-Z]+[a-zA-Z0-9]*") && probe.length()<9));

        //separators
        probe = ".,;:{}?[]()'\n";
        System.out.println("Sep "+probe.matches("[,.' \\[\\]\\{\\}\\(\\)<>\\?;:\\n]+"));

        probe= "'"+"asd"+"'";

        System.out.println("String "+ probe.matches("'\\p{Alnum}+'"));

        //operators
        probe = "!+-*/<=>";
        System.out.println("Ops " + probe.matches("[\\+\\-\\*/<=>!]+"));

        probe = "!"+"=";
        System.out.println("Is different "+probe.matches("!=")); //PENTRU ASTA CAND TOKENIZEZ PUN TOKENS IN ARRAY DE STIRNG.
        //isEqual
        //isLessOrEqual
        //isGreaterOrEqual

        //const
        probe = "1322";
        System.out.println("Num const "+probe.matches("[-]?[\\d]+"));
        probe = "'20as1dsa'";
        System.out.println("Str const "+probe.matches("'\\p{Alnum}+'"));

        List<String> reserved = Arrays.asList("char","const",
                "do","if","then","in","bool","print",
                "while","for","int","break","&&","||",
                "real","string","struct");
        probe ="print";

        System.out.println("Reserved "+reserved.contains(probe));


    }

}

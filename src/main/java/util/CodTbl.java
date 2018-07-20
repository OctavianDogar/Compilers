package util;

import java.util.HashMap;

/**
 * Created by Octavian on 10/23/2016.
 */
public class CodTbl {

    private static final HashMap<String,Integer> codTbl = new HashMap<>();
    static {
        codTbl.put("identifier",1);
        codTbl.put("constant",2);
        codTbl.put("array",3);
        codTbl.put("of",4);
        codTbl.put("String",5);
        codTbl.put("int",6);
        codTbl.put("real",7);
        codTbl.put("bool",8);
        codTbl.put("read",9);
        codTbl.put("print",10);
        codTbl.put("for",11);
        codTbl.put("while",12);
        codTbl.put("break",13);
        codTbl.put("in",14);
        codTbl.put("if",15);
        codTbl.put("else",16);
        codTbl. put("&&",17);
        codTbl. put("||",18);
        codTbl. put("!=",19);
        codTbl. put("==",20);
        codTbl. put(";",21);
        codTbl. put(",",22);
        codTbl. put(".",23);
        codTbl. put("+",24);
        codTbl. put("*",25);
        codTbl. put("/",26);
        codTbl. put("(",27);
        codTbl. put(")",28);
        codTbl. put("[",29);
        codTbl. put("]",30);
        codTbl. put("-",31);
        codTbl. put("<",32);
        codTbl. put(">",33);
        codTbl. put("=",34);
        codTbl. put("{",35);
        codTbl. put("}",36);
        codTbl. put("<=",37);
        codTbl. put(">=",38);
        codTbl. put("'",39);
        codTbl. put("#start",40);
        codTbl. put("#end",41);
    }

    public static Integer get(Object key){
        return codTbl.get(key);
    }



}

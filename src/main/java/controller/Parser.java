package controller;

import ADT.PIF;
import ADT.SymTbl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import util.CodTbl;
import util.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import static org.apache.commons.io.FileUtils.readLines;

/**
 * Created by Octavian on 10/23/2016.
 */
@NoArgsConstructor
@Getter
@Setter
public class Parser {

    private static final String separators = ".,;:{}?[]()' !+-*/<=>\n\r";
    public static final String inputFile = "./src/main/resources/Test1.txt";
    public static final String parsedArrayFile = "./src/main/resources/ParsedArray.txt";

    private static final List<String> reserved = Arrays.asList("char","const",
            "do","if","in","bool","print",
            "while","for","int","break","&&","||",
            "real","String","struct");

    private Integer inputLinesNumber;
    private Integer currentLineNumber = 1;

    private SymTbl symTbl;
    private PIF pif;

    private List<String> tokens;

    public Parser(String inputFile){

        String content="";
        try {
            inputLinesNumber = readLines(new File(inputFile)).size();
            content = FileUtils.readFileToString(new File(inputFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        symTbl = new SymTbl();
        pif = new PIF();
        StringTokenizer st = new StringTokenizer(content,separators,true);
        tokenize(st);
    }

    public void tokenize(StringTokenizer tokenizer){
        tokens = new ArrayList<>();
        while(tokenizer.hasMoreTokens()){
            tokens.add(tokenizer.nextToken());
        }
        try {
            FileUtils.writeLines(new File(parsedArrayFile),tokens);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isIdentifier(String probe){
        return probe.matches("[a-zA-Z]+[a-zA-Z0-9]*") && probe.length()<9;
    }

    public boolean isSeparator(String probe){
        return probe.matches("[,.'\\[\\]\\{\\}\\(\\)\\?;:]+");
    }

    public boolean isStringConst (String probe) {
        return probe.matches("'\\p{Alnum}+'");
    }

    public boolean isNumConst(String probe){
        return probe.matches("[-]?[\\d]+");
    }

    public boolean isOperator(String probe){
        return probe.matches("[\\+\\-\\*/<=>!]+");
    }

    public boolean isConstant(String probe){
        return isNumConst(probe) || isStringConst(probe);
    }

    public boolean isReserved(String probe){
        return reserved.contains(probe);
    }

    public void parse() throws ParseException{
        for (String s: tokens){
            if(s.equals("\n")){
                currentLineNumber++;
                continue;
            }
            if(isConstant(s)) {
                pif.addConst(symTbl.addConst(s));
            }else if (isReserved(s)){
                pif.addToken(s);
            }else if(isIdentifier(s)){
                if(tokens.indexOf(s)==0){
                    pif.addIdent(symTbl.addIdent(s));
                }else if(tokens.get( tokens.indexOf(s)-1).equals("'") && tokens.get( tokens.indexOf(s)+1).equals("'")) {
                    pif.addIdent(symTbl.addConst("'" + s + "'"));
                }else{
                    pif.addIdent(symTbl.addIdent(s));
                }
            }else if(isOperator(s)){
                String next = null;
                String last = null;
                boolean isFirst = false;
                boolean isLast = false;
                if(tokens.indexOf(s)==0 && tokens.indexOf(s)==tokens.size()-1){
                    isFirst=true; isLast = true;
                }else if(tokens.indexOf(s)==0 && !(tokens.indexOf(s)==tokens.size()-1)){
                    isFirst = true;
                    next = tokens.get( tokens.indexOf(s)+1);
                }else if(tokens.indexOf(s)==tokens.size()-1){
                    last = tokens.get( tokens.indexOf(s)-1);
                    isLast = true;
                }else{
                    last = tokens.get( tokens.indexOf(s)-1);
                    next = tokens.get( tokens.indexOf(s)+1);
                }

                if(!isFirst && (last.equals("=") || last.equals("<") || last.equals(">")
                        || last.equals("!") || last.equals("|") || last.equals("&"))  ){
                    continue;
                }
                if(!isLast){
                    if (s.equals("=") && (next.equals("=")) ){
                        pif.addToken(s+next);
                    }
                    else if (s.equals("!") && (next.equals("=")) ){
                        pif.addToken(s+next);
                    }
                    else if (s.equals("<") && (next.equals("=")) ){
                        pif.addToken(s+next);
                    }
                    else if (s.equals(">") && (next.equals("=")) ){
                        pif.addToken(s+next);
                    }
                    else if (s.equals("|") && (next.equals("|")) ){
                        pif.addToken(s+next);
                    }
                    else if (s.equals("&") && (next.equals("&")) ){
                        pif.addToken(s+next);

                    }else if(isFirst && isLast){
                            pif.addToken(s);
                    }else{
                        throw new ParseException("something went wrong on line "+currentLineNumber);
                    }
                }else{
                    pif.addToken(s);
                }
            }else if(isSeparator(s)){
                pif.addToken(s);
            }else if(!s.equals("\t") && !s.equals("\r") && !s.equals(" ")){
                throw new ParseException("something went wrong on line "+currentLineNumber);
            }


        }
    }

    @Override
    public String toString(){
        return getPif().toString()+'\n'+getSymTbl().toString();
    }
}

package model;


import exceptions.GrammarException;
import exceptions.MissingElemException;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Created by Flavius on 2017-01-15.
 */
@Getter
@Setter
public class ParsingTree {
    private CustomStack<Character> initialStack;
    private CustomStack<Character> workStack;
    private List<Integer> output;
    private Analyzer analyzer;

    public ParsingTree(Analyzer analyzer) throws GrammarException {
        this.initialStack = new CustomStack<>();
        this.workStack = new CustomStack<>();
        this.output = new CustomArrayList<>();
        this.analyzer = analyzer;
        setUp();
        checkSequence("a*(a+a)");
    }

    private void setUp() throws GrammarException {
        initialStack.push('%');
        workStack.push('%');
        analyzer.computeAllFirsts();
        analyzer.computeAllFollows();
        analyzer.fillParsingTable();
    }

    public Boolean checkSequence(String aString){
        String newString = aString.replace(" ", "");
        for(Character character:newString.toCharArray()){
            if(!analyzer.getGrammar().getTerminals().contains(character)){
                return false;
            }
        }
        //Stacks setup
        for(Character character : new StringBuilder(newString).reverse().toString().toCharArray()){
            initialStack.push(character);
        }
        workStack.push('S');
        System.out.println(toString());
        while(true){
            Character kPair = workStack.peek();
            Character vPair = initialStack.peek();



            if(kPair.equals(vPair)){
                if(kPair.equals('%')){
                    break;
                }
                else{
                    pop();
                }
            }
            else{
                kPair = workStack.pop();
                if(kPair.equals('$')){
                    //NOTHING
                }
                else {
                    if(initialStack.size() == 1){
                        vPair = '$';
                    }
                    Pair<Character, Character> aPair = new Pair<>(kPair, vPair);

                    for (Pair existingPair : analyzer.getParsingTable().keySet()) {
                        if (aPair.equals(existingPair)) {
                            aPair = existingPair;
                        }
                    }
                    if (analyzer.getParsingTable().containsKey(aPair)) {
                        push(analyzer.getParsingTable().get(aPair));
                    } else {

                        return false;
                    }
                }
            }
            System.out.println(toString());
        }

        System.out.println(toString());
        return true;
    }

    private void push(Pair<Set<Character>, Integer> aPair){
        List<Character> list = new ArrayList<>(aPair.getKey());
        Collections.reverse(list);
        for (Character character:
             list) {
            workStack.push(character);
        }
        output.add(aPair.getValue());
    }

    private void pop(){
        initialStack.pop();
        workStack.pop();
    }

    @Override
    public String toString() {
//        return "ParsingTree{" +
//                "initialStack=" + initialStack +
//                ", workStack=" + workStack +
//                ", output=" + output +
//                ", analyzer=" + analyzer +
//                '}';
        return "|- (" + initialStack +
                "," + workStack +
                "," + output +
                ")";
    }
}

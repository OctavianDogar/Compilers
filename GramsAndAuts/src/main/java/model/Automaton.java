package model;

import com.rits.cloning.Cloner;
import exceptions.AutomatonException;
import exceptions.GrammarException;
import exceptions.MissingElemException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Octavian on 11/27/2016.
 */
@Getter
@Setter
public class Automaton {

    private static final String NON_EXISTING_STATE =
            "The chosen state does not exist in the current automaton.";
    private static final String TRANSITION_TOO_LONG =
            "Attempted to add a transition longer than 2.";

    public static final String AUT_FILE_1 =
            "D:\\Mars workspace\\Compilators\\GramsAndAuts\\src\\main\\resources\\aut1.txt";
    public static final String AUT_FILE_2 =
            "D:\\Mars workspace\\Compilators\\GramsAndAuts\\src\\main\\resources\\aut2.txt";


    private Set<Character> states;

    private Set<Character> alphabet;

    /*
    Similar design to the grammar's productions"
    There, the productions of one elem were sets inside a list  A=[[a, D], [e, A]]
    Here, (A,a) -> D and (A,e) -> A
     */
    private Map<Character,List<Set<Character>>> transitions;

    private Set<Character> Q0;

    private Set<Character> F;

    public Automaton(){
        states = new LinkedHashSet<>();
        alphabet = new LinkedHashSet<>();
        transitions = new LinkedHashMap<>();
        Q0 = new LinkedHashSet<>();
        F = new LinkedHashSet<>();
    }

    public void addState(Character s){
        transitions.put(s,new ArrayList<>());
        states.add(s);
    }

    public void addToAlphabet(Character letter){
        alphabet.add(letter);
    }

    public void addToFinal(Character f){
        F.add(f);
    }

    public List<Set<Character>> getTransitionsOfState(Character state) throws MissingElemException {
        if(!states.contains(state))
            throw new MissingElemException(state.toString());
        return transitions.get(state);
    }

    public void addTransition(Character state, Set<Character> waySet) throws GrammarException{
        if(!states.contains(state)){
            throw new AutomatonException(NON_EXISTING_STATE);
        }
        for(Character c : waySet){
            if(!states.contains(c) && !alphabet.contains(c)){
                throw new MissingElemException(c.toString());
            }
        }
        if(waySet.size()>2) {
            throw new GrammarException(TRANSITION_TOO_LONG);
        }
        if(waySet.size()==1 && new ArrayList<>(waySet).get(0).equals('$')){
            addToFinal(state);
        }

        transitions.get(state).add(waySet);

    }

    public Grammar toGrammar() throws MissingElemException {

        Grammar g = new Grammar();
        /*
        non-terminals
         */
        g.setNonTerminals(states);

        /*
        terminals
         */
        g.setTerminals(alphabet);
        /*
        startState
        */
        if(!this.Q0.isEmpty()){
            Q0.stream().forEach(q0 -> g.getS().add(q0));
        }

        /*
        productions
         */
        g.setProductions(transitions);

        /*
        transitions of final states
         */
        if(!F.isEmpty()){
            List<Character> aux = new ArrayList<>(F);
            for(Character c: aux){
                for(Set<Character> set: new Cloner().deepClone(transitions.get(c))){
                    List<Character> transAux = new ArrayList<>(set);
                    if(transAux.get(0).equals('$')) continue;
                    if(transAux.size()>1){
                        Set<Character> newSet1 = new LinkedHashSet<>();
                        newSet1.add(transAux.get(0));
                        g.getProductionsOfNonterminal(c).addAll(Arrays.asList(newSet1));
                    }
                }
            }
        }

        return g;

    }

    public static Automaton intakeFromFile(String filePath) throws GrammarException{
        Automaton a = new Automaton();
        StringTokenizer st;
        Set<Character> transSets;
        boolean statesDone=false;
        boolean alphaDone=false;
        boolean prodsDone=false;

        List<String> lines = new ArrayList<>();
        try {
            lines = FileUtils.readLines(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String line: lines){
            if(line.charAt(0)=='#') continue;
            //after the first # come the states
            if(!statesDone){
                st = new StringTokenizer(line,"|",false);
                while (st.hasMoreTokens()){
                    a.addState(st.nextToken().charAt(0));
                }
                statesDone = true;
                continue;
            }
            if(!alphaDone){
                st = new StringTokenizer(line,"|",false);
                while (st.hasMoreTokens()){
                    a.addToAlphabet(st.nextToken().charAt(0));
                }
                alphaDone = true;
                continue;
            }
            if(!prodsDone){
                Character transKey = line.charAt(0);
                StringTokenizer tokenizer = new StringTokenizer(line.substring(2,line.length()),"|",false);
                while (tokenizer.hasMoreTokens()){
                    transSets = new LinkedHashSet<>();
                    String nextToken = tokenizer.nextToken();
                    char[] aux = nextToken.toCharArray();
                    for(char c: aux){
                        transSets.add(c);
                    }
                    a.addTransition(transKey,transSets);
                }
            }
        }
        return a;

    }


    @Override
    public String toString() {

        String transString="";
        for(Character c: states){
            try {
                for(Set<Character> set: getTransitionsOfState(c)){
                    List<Character> aux = new ArrayList<>(set);
                    if(aux.size()==1){
                        transString+="("+c+")"+"->"+aux.get(0)+";";
                    }else{
                        transString+="("+c+aux.get(0)+")"+"->"+aux.get(1)+";";
                    }
                }
            } catch (MissingElemException e) {
                e.printStackTrace();
            }
        }

        return "Automaton{" + '\n'+
                "states=" + states + '\n'+
                ", alphabet=" + alphabet + '\n'+
                ", transitions=" + transString + '\n'+
                ", Q0=" + Q0 + '\n'+
                ", F=" + F + '\n'+
                '}';
    }

    public Map<Character, List<Set<Character>>> getTransitions() {
        return transitions;
    }
}

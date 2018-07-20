package model;

import com.rits.cloning.Cloner;
import exceptions.GrammarException;
import exceptions.MissingElemException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Octavian on 11/14/2016.
 */
@Getter
@Setter
public class Grammar {

    private static final Cloner CLONER = new Cloner();

    private static final String NON_EXISTING_NONTERMINAL =
            "The chosen non-terminal does not exist in the current grammar.";
    private static final String PRODUCTION_TOO_LONG =
            "Attempted to add a production longer than 2.";

    public static final String GRAM_FILE_1 =
            "E:\\Projects\\Formal languages and compilers\\Lab4- LFC\\gramm1.txt";
    public static final String GRAM_FILE_2 =
            "D:\\Mars workspace\\Compilators\\GramsAndAuts\\src\\main\\resources\\gramm2.txt";
    public static final String GRAM_FILE_3 =
            "D:\\InteliJ Workspace\\Compilers\\GramsAndAuts\\src\\main\\resources\\gramm3.txt";
    private boolean twoLimit = false;
    /*
    Set of all non-terminals (UPPERCASE)
     */
    private Set<Character> nonTerminals;
    /*
    Set of all terminals (LOWERCASE)
     */
    private Set<Character> terminals;
    /*
    Each non-terminal has 0 or more modifying ways.
    Each way is composed of a Set of terminals and nonterminals
    All the non-terminal's ways form a list
    The non-terminal and it's way-list form a mapping in the grammar's productions map
     */
    private Map<Character,List<Set<Character>>> productions;

    private boolean onlyOneEpsilon;

    private Set<Character> S;

    public Grammar(){
        nonTerminals = new LinkedHashSet<>();
        terminals = new LinkedHashSet<>();
        productions = new LinkedHashMap<>();
        onlyOneEpsilon = false;
        S = new LinkedHashSet<>();
//        setStartingNonterminal();
    }

    /*
    waylist Set is to be created on UI and passed here
     */
    public void addProduction(Character nonTerminal, Set<Character> waySet) throws GrammarException {

        if(!nonTerminals.contains(nonTerminal)){
            throw new GrammarException(NON_EXISTING_NONTERMINAL);
        }

        for(Character c : waySet){
            if(!nonTerminals.contains(c) && !terminals.contains(c)){
                throw new MissingElemException(c.toString());
            }
        }
        if(waySet.size()>2 && twoLimit){
            throw new GrammarException(PRODUCTION_TOO_LONG);
        }
        if(waySet.size()==1 && new ArrayList<>(waySet).get(0).equals('$')){
            S.add(nonTerminal);
            if(!S.isEmpty() && onlyOneEpsilon && !nonTerminal.equals('S'))
                onlyOneEpsilon = false;
            else
                onlyOneEpsilon = true;
        }

        productions.get(nonTerminal).add(waySet);

    }

    public void addNonTerminal(Character c){
        productions.put(c,new ArrayList<>());
        nonTerminals.add(c);
    }

    public void addTerminal(Character c){
        terminals.add(c);
    }

    public void setStartingNonterminal(){
        onlyOneEpsilon = true;
        Character S = new Character('S');
        this.S.add(S);
        addNonTerminal(S);
        addTerminal(new Character('$'));
        productions.put(S,new ArrayList<>());
        Set<Character> waySet = new LinkedHashSet<>();
        waySet.add('$');
        try {
            addProduction(S,waySet);
        } catch (GrammarException e) {
            e.printStackTrace();
        }
    }

    public List<Set<Character>> getProductionsOfNonterminal(Character nonTerminal) throws MissingElemException {
        if(!nonTerminals.contains(nonTerminal))
            throw new MissingElemException(nonTerminal.toString());
        return productions.get(nonTerminal);
    }

    public boolean isRightLinear(){
        List<Character> aux;
        for(Character c :productions.keySet()){
            for(Set<Character> s: productions.get(c)){
                 aux = new ArrayList<>(s);
                 boolean isStart = aux.size()==1 && aux.get(0).equals('$');
                if(!nonTerminals.contains(aux.get(aux.size()-1)) && !isStart)
                    return false;
            }
        }
        return true;
    }

    public boolean isStart(Character s) throws MissingElemException {
        for(Set<Character> set: getProductionsOfNonterminal(s)){
            if (set.contains('$')) return true;
        }
        return false;
    }

    public boolean passEpsilon() throws MissingElemException {
        if(!onlyOneEpsilon && !S.isEmpty()){
            return false;
        }
        for(Character c: productions.keySet()){
            for(Set<Character> s: productions.get(c)){
                List<Character> startingSet = new ArrayList<>(getS());
                for (Character ch: startingSet){
                    if(!getS().isEmpty() && s.contains(ch)){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean isRegular() throws MissingElemException {
        if(isRightLinear() && passEpsilon())
            return true;
        return false;
    }

    public Automaton toAutomaton() throws GrammarException {
        Automaton aut = new Automaton();
        if(!isRegular())
            throw new GrammarException("The chosen grammar is not regular.");
        /*
        States
         */
        Set<Character> states = nonTerminals;
//        aut.addState(new Character('K'));
        aut.getTransitions().put(new Character('K'),new ArrayList<>());
        aut.setStates(states);
        /*
        Starting state
         */
        aut.setQ0(S);

        /*
        Alphabet
         */
        aut.setAlphabet(terminals);
        /*
        Transitions
         */
        aut.setTransitions(productions);
        /*
        Final states
         */
        Set<Character> finals = new LinkedHashSet<>();
        finals.add(new Character('K'));
        if(!S.isEmpty()){
            finals.addAll(S);
        }
        aut.setF(finals);

        return aut;
    }

    public static Grammar intakeFromFile(String filePath) throws GrammarException, IOException {
        Grammar g = new Grammar();
        StringTokenizer st;
        Set<Character> prodSets;
        boolean ntsDone=false;
        boolean termsDone=false;
        boolean prodsDone=false;

        List<String> lines = new ArrayList<>();
        try {
            lines = FileUtils.readLines(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String line: lines){
            if(line.charAt(0)=='#') continue;
            //after the first # come the nonterminals
            if(!ntsDone){
                st = new StringTokenizer(line,"|",false);
                while (st.hasMoreTokens()){
                    g.addNonTerminal(st.nextToken().charAt(0));
                }
                ntsDone = true;
                continue;
            }
            if(!termsDone){
                st = new StringTokenizer(line,"|",false);
                while (st.hasMoreTokens()){
                    g.addTerminal(st.nextToken().charAt(0));
                }
                termsDone = true;
                continue;
            }
            if(!prodsDone){
                Character prodKey = line.charAt(0);
                StringTokenizer tokenizer = new StringTokenizer(line.substring(2,line.length()),"|",false);
                while (tokenizer.hasMoreTokens()){
                    prodSets = new LinkedHashSet<>();
                    String nextToken = tokenizer.nextToken();
                    char[] aux = nextToken.toCharArray();
                    for(char c: aux){
                        prodSets.add(c);
                    }
                    g.addProduction(prodKey,prodSets);
                }
            }

        }
        Analyzer analyzer = new Analyzer(g);
        analyzer.computeAllFirsts();
        analyzer.computeAllFollows();
        analyzer.fillParsingTable();
        FileUtils.writeStringToFile(new File("./gigica.txt"),analyzer.getFirst().toString());
        FileUtils.writeStringToFile(new File("./gigica.txt"),analyzer.getFollow().toString());
        FileUtils.writeStringToFile(new File("./gigica.txt"),analyzer.getParsingTable().toString());
        ParsingTree parsingTree = new ParsingTree(analyzer);
        return g;

    }

    public static Map<Character,List<Set<Character>>> obtainEliminatedSingleProdctions(Grammar grammar) throws MissingElemException {

        Map<Character,List<Set<Character>>> singleProdMap = Grammar.eliminateSingleProductions(grammar);
        Map<Character,List<Set<Character>>> auxProductions = CLONER.deepClone(grammar.getProductions());

        for(Character c: grammar.getNonTerminals()){
            auxProductions.get(c).clear();
        }

        for(Character c: grammar.getNonTerminals()){
            List<Set<Character>> aux = singleProdMap.get(c);
            Set<Character> goodSet = aux.get(aux.size()-1);
            List<Set<Character>> productionsContainer = new ArrayList<>();
            for(Character cr: goodSet){
                List<Set<Character>> prods = grammar.getProductionsOfNonterminal(cr);
                for(Set<Character> set:prods){
                    if(set.size()>1){
                        productionsContainer.add(set);
                    }else if(set.size()==1 && grammar.getTerminals().contains(new ArrayList<>(set).get(0))){
                        productionsContainer.add(set);
                    }
                }
                auxProductions.put(c,productionsContainer);
            }
        }
        return auxProductions;
    }

    public static Map<Character,List<Set<Character>>> eliminateSingleProductions(Grammar grammar) throws MissingElemException {
        /*
        partials list containing each version of nonTerminal reaches
         */
        Map<Character,List<Set<Character>>> partials = new LinkedHashMap<>();
        /*
        nested list instantiations
         */
        Set<Character> nonTerminals = grammar.getNonTerminals();
        for(Character nt: nonTerminals) {
            List<Set<Character>> specificPartials = new ArrayList<>();
            partials.put(nt, specificPartials);
        }
        for(Character nt:nonTerminals){
            List<Set<Character>> partialsList  = partials.get(nt);

            Set<Character> first = new LinkedHashSet<>();
            Set<Character> aux = new LinkedHashSet<>();

            partialsList.add(first);
            //adding itself
            first.add(nt);
            aux.add(nt);

            extractingNonTsRec(grammar, nt, partialsList, aux);
        }
        return partials;

    }

    private static void extractingNonTsRec(Grammar grammar, Character nt, List<Set<Character>> partialsList, Set<Character> aux) throws MissingElemException {
        for(Set<Character> set: grammar.getProductionsOfNonterminal(nt)){
            if(set.size()==1 && !aux.contains(new ArrayList<>(set).get(0)) && grammar.getNonTerminals().contains(new ArrayList<>(set).get(0))){
                Set<Character> nextSet = new LinkedHashSet<>(aux);
                nextSet.add(new ArrayList<>(set).get(0));
                aux.add(new ArrayList<>(set).get(0));
                partialsList.add(nextSet);
                extractingNonTsRec(grammar,new ArrayList<>(set).get(0),partialsList,aux);
            }
        }
    }


    @Override
    public String toString() {
        String prodString="";
        for(Character c:nonTerminals){
            try {
                if(!getProductionsOfNonterminal(c).isEmpty()){
                    prodString+=c+":="+getProductionsOfNonterminal(c)+";";
                }
            } catch (MissingElemException e) {
                e.printStackTrace();
            }
        }
        return "Grammar{" + '\n'+
                "nonTerminals: " + nonTerminals + '\n'+
                ", terminals: "+ terminals + '\n'+
                ", productions: " + prodString + '\n'+
//                ", onlyOneEpsilon=" + onlyOneEpsilon + '\n'+
                ", S: " + S + '\n'+
                '}';
    }
}

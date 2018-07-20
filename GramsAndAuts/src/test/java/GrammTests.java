import exceptions.GrammarException;
import exceptions.MissingElemException;
import model.Grammar;

import javax.naming.directory.SearchControls;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Octavian on 11/26/2016.
 */
public class GrammTests {

    public static void main(String[] args) {

//        first();
        second();
    }

    private static void second(){
        Grammar grammar = null;
        try {
            grammar = Grammar.intakeFromFile(Grammar.GRAM_FILE_3);
        } catch (GrammarException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(grammar);

        try {
            System.out.println(Grammar.eliminateSingleProductions(grammar));
        } catch (MissingElemException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(Grammar.obtainEliminatedSingleProdctions(grammar));
        } catch (MissingElemException e) {
            e.printStackTrace();
        }

    }

    private static void first() {
        Grammar grammar = new Grammar();

        grammar.addNonTerminal(new Character('A'));
        grammar.addNonTerminal(new Character('B'));
        grammar.addNonTerminal(new Character('C'));
        grammar.addNonTerminal(new Character('D'));
        grammar.addNonTerminal(new Character('E'));

        grammar.addTerminal(new Character('a'));
        grammar.addTerminal(new Character('A'));
        grammar.addTerminal(new Character('b'));
        grammar.addTerminal(new Character('c'));
        grammar.addTerminal(new Character('d'));
        grammar.addTerminal(new Character('D'));
        grammar.addTerminal(new Character('e'));
        grammar.addTerminal(new Character('$'));

        Set<Character> waySet = new LinkedHashSet<>();
        waySet.add(new Character('a'));
        waySet.add(new Character('D'));

        Set<Character> waySet1 = new LinkedHashSet<>();
        waySet1.add(new Character('e'));
        waySet1.add(new Character('A'));

        Set<Character> errorInFinal = new LinkedHashSet<>();
        errorInFinal.add(new Character('$'));

        try {
            grammar.addProduction(new Character('A'),waySet);
            grammar.addProduction(new Character('A'),waySet1);
            grammar.addProduction(new Character('B'),errorInFinal);

        } catch (GrammarException e) {
            if(e instanceof MissingElemException){
                System.out.printf("The chosen non-terminal " +
                        "%s does not exist in the grammar. \n",e.getMessage());
            }
//            e.printStackTrace();
        }

        System.out.println(grammar.getProductions());

        try {
            System.out.println(grammar.getProductionsOfNonterminal(new Character('A')));
            System.out.println(grammar.getProductionsOfNonterminal(new Character('B')));
            System.out.println(grammar.isStart('c'));
        } catch (MissingElemException e) {
            System.out.println("The chosen nonterminal "+e.getMessage()+" does not exist in this grammmar.");
        }

        System.out.println(grammar.isRightLinear());


        try {
            System.out.println("Passes epsilon: "+grammar.passEpsilon());
            System.out.println("Is regular: "+grammar.isRegular());
        } catch (MissingElemException e) {
            e.printStackTrace();
        }

        System.out.println(grammar.toString());
        System.out.println("NonTerminals: ");

//        try {
//            System.out.println(grammar.toAutomaton());
//        } catch (GrammarException e) {
//            e.printStackTrace();
//        }

        try {
            System.out.println(Grammar.intakeFromFile(Grammar.GRAM_FILE_1));
        } catch (GrammarException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

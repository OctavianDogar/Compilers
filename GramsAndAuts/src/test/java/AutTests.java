import exceptions.GrammarException;
import exceptions.MissingElemException;
import model.Automaton;
import model.Grammar;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Octavian on 11/27/2016.
 */
public class AutTests {

    public static void main(String[] args) {

        Automaton automaton = new Automaton();

        automaton.addState(new Character('A'));
        automaton.addState(new Character('B'));
        automaton.addState(new Character('C'));
        automaton.addState(new Character('D'));
        automaton.addState(new Character('E'));

        automaton.addToAlphabet(new Character('a'));
        automaton.addToAlphabet(new Character('A'));
        automaton.addToAlphabet(new Character('b'));
        automaton.addToAlphabet(new Character('c'));
        automaton.addToAlphabet(new Character('d'));
        automaton.addToAlphabet(new Character('D'));
        automaton.addToAlphabet(new Character('e'));
        automaton.addToAlphabet(new Character('$'));

        Set<Character> waySet = new LinkedHashSet<>();
        waySet.add(new Character('a'));
        waySet.add(new Character('D'));

        Set<Character> waySet1 = new LinkedHashSet<>();
        waySet1.add(new Character('e'));
        waySet1.add(new Character('A'));

        Set<Character> errorInFinal = new LinkedHashSet<>();
        errorInFinal.add(new Character('$'));

        try {
            automaton.addTransition(new Character('A'),waySet);
            automaton.addTransition(new Character('A'),waySet1);
            automaton.addTransition(new Character('B'),errorInFinal);
            automaton.addTransition(new Character('B'),waySet);
        } catch (GrammarException e) {
            if(e instanceof MissingElemException){
                System.out.printf("The chosen non-terminal " +
                        "%s does not exist in the grammar. \n",e.getMessage());
            }
        }

        System.out.println(automaton.getTransitions());

        try {
            System.out.println(automaton.getTransitionsOfState(new Character('A')));
            System.out.println(automaton.getTransitionsOfState(new Character('B')));
        } catch (MissingElemException e) {
            System.out.println("The chosen nonterminal "+e.getMessage()+" does not exist in this grammmar.");
        }

        System.out.println(automaton.toString());

//        try {
//            System.out.println(automaton.toGrammar());
//        } catch (GrammarException e) {
//            e.printStackTrace();
//        }
    
        try {
            System.out.println("Bine boss");
            Grammar g = Grammar.intakeFromFile(Grammar.GRAM_FILE_1);
            System.out.println(g);
            System.out.println(Automaton.intakeFromFile(Automaton.AUT_FILE_1));
        } catch (GrammarException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

package model;

import exceptions.GrammarException;
import exceptions.MissingElemException;
import lombok.Getter;
import lombok.Setter;


import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Octavian on 1/14/2017.
 */

@Getter
@Setter
public class Analyzer {
    private Map<Character,Set<Character>> first;
    private Map<Character,Set<Character>> follow;
    private Map<Pair<Character,Character>,Pair<Set<Character>,Integer>> parsingTable;
    private Grammar grammar;
    private Map<Pair<Character,Set<Character>>,Integer> productionNumber;

    public Analyzer(Grammar grammar) throws MissingElemException {
        this.first = new LinkedHashMap<>();
        this.follow = new LinkedHashMap<>();
        this.parsingTable = new LinkedHashMap<>();
        this.grammar = grammar;
        this.productionNumber = new LinkedHashMap<>();
        setUp();
    }

    public void setUp() throws MissingElemException {
        for (Character nonterminal:grammar.getNonTerminals()){
            first.put(nonterminal,new LinkedHashSet<>());
            follow.put(nonterminal, new LinkedHashSet<>());
        }
        Integer prodNumber = 0;
        for (Character nonterminal:grammar.getNonTerminals()){
            List<Set<Character>> productions =  grammar.getProductionsOfNonterminal(nonterminal);
            for (Set<Character> currentProduction : productions){
                prodNumber += 1;
                productionNumber.put(new Pair<>(nonterminal,currentProduction),prodNumber);
            }
        }
    }
    public void computeAllFirsts() throws MissingElemException {
        for (Character nonterminal:grammar.getNonTerminals()){
            computeFirst(nonterminal,nonterminal);
        }

       //TEST
//        List<Set<Character>> productions =  grammar.getProductionsOfNonterminal('B');
//        for (Set<Character> production : productions){
////            System.out.println(production.toString());
////            System.out.println(checkFirstOfProduction(production,'('));
////            System.out.println(checkFirstOfProduction(production,')'));
////            System.out.println(checkFirstOfProduction(production,'a'));
////            System.out.println(checkFirstOfProduction(production,'+'));4
//            System.out.println(production.toString());
//            System.out.println(getProductionNumber(production));
//        }
    }

    private Boolean checkFirstOfProduction(Set<Character> production, Character character){
            Character firstProductionElement = production.iterator().next();

            if(grammar.getTerminals().contains(firstProductionElement)){
                if(character.equals(firstProductionElement)){
                    return true;
                }
                else{
                    return false;
                }

            }
            else{
                if(first.get(firstProductionElement).contains(character)){
                    return true;
                }
                else{
                    return false;
                }
            }

    }

    private void computeFirst(Character initialCharacter, Character currentCharacter) throws MissingElemException {
        List<Set<Character>> productions =  grammar.getProductionsOfNonterminal(currentCharacter);
        for (Set<Character> production : productions){
            //Check if the first element in a production is a terminal
            Character firstProductionElement = production.iterator().next();

            if(grammar.getTerminals().contains(firstProductionElement)){
                first.get(initialCharacter).add(firstProductionElement);
                continue;
            }
            else{
                computeFirst(initialCharacter,firstProductionElement);
            }
        }
    }

    public void computeAllFollows() throws GrammarException {
        if(first.isEmpty()){
            computeAllFirsts();
        }
        for(Character nonTerminal: grammar.getNonTerminals()){
            computeFollow(nonTerminal);
        }
    }

    /*
    currentCharacter is the searchedFor char
     */
    private void computeFollow(Character searchedForCharacter) throws GrammarException {
        Set<Character> nonTerminals = grammar.getNonTerminals();

        /*
        iterate over all nonTerminals
         */
        for(Character iteratedNonTerminal: nonTerminals){
            List<Set<Character>> productions =  grammar.getProductionsOfNonterminal(iteratedNonTerminal);

            /*
            iterate over all productions of currently iterated nonTerminal
             */
            for (Set<Character> production : productions){
                /*
                grabbing the production's first elem
                */
                List<Character> productionArray = new ArrayList<>(production);
                for(Character currentProductionElement: productionArray){

                    if(currentProductionElement.equals(searchedForCharacter) && ((productionArray.indexOf(currentProductionElement)+1)!= productionArray.size())){
                    /*
                    picking the element following the one searchedFor
                    */
                        Character followingElement = productionArray.get(productionArray.indexOf(currentProductionElement)+1);

                        if(grammar.getTerminals().contains(followingElement)){
                            /*
                            ensure we have the required sets
                             */
                            if(searchedForCharacter.equals('S')){
                                follow.get(searchedForCharacter).add('$');
                            }

                            if (followingElement.equals('$')) {
                                follow.get(searchedForCharacter).addAll(follow.get(iteratedNonTerminal));
                            } else {
                                follow.get(searchedForCharacter).add(followingElement);
                            }
                         /*
                        further on means the followingElemnent is nonTerminal
                        */
                        }else if(first.get(followingElement).contains('$')){
                             /*
                            in first IF branch we need the follow of the iterated nonTerminal for the follow
                            of the searchedForNonTerminal
                             */
                            follow.get(searchedForCharacter).addAll(follow.get(iteratedNonTerminal));


                            Set<Character> firstWithoutEps = new LinkedHashSet<>(first.get(followingElement));
                            firstWithoutEps.remove((Character)'$');
                            follow.get(searchedForCharacter).addAll(first.get(followingElement));
                            /*
                            adding without the epsilon
                             */
                            follow.get(searchedForCharacter).addAll(firstWithoutEps);

                        }else{
                           /*
                            else if epsilon isn't contained in the following element's FIRST
                            or equal to following element's FIRST
                             */
                            follow.get(searchedForCharacter).addAll(first.get(followingElement));
                        }

                    }else if(currentProductionElement.equals(searchedForCharacter) && ((productionArray.indexOf(currentProductionElement)+1)== productionArray.size())){
                        /*
                        the case in which no element follows the searchedFor element
                         */

                        follow.get(searchedForCharacter).addAll(follow.get(iteratedNonTerminal));
                    }

                }

            }
        }
    }

    public Integer getProductionNumber(Character character, Set<Character> production) throws MissingElemException {
        Pair<Character,Set<Character>> apair = new Pair<>(character,production);
        for (Pair<Character,Set<Character>> newPair:
             productionNumber.keySet()) {
            if(apair.equals(newPair)){
                return productionNumber.get(newPair);
            }
        }
        return 0;
    }

    public void fillParsingTable() throws MissingElemException {
//        first.clear();
//        follow.clear();
//        parsingTable.clear();

        List<Character> line = new ArrayList<>();
        line.addAll(grammar.getNonTerminals());
        List<Character> column = new ArrayList<>();
        column.addAll(grammar.getTerminals());

        for(Character lineCharacter:line){
            List<Set<Character>> productions =  grammar.getProductionsOfNonterminal(lineCharacter);
            for (Set<Character> production : productions){
                for(Character columnCharacter:column){
                    if(checkFirstOfProduction(production,columnCharacter)){

                        parsingTable.put(new Pair<>(lineCharacter,columnCharacter),new Pair<>(production,getProductionNumber(lineCharacter,production)));
                    }
                }
                if(first.get(lineCharacter).contains('$') && production.contains('$')){
                    for (Character character:
                            follow.get(lineCharacter)) {
                        Pair<Character,Character> aPair = new Pair<>(lineCharacter, character);
                        for (Pair existingPair:getParsingTable().keySet()){
                            if(aPair.equals(existingPair)){
                                aPair = existingPair;
                            }
                        }
                        if(!getParsingTable().keySet().contains(aPair)){
                            parsingTable.put(new Pair<>(lineCharacter,character), new Pair<>(production,getProductionNumber(lineCharacter,production)));

                        }
                    }
                }

            }


        }
    }


}

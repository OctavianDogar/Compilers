package view;

import exceptions.GrammarException;
import exceptions.MissingElemException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Automaton;
import model.Grammar;
import org.apache.commons.io.FileUtils;
import model.Analyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import static model.Automaton.AUT_FILE_2;
import static model.Grammar.GRAM_FILE_2;
import static org.apache.commons.io.FileUtils.readLines;
import static org.apache.commons.io.FileUtils.writeStringToFile;

/**
 * Created by Octavian on 10/30/2016.
 */
public class Controller {

    /*
    Grammar related elements
     */
    @FXML
    Label errLbl;
    @FXML
    TextArea gramTextArea;
    @FXML
    TextField gramNonTermTF;
    @FXML
    TextField gramTermTF;
    @FXML
    TextField gramProdTF;
    @FXML
    TextField gramNonTermToProcTF;

    /*
    Automaton related elements
     */
    @FXML
    Label errLbl2;
    @FXML
    TextArea autTextArea;
    @FXML
    TextField autStatesTF;
    @FXML
    TextField autAlphabTF;
    @FXML
    TextField autTransTF;
    @FXML
    TextField autStateToTransTF;


    private Grammar currentGram;
    private Automaton currentAut;

    @FXML
    public void eliminateSingleProductions(){
        gramTextArea.appendText('\n'+"");
        gramTextArea.appendText('\n'+"PARTIAL AND FINAL NONTERMINAL CONSTURCTS:"+'\n');
        try {
            gramTextArea.appendText(Grammar.eliminateSingleProductions(currentGram).toString());
            gramTextArea.appendText('\n'+"");
            gramTextArea.appendText('\n'+"FINAL GRAMMAR FORM:");
            gramTextArea.appendText('\n'+"N'="+currentGram.getNonTerminals());
            gramTextArea.appendText('\n'+"E'="+currentGram.getTerminals());
            gramTextArea.appendText('\n'+"P'="+Grammar.obtainEliminatedSingleProdctions(currentGram).toString());
            gramTextArea.appendText('\n'+"S'="+currentGram.getS());

        } catch (GrammarException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadGramFromFile(){
        try {
            currentGram =Grammar.intakeFromFile(Grammar.GRAM_FILE_1);
            gramTextArea.setText(currentGram.toString());
        } catch (GrammarException e) {
            errLbl.setText(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadAutFromFile(){
        try {
            currentAut =Automaton.intakeFromFile(Automaton.AUT_FILE_1);
            autTextArea.setText(currentAut.toString());
        } catch (GrammarException e) {
            errLbl2.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void findTransitions(){
        Character c = autStateToTransTF.getText().charAt(0);
        String transString="";
        try {
            for(Set<Character> set: currentAut.getTransitionsOfState(c)){
                List<Character> aux = new ArrayList<>(set);
                if(aux.size()==1){
                    transString+="("+c+")"+"->"+aux.get(0)+";";
                }else{
                    transString+="("+c+aux.get(0)+")"+"->"+aux.get(1)+";";
                }
            }
        } catch (MissingElemException e) {
            errLbl2.setText("The chosen state does not exist in the current automaton.");
            e.printStackTrace();
        }
        autTextArea.setText(transString);
    }

    @FXML
    public void convertAutToGram(){
        try {
            autTextArea.setText(currentAut.toGrammar().toString());
        } catch (GrammarException e) {
            errLbl2.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void refreshAut(){
        autTextArea.setText(currentAut.toString());
    }

    @FXML
    public void insertAut(){
        File aut2 = new File(AUT_FILE_2);
        String states = autStatesTF.getText();
        String alphabet = autAlphabTF.getText();
        String transitions = autTransTF.getText();
        List<String> transitionsList = new ArrayList<>();

        StringTokenizer st = new StringTokenizer(transitions,";",false);
        while(st.hasMoreTokens()){
            transitionsList.add(st.nextToken());
        }

        try {
            writeStringToFile(aut2,"");
            writeStringToFile(aut2, "#startStates"+'\n',true);
            writeStringToFile(aut2, states+'\n',true);
            writeStringToFile(aut2, "#endStates"+'\n',true);
            writeStringToFile(aut2, alphabet+'\n',true);
            writeStringToFile(aut2, "#endAlphabet"+'\n',true);
            transitionsList.stream().forEach(
                    trn -> {
                        try {
                            writeStringToFile(aut2, trn+ '\n', true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            writeStringToFile(aut2, "#endTransitions"+'\n',true);

        } catch (IOException e) {
            errLbl.setText(e.getMessage());
            e.printStackTrace();
        }

        try {
            currentAut = Automaton.intakeFromFile(AUT_FILE_2);
            autTextArea.setText(currentAut.toString());
        } catch (GrammarException e) {
            errLbl2.setText(e.getMessage());
            e.printStackTrace();
        }
    }



    @FXML
    public void insertGram(){

        File gram2 = new File(GRAM_FILE_2);
        String nonTerminals = gramNonTermTF.getText();
        String terminals = gramTermTF.getText();
        String productions = gramProdTF.getText();
        List<String> productionsList = new ArrayList<>();

        StringTokenizer st = new StringTokenizer(productions,";",false);
        while(st.hasMoreTokens()){
            productionsList.add(st.nextToken());
        }

        try {
            writeStringToFile(gram2,"");
            writeStringToFile(gram2, "#startNonTerminals"+'\n',true);
            writeStringToFile(gram2, nonTerminals+'\n',true);
            writeStringToFile(gram2, "#endNonTerminals"+'\n',true);
            writeStringToFile(gram2, terminals+'\n',true);
            writeStringToFile(gram2, "#endTerminals"+'\n',true);
            productionsList.stream().forEach(
                    prod -> {
                        try {
                            writeStringToFile(gram2, prod + '\n', true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            writeStringToFile(gram2, "#endProductions"+'\n',true);

        } catch (IOException e) {
            errLbl.setText(e.getMessage());
        }

        try {
            currentGram = Grammar.intakeFromFile(GRAM_FILE_2);
            gramTextArea.setText(currentGram.toString());

        } catch (GrammarException e) {
            errLbl.setText(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void convertGramToAut(){
        try {
            gramTextArea.setText(currentGram.toAutomaton().toString());
        } catch (GrammarException e) {
            errLbl.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void findProductions(){
        Character nt = new Character(gramNonTermToProcTF.getText().charAt(0));
        try {
            gramTextArea.setText(currentGram.getProductionsOfNonterminal(nt).toString());
        } catch (GrammarException e) {
            if(e instanceof MissingElemException){
                errLbl.setText("The chosen non-terminal "+e.getMessage()+" does not exist in the current grammar.");
            }else{
                errLbl.setText(e.getMessage());
            }
            e.printStackTrace();
        }
    }

    @FXML
    public void refreshGram(){
        gramTextArea.setText(currentGram.toString());
    }





}

package ADT;

import controller.Parser;
import org.apache.commons.io.FileUtils;
import util.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Octavian on 10/29/2016.
 */
public class Test1Parse {

    public static void main(String[]args){

        String content="";
        try {
            content = FileUtils.readFileToString(new File("./src/main/resources/Test1.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        List<String> tokens = new ArrayList<>();
//
//        StringTokenizer st = new StringTokenizer(content,".,;:{}?[]()' !+-*/<=>",true);
//
//        while(st.hasMoreTokens()){
//            tokens.add(st.nextToken());
//        }
//        tokens.stream().forEach(System.out::println);


        Parser parser = new Parser(Parser.inputFile);

        try {
            parser.parse();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(parser.toString());

    }

}

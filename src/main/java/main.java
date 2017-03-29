import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.operator.IOContainer;
import myLibrary.parser;
import myLibrary.resultCsv;
import org.codehaus.jackson.JsonNode;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class main {
    public static void main(String[] args)  {

        //MAIN
        List<List> result = new LinkedList<List>();

        //create parse
        String fileName = "input.json";
        parser p = new parser();
        p.create(result, fileName);

        //create CSV
        resultCsv csv = new resultCsv(result);
        csv.countOfFeatures();
        csv.create();


        //TEST RAPIDMINER
       /* try {
            RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
            RapidMiner.init();

            Process pr = RapidMiner.readProcessFile(new File("SP-Stritesky.rmp"));
            IOContainer input = new IOContainer();
            IOContainer output = pr.run(input);
            System.out.println(output);
        }catch ( Exception ex) {
            System.out.println(ex);
        }*/

       //generate attributes from one link


    }
}
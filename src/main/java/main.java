import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.operator.IOContainer;
import myLibrary.parser;
import myLibrary.resultCsv;
import org.codehaus.jackson.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class main {
    public static void main(String[] args)  {

        //MAIN
      /*  List<List> result = new LinkedList<List>();

        //create parse
        String fileName = "input.json";
        parser p = new parser();
        p.create(result, fileName);

        //create CSV
        resultCsv csv = new resultCsv(result);
        csv.countOfFeatures();
        csv.create();*/


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
        try {
            String result = "1,";


//            Document doc = Jsoup.connect("https://blog.docker.com/").get();
            Document doc = Jsoup.connect("https://www.quora.com/How-can-I-install-OpenCV3-on-MacOs-Sierra-10-12").get();
            System.out.println(doc);
            parser parser = new parser();
//            List<String> features = parser.generateAttributeTest(doc, "div.entry-summary.shareleft p");
//            List<String> features = parser.generateAttributeTest(doc, "li.menu-docker-for-mac a");
//            List<String> features = parser.generateAttributeTest(doc, "div.ExpandedAnswer span p"); // positive
            List<String> features = parser.generateAttributeTest(doc, "span.TopicNameSpan"); // negative
            System.out.println(features.toString());

            ObjectMapper mapper = new ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode allFeatures = mapper.readTree(new File("attributes.json"));

            int count = 0;
            for (com.fasterxml.jackson.databind.JsonNode allFeature:allFeatures) {
                String item = allFeature.toString();
                item = item.substring(1,item.length()-1);
                if (features.contains(item)) {
                    result += "1,";
                } else {
                    result += "0,";
                }
                count ++;
            }
            result = result.substring(0,result.length()-1);
            System.out.println("pocet: " + count);
            try {
                //Whatever the file path is.
                File statText = new File("test.csv");
                FileOutputStream is = new FileOutputStream(statText);
                OutputStreamWriter osw = new OutputStreamWriter(is);
                Writer w = new BufferedWriter(osw);
                w.write(result);
                w.close();
            } catch (IOException e) {
                System.err.println("Problem writing to the file statsTest.txt");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
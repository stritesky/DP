package GenerateAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapidminer.gui.new_plotter.utility.ListUtility;
import myLibrary.parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by radek on 3.4.17.
 */
public class GenerateAttributes {
    private Elements allElements;
    private List<List> allFeaturesOfLinkList;

    public Elements getAllElements () {
        return this.allElements;
    }

    public void findElements (String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
//        this.allElements = doc.select("div.wiswig > p:first-of-type, div.comment_text > p:first-of-type");
        this.allElements = doc.select("div.wiswig > p");
//        this.allElements = doc.select("div.comment_text > p:first-of-type");
        System.out.println("count of elements:" + allElements.size());
        System.out.println(allElements.toString());
    }

    public void generateAttributes (){
        parser parser = new parser();
        this.allFeaturesOfLinkList = parser.generateAttributesFromElements(this.allElements);
    }

    public void generateCSV () throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        com.fasterxml.jackson.databind.JsonNode allFeaturesJson = mapper.readTree(new File("attributes.json"));
        List<String> allFeatures = new ArrayList<String>();


        for (JsonNode item:allFeaturesJson) {
            allFeatures.add(item.textValue());
        }


        String result = "";


        int count = 0;
        for (List<String> allFeatureOfLinkList: allFeaturesOfLinkList) {

            count++;
            System.out.println(count);
            if (count == 2) break;
            result += "1,";
            for (String allFeature:allFeatures) {
                if (allFeatureOfLinkList.contains(allFeature)) {
                    result += "1,";
                } else {
                    result += "0,";
                }
            }
            result = result.substring(0,result.length() - 1 );
            result += "\n";
        }

        try {
            File statText = new File("test.csv");
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(result);
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }

    }
}

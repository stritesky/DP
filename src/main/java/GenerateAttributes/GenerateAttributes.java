package GenerateAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapidminer.gui.new_plotter.utility.ListUtility;
import myLibrary.parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        this.allElements = doc.select("p, div, span");
        ArrayList <Integer> removex = new ArrayList<Integer>();
        for (Integer i = 0; i < allElements.size();i++) {
            if (allElements.get(i).getAllElements().size() > 2 ||  allElements.get(i).ownText().length() < 10) {
                System.out.println(allElements.get(i).ownText());
                removex.add(i);
            }
        }

        Collections.sort(removex,Collections.reverseOrder());
        for (int j = 0; j< removex.size(); j++) {
            this.allElements.remove((int)removex.get(j));
        }

        System.out.println("count of elements:" + allElements.size());
//        System.out.println(allElements.toString());

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
        FileWriter fileWriter = null;

        try {
           /* File statText = new File("test.csv");
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(result);
            w.close();*/
            String fileName = "test.csv";
            fileWriter = new FileWriter(fileName);

            fileWriter.append(result);

            int count = 0;
            for (List<String> allFeatureOfLinkList: allFeaturesOfLinkList) {
                boolean first = true;
                count++;
                System.out.println(count);
//                if (count == 50) break;


                for (String allFeature:allFeatures) {
                    if (first) {
                        fileWriter.append("1");
                        fileWriter.append(",");
                    } else {
                        fileWriter.append(",");
                    }
                    if (allFeatureOfLinkList.contains(allFeature)) {
                        fileWriter.append("1");
                    } else {
                        fileWriter.append("0");
                    }
                    first = false;
                }
                fileWriter.append("\n");
            }

        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }

    }
}

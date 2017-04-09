package myLibrary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by radek on 6.11.16.
 */
public class resultCsv {

    private List<String> featuresAll = new ArrayList<String>(); //features all without label
    private List<List> featuresCsv = new LinkedList<List>();

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    public resultCsv(List<List> result) {

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode attributes = mapper.createArrayNode();


        List<FeatureObject> allFeatures = GenerateAllFeatures.createAllFeature(result);
/*
        for (FeatureObject item :allFeatures) {
            if (item.getCount() > 0) {
                featuresAll.add(item.getName());
                attributes.add(item.getName());
            }
        }*/
        for (List item : result) {
            for (Object i:item){
                if (!featuresAll.contains(i) && !i.equals(parser.LABEL_POSITIVE) && !i.equals(parser.LABEL_NEGATIVE)){
                    featuresAll.add((String) i);
                    attributes.add((String) i);
                }
            }
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("attributes.json"),attributes);
        } catch (IOException e){
            e.printStackTrace();
        }

        for (List resultItem : result){
            List<String> row = new LinkedList<String>();
            row.add((resultItem.get(0) == parser.LABEL_POSITIVE)? "1" : "0"); //label positive or negative
            for (Object featuresAllItem : featuresAll) {
                row.add(resultItem.contains(featuresAllItem)? "1" : "0");
            }
            featuresCsv.add(row);
        }
    }


    public void countOfFeatures (){

        System.out.println("Count of features: " + featuresAll.size());
    }

    public void create () {
        System.out.println("create CSV");
//        System.out.println(featuresAll);
        String fileName = "result.csv";
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file
            for (List row : featuresCsv) {
                boolean first = true;
                for (Object element : row) {
                    if (!first) {
                        fileWriter.append(COMMA_DELIMITER);
                    }
                    fileWriter.append(String.valueOf(element));
                    first = false;
                }
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
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

package resultFile;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by radek on 6.11.16.
 */
public class resultCsv {

    private List<Object> featuresAll = new LinkedList<>();
    private List<List> featuresCsv = new LinkedList<>();

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    public resultCsv(List<List> result) {

        for (List item : result) {
            for (Object i:item){
                if (!featuresAll.contains(i)){
                    featuresAll.add(i);
                }
            }
        }

        for (List resultItem : result){
            List<String> row = new LinkedList<>();

            for (Object featuresAllItem : featuresAll) {
                row.add(resultItem.contains(featuresAllItem)? "1" : "0");
            }
            featuresCsv.add(row);
        }
    }
    public void countOfFeatures (){

        System.out.println("Count of featute: " + featuresAll.size());
    }

    public void create () {
        System.out.println(featuresAll);
        String fileName = "test.csv";
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append("label");
            for (Object item: featuresAll) {
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(item));
            }
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write the CSV file
            for (List row : featuresCsv) {
                fileWriter.append("1");
                for (Object element : row) {
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(String.valueOf(element));
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

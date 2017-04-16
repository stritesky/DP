package featuregen;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by radek on 15.4.17.
 */
public class ApplyFuture {

    public static final String LABEL_POSITIVE = "label_positive";
    public static final String LABEL_NEGATIVE = "label_negative";
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    private List<IFeatureGenerator> featureGenerators = new ArrayList<IFeatureGenerator>();
    private List<String> featuresAll = new ArrayList<String>(); //features all without label
    private ObjectMapper objectMapper = new ObjectMapper();


    private WebDriver driver = null;
    private List<List> listOfAllGenerateFeatures = new ArrayList();


    public ApplyFuture() {
        featureGenerators.add(new ParentTagNameGenerator());
        featureGenerators.add(new ParentAttributeGenerator());
        featureGenerators.add(new ParentSiblingsTagNameGenerator());
        featureGenerators.add(new ParentSiblingsAttributeGenerator());
    }


    //TRAIN DATABASE
    public void generateTrainDatabase(String pathToInputFile) throws IOException {
        generateFeatureFromInputFile(pathToInputFile);
        createFuturesAll();
        createCSVFileTrainDatabase();
    }

    //Apply model to elements

    public Elements applyModelToLink (String link) throws IOException {
        Elements findElements = findElements(link);
        List<List> ListOfFeatures = generateAttributesFromElements(findElements);
        createCSVFileForExecute(ListOfFeatures);
        return findElements;
    }

    private Elements findElements (String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements allElements = doc.select("p, div, span"); //select
        ArrayList <Integer> removex = new ArrayList<Integer>();
        for (Integer i = 0; i < allElements.size(); i++) { //fdf
            if (allElements.get(i).getAllElements().size() > 2 ||  allElements.get(i).ownText().length() < 10) {
                System.out.println(allElements.get(i).ownText());
                removex.add(i);
            }
        }

        Collections.sort(removex,Collections.reverseOrder());
        for (int j = 0; j < removex.size(); j++) {
            allElements.remove((int)removex.get(j));
        }

        System.out.println("count of elements:" + allElements.size());

        return allElements;
    }

    private void generateFeatureFromInputFile(String pathToInputFile) throws IOException {
        System.setProperty("webdriver.phantomjs.driver","/usr/bin/phantomjs");
        driver = new PhantomJSDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);


        JsonNode inputFile = this.objectMapper.readTree(new File(pathToInputFile));
        JsonNode links = inputFile.get("links");

        //Generate features
        for (JsonNode link: links) {
            Document doc;
            System.out.println(link.get("link").asText());
            if (link.get("selenium").asText().equals("true")) {
                System.out.println("selenium");
                driver.get(link.get("link").asText());
                doc = Jsoup.parse(driver.getPageSource());
            } else {
                doc = Jsoup.connect(link.get("link").asText()).get();
            }
            generateAttribute(doc, link.get("positive"), this.listOfAllGenerateFeatures, LABEL_POSITIVE);
            generateAttribute(doc, link.get("negative"), this.listOfAllGenerateFeatures, LABEL_NEGATIVE);
        }
        driver.quit();
    }

    private void createFuturesAll () throws IOException {
        ArrayNode attributes = this.objectMapper.createArrayNode();

        for (List item : listOfAllGenerateFeatures) {
            for (Object i:item){
                if (!this.featuresAll.contains(i) && !i.equals(LABEL_POSITIVE) && !i.equals(LABEL_NEGATIVE)){
                    this.featuresAll.add((String) i);
                    attributes.add((String) i);
                }
            }
        }
        this.objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("attributes.json"),attributes);
    }

    private void createCSVFileTrainDatabase() {

        StringBuffer csvContent = new StringBuffer();
        for (List resultItem : this.listOfAllGenerateFeatures){
            csvContent.append((resultItem.get(0) == LABEL_POSITIVE)? "1" : "0"); //label positive or negative
            csvContent.append(COMMA_DELIMITER);
            for (Object featuresAllItem : this.featuresAll) {
                csvContent.append(resultItem.contains(featuresAllItem)? "1" : "0");
                csvContent.append(COMMA_DELIMITER);
            }
            csvContent.deleteCharAt(csvContent.length()-1);
            csvContent.append(NEW_LINE_SEPARATOR);
        }
        writeContentToFile(csvContent, "result.csv");
    }

    private void createCSVFileForExecute(List<List> listOgFeatures) throws IOException {
        JsonNode allFeaturesJson = this.objectMapper.readTree(new File("attributes.json"));
        List<String> allFeatures = new ArrayList<String>();
        StringBuffer csvContent = new StringBuffer();


        for (JsonNode item:allFeaturesJson) {
            allFeatures.add(item.textValue());
        }

        for (List<String> allFeatureOfLinkList: listOgFeatures) {
            boolean first = true;
            for (String allFeature:allFeatures) {
                if (first) {
                    csvContent.append("1");
                    csvContent.append(COMMA_DELIMITER);
                }
                if (allFeatureOfLinkList.contains(allFeature)) {
                    csvContent.append("1");
                } else {
                    csvContent.append("0");
                }
                csvContent.append(COMMA_DELIMITER);
                first = false;
            }
            csvContent.deleteCharAt(csvContent.length() - 1);
            csvContent.append(NEW_LINE_SEPARATOR);
        }

        writeContentToFile(csvContent, "test.csv" );
    }

    public void writeContentToFile (StringBuffer content, String fileName) {
        BufferedWriter bwr = null;

        try {
            bwr = new BufferedWriter(new FileWriter(new File(fileName)));
            bwr.write(content.toString());
            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                bwr.flush();
                bwr.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }


    private void generateAttribute (Document doc,JsonNode selectors, List<List> result, String label) {

        for (JsonNode selector: selectors) {
            Elements elements = doc.select(selector.asText());

            for (Element element:elements) {
                List<String> features = new ArrayList<String>();
                features.add(label);
                for (IFeatureGenerator gen : featureGenerators) {
                    gen.createFeatures(features, element);
                }
                if (!result.contains(features)){
                    System.out.println("LABEL: " + label + "select: " + selector +  "element:  " + element);
                    result.add(features);
                }
            }
        }
    }

    private List<List> generateAttributesFromElements (Elements elements) {

        List<List> listFeatures = new ArrayList<List>();
        List<String> features ;
//        ArrayNode elements = this.objectMapper.cr

        for (Element element :elements) {
            features = new ArrayList<String>();
            for (IFeatureGenerator gen : featureGenerators) {
                gen.createFeatures(features, element);
            }
            listFeatures.add(features);
        }
        return listFeatures;
    }

}

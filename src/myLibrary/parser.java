package myLibrary;

import featuregen.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by radek on 7.12.16.
 */
public class parser {

    public static final String LABEL_POSITIVE = "label_positive";
    public static final String LABEL_NEGATIVE = "label_negative";

    private WebDriver driver = new PhantomJSDriver();
    private List<IFeatureGenerator> featuregen = new LinkedList<>();

    public parser() {
        System.setProperty("webdriver.phantomjs.driver","/usr/bin/phantomjs");
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        //add feature generator
        featuregen.add(new ParentTagNameGenerator());
        featuregen.add(new ParentAttributeGenerator());
        featuregen.add(new ParentSiblingsTagNameGenerator());
        featuregen.add(new ParentSiblingsAttributeGenerator());
    }

    public void create(List<List> result){

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("input.json"));

            JSONObject jsonObject =  (JSONObject) obj;

            JSONArray links = (JSONArray) jsonObject.get("links");

            // loop array
            Iterator<JSONObject> iterator = links.iterator();
            while (iterator.hasNext()) {
                JSONObject link = iterator.next();
                System.out.println(link.get("link"));


                try {
                Document doc;
                if ((boolean)link.get("selenium")) {
                    driver.get((String) link.get("link"));
                    doc = Jsoup.parse(driver.getPageSource());
                } else {
                    doc = Jsoup.connect((String) link.get("link")).get();
                }
                    System.out.println(link.get("positive"));
                generateAttribute(doc, (String) link.get("positive"), result, LABEL_POSITIVE);
                generateAttribute(doc, (String) link.get("negative"), result, LABEL_NEGATIVE);

                }catch (Exception e) {
                    System.out.println("io - "+e);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void generateAttribute (Document doc,String selector, List<List> result, String label) {
        System.out.println();
        System.out.println(label);
        Elements elements = doc.select(selector);

//                System.out.println(elements);
        for (Element element:elements) {
            List<String> features = new LinkedList<>();
                    System.out.println(element.text());
            features.add(label);
            for (IFeatureGenerator gen : featuregen) {
                gen.createFeatures(features, element);
            }
            if (!result.contains(features)){
                result.add(features);
            }
            System.out.println(features);
        }
    }
}

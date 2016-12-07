package myLibrary;

import featuregen.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

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
    private List<String[]> linkURLs = new LinkedList<>();

    public parser(List<String[]> links) {
        System.setProperty("webdriver.phantomjs.driver","/usr/bin/phantomjs");
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        //add feature generator
        featuregen.add(new ParentTagNameGenerator());
        featuregen.add(new ParentAttributeGenerator());
        featuregen.add(new ParentSiblingsTagNameGenerator());
        featuregen.add(new ParentSiblingsAttributeGenerator());

        linkURLs = links;
    }

    public void create(List<List> result){
        for (String[] linkURL : linkURLs) {

            try {
                Document doc;
                if (linkURL[3] == "js") {
                    driver.get(linkURL[0]);
                    doc = Jsoup.parse(driver.getPageSource());
                } else {
                    doc = Jsoup.connect(linkURL[0]).get();
                }
                generateAttribute(doc, linkURL, result, LABEL_POSITIVE);
                generateAttribute(doc, linkURL, result, LABEL_NEGATIVE);

            }catch (Exception e) {
                System.out.println("io - "+e);
            }
        }
    }

    private void generateAttribute (Document doc,String[] link, List<List> result, String label) {
        Elements comments;
        if (label == LABEL_POSITIVE) {
            comments = doc.select(link[1]);
        } else {
            comments = doc.select(link[2]);
        }
//                System.out.println(coments);
        for (Element element:comments) {
            List<String> features = new LinkedList<>();
//                    System.out.println(element.text());
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

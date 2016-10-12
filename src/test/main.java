package test;

import featuregen.IFeatureGenerator;
import featuregen.ParentClassFeatureGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class main {
    public static void main(String[] args) throws IOException {
        List<IFeatureGenerator> featuregen = new LinkedList<>();
        featuregen.add(new ParentClassFeatureGenerator());

        Document doc = Jsoup.connect("https://www.sport.cz/vasenazory/222287").get();
        Elements coments = doc.select("#discussion-block div.report-text div");
        for (Element element:coments
             ) {

            System.out.println(element.text());


            List<String> features = new LinkedList<>();
            for (IFeatureGenerator gen : featuregen) {
                gen.createFeatures(features, element);
            }

            System.out.println(features);

            System.out.println();
        }
    }
}
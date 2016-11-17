package main;

import resultFile.resultCsv;
import featuregen.IFeatureGenerator;
import featuregen.ParentClassFeatureGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.LinkedList;
import java.util.List;


public class main {
    public static void main(String[] args)  {
        List<String[]> linkURLs = new LinkedList<>();
        List<IFeatureGenerator> featuregen = new LinkedList<>();
        List<List> result = new LinkedList<>();

        //add feature generator
        featuregen.add(new ParentClassFeatureGenerator());
//        featuregen.add(new ParentParentClassFeatureGenerator());

        //add link
        linkURLs.add(new String[]{"https://www.sport.cz/vasenazory/224174","div.report-text div"});
        linkURLs.add(new String[]{"http://zpravy.idnes.cz/diskuse.aspx?iddiskuse=A161106_145250_domaci_bur",".user-text p"});
        linkURLs.add(new String[]{"http://www.auto.cz/dieselgate-cislo-2-100027/diskuse?typ=1&sort=2",".comment-text"});


        for (String[] linkURL : linkURLs) {
            System.out.println(linkURL[0]);

            try {
                Document doc = Jsoup.connect(linkURL[0]).get();
                Elements coments = doc.select(linkURL[1]);
                List<String> features = new LinkedList<>();
//                System.out.println(coments);
                for (Element element:coments) {
//                    System.out.println(element.text());
                    for (IFeatureGenerator gen : featuregen) {
                        gen.createFeatures(features, element);
                    }
//                    System.out.println();
                }
                result.add(features);
                System.out.println(features);
            }catch (Exception e) {
                System.out.println("io - "+e);
            }

        }

        //create CSV
        System.out.println("create CSV");
        resultCsv csv = new resultCsv(result);
        csv.create();
    }
}
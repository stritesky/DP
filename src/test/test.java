package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by radek on 16.11.16.
 */
public class test {

    public static void main(String[] args)  {

        try {
            Document doc = Jsoup.connect("https://www.sport.cz/vasenazory/224145")
                    .get();
            Elements coments = doc.select("#discussion-block div.report-text div");
            for (Element element:coments) {
                int i = 0;
                while ( i <= 10) {
                    element = element.parent();
                    for (String className : element.classNames()) {
                        System.out.println(i);
                        System.out.println(className);
                    }
                i++;
                }
            }
//            System.out.println(coments);
        } catch (Exception e) {
            System.out.println("io - "+e);
        }
    }
}

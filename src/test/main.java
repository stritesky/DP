package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.sport.cz/vasenazory/222118").get();
        Elements coments = doc.select("#discussion-block div.report-text div");
        for (Element coment:coments
             ) {
            System.out.println(coment);
        }
    }
}
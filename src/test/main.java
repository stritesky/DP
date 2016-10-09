package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;


/**
 * Created by radek on 9.10.16.
 */

public class main {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://dameakci.cz").get();
        String title = doc.title();
        System.out.println(title);
    }
}

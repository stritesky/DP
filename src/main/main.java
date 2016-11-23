package main;

import featuregen.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import resultFile.resultCsv;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;



public class main {
    public static void main(String[] args)  {

        System.setProperty("webdriver.phantomjs.driver","/usr/bin/phantomjs");
        WebDriver driver = new PhantomJSDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);


        List<String[]> linkURLs = new LinkedList<>();
        List<IFeatureGenerator> featuregen = new LinkedList<>();
        List<List> result = new LinkedList<>();

        //add feature generator
        featuregen.add(new ParentTagNameGenerator());
        featuregen.add(new ParentAttributeGenerator());
        featuregen.add(new ParentSiblingsTagNameGenerator());
        featuregen.add(new ParentSiblingsAttributeGenerator());

        //add link - NOT work
//        linkURLs.add(new String[]{"http://www.dailymail.co.uk/news/article-3953988/Obama-meet-Australia-Canada-leaders-return-US.html","div.diskuse-text","js"});
//        linkURLs.add(new String[]{"https://www.youtube.com/watch?v=mivBmNP-h-g&t=3034s","div.gig-comment-body","js"});
//        linkURLs.add(new String[]{"http://www.express.co.uk/news/world/735340/Hillary-Clinton-US-President-John-Bonifaz-J-Alex-Halderman-Wisconsin-Pennsylvania-Michigan","div.gig-comment-body","js"});
        //add link working
        linkURLs.add(new String[]{"http://www.foxnews.com/politics/2016/11/20/schumer-hints-compromise-with-trump-but-not-on-obamacare.html","div.fyre-comment p","js"});
        linkURLs.add(new String[]{"http://www.blesk.cz/clanek/celebrity-svetove-celebrity/432315/rozvod-jolie-a-pitta-brad-ma-novou-velmi-slavnou-lasku-uz-spolu-spi-tvrdi-bodyguard.html","div.commentsBubble p","js"});
        linkURLs.add(new String[]{"https://www.sport.cz/vasenazory/224381","div.report-text div","NO"});
        linkURLs.add(new String[]{"http://zpravy.idnes.cz/diskuse.aspx?iddiskuse=A161106_145250_domaci_bur",".user-text p","NO"});
        linkURLs.add(new String[]{"http://www.auto.cz/dieselgate-cislo-2-100027/diskuse?typ=1&sort=2",".comment-text","NO"});
        linkURLs.add(new String[]{"http://www.zive.cz/clanky/6-programatorskych-chyb-ktere-zabijely-a-staly-stovky-milionu-dolaru/sc-3-a-184788/default.aspx?artcomments=1",".forum-text p","NO"});
        linkURLs.add(new String[]{"http://www.denik.cz/diskuse/turecke-dobrodruzstvi.html","div.diskuse-text","js"});
        linkURLs.add(new String[]{"http://mtbs.cz/clanek/v-usa-hori-diskuse-nad-elektrokoly-na-trailech-jsou-nechtena/kategorie/pozvanky#.WDWXr3XhBhF","#comments li p","NO"});
        linkURLs.add(new String[]{"http://www.csfd.cz/film/52430-nocni-zvirata/komentare/","ul.ui-posts-list li p","NO"});
        linkURLs.add(new String[]{"http://stalkerky.cz/aguse-a-jeji-volno/#comments","article.comment-body div p","NO"});
        linkURLs.add(new String[]{"http://9gag.com/gag/aMwjjbR?ref=fsidebar#comment","div.badge-content","js"});
        linkURLs.add(new String[]{"http://tn.nova.cz/clanek/zima-uderi-na-cesko-blizi-se-konec-tepla-i-inverze-predpoved.html","div.com_text","no"});
        linkURLs.add(new String[]{"http://www.lidovky.cz/diskuse.aspx?iddiskuse=A161123_074429_ln_zahranici_ELE","div.contribution table tbody tr td.right p:last-of-type","no"});

        for (String[] linkURL : linkURLs) {
            System.out.println(linkURL[0]);

            try {
                Document doc;
                if (linkURL[2] == "js") {
                    driver.get(linkURL[0]);
                    doc = Jsoup.parse(driver.getPageSource());
                } else {
                    doc = Jsoup.connect(linkURL[0]).get();
                }
//                System.out.println(doc);
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
        csv.countOfFeatures();
        csv.create();
    }

}
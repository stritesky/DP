package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Driver;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Created by radek on 16.11.16.
 */
public class test {


    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.phantomjs.driver","/usr/bin/phantomjs");
        WebDriver driver = new PhantomJSDriver();
        driver.manage().window().maximize();

//        ghostDriver.get("https://www.stream.cz/prague-guide/10012306-co-o-nas-ve-svete-nevedi-ceske-osobnosti-vynalezy-a-pikantnosti#nejnovejsi");
        driver.get("http://www.denik.cz/diskuse/turecke-dobrodruzstvi.html");
        Document doc = Jsoup.parse(driver.getPageSource());
        System.out.println(doc);
        Elements coments = doc.select("div.diskuse-text");
        System.out.println(coments);


//        System.out.println(driver.getPageSource());
//        System.out.println(driver.getTitle());
        driver.close();

//        try {
//            Document doc = Jsoup.connect("https://www.sport.cz/vasenazory/224145")
//                    .get();
//            Elements coments = doc.select("#discussion-block div.report-text div");
//            for (Element element:coments) {
//                int i = 0;
//                while ( i <= 10) {
//                    element = element.parent();
//                    for (String className : element.classNames()) {
//                        System.out.println(i);
//                        System.out.println(className);
//                    }
//                i++;
//                }
//            }
////            System.out.println(coments);
//        } catch (Exception e) {
//            System.out.println("io - "+e);
//        }
    }
}

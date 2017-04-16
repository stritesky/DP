import featuregen.ApplyFuture;
import org.jsoup.select.Elements;
import rapidMiner.ApplyModelResult;

import java.io.*;
import java.util.List;

public class main {
    public static void main(String[] args) throws IOException {



        Elements elements = null;
        ApplyFuture applyFuture = new ApplyFuture();
        try {
//            applyFuture.generateTrainDatabase("newInput.json");
            elements = applyFuture.applyModelToLink("https://www.svetandroida.cz/hodinky-xiaomi-huami-amazfit-recenze-201609#comments");
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            rapidMiner.RapidMiner rapidMiner = new rapidMiner.RapidMiner();
            List<ApplyModelResult> applyModelResults = rapidMiner.run();

            for (int i = 0; i < applyModelResults.size(); i++ ) {

//                if (applyModelResults.get(i).getPrediction().equals("1")) {
                    System.out.println(elements.get(i).toString());
                    System.out.println("item prediction: " + applyModelResults.get(i).getPrediction());
//                }
            }

        }catch ( Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
    }
}
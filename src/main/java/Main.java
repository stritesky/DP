import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import featuregen.ApplyFuture;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rapidMiner.ApplyModelResult;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {

    private static final String MOD_LEARN = "learn";
    private static final Logger LOG = LoggerFactory.getLogger(ApplyFuture.class);

    public static void main(String[] args) throws IOException {
        String mode = args[0];
        if (mode.equals(MOD_LEARN)) {
            ApplyFuture applyFuture = new ApplyFuture();
            try {
                applyFuture.generateTrainDatabase("input.json");
                System.out.println("he program ended successfully! result.csv and attributes.json saved in new folder data-TIMESTAMP");
            } catch (Exception e) {
                System.out.println("Program failed!! Open logFile.log and search for a problem!!!!!!"  );
                LOG.error("Create dataset error with exception: {}", e.getMessage());
            }

        } else {
            Elements elements = null;
            ApplyFuture applyFuture = new ApplyFuture();
            try {
                elements = applyFuture.applyModelToLink(mode);
            } catch (Exception e) {
                LOG.error("applyModelToLink: {}", e.getMessage());
            }

            try {
                rapidMiner.RapidMiner rapidMiner = new rapidMiner.RapidMiner();
                List<ApplyModelResult> applyModelResults = rapidMiner.run();
                ObjectMapper mapper = new ObjectMapper();
                ArrayNode resultCommnets = mapper.createArrayNode();
                for (int i = 0; i < applyModelResults.size(); i++ ) {

                    if (applyModelResults.get(i).getPrediction().equals("1")) {
                       resultCommnets.add(elements.get(i).toString());
                        LOG.debug("prediction: {} potential HTML element:{}", applyModelResults.get(i).getPrediction(), elements.get(i).toString());

                    }
                }

                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                File file = new File("resultComments-" + timeStamp + ".json");
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, resultCommnets);
                LOG.trace("File with comments was created successfully");
                System.out.println("The program ended successfully! The searched comments are stored in the resultComments -TIMESTAMP.json file");
            }  catch ( Exception ex) {
                System.out.println("Program failed!! Open logFile.log and search for a problem!!!!!!"  );
                LOG.error("Classification with Exception: {}", ex.getMessage());
            }
        }
    }
}
package myLibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by radek on 5.4.17.
 */
public class GenerateAllFeatures {

    public static List<FeatureObject> createAllFeature (List<List> result) {
        List<FeatureObject> allFeatures = new ArrayList<FeatureObject>();

        for (List item : result) {
            for (Object i:item) {

                if (!i.equals(parser.LABEL_POSITIVE) || i.equals(parser.LABEL_NEGATIVE)){
                    if (!allFeatures.contains(new FeatureObject((String)i))){
                        allFeatures.add(new FeatureObject((String)i));
                    } else {
                        allFeatures.get(allFeatures.indexOf(new FeatureObject((String)i))).incrementCount();
                    }
                }
            }
        }
        return allFeatures;
    }

}

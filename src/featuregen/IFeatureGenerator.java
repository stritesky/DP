package featuregen;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by komp2 on 12.10.2016.
 */
public interface IFeatureGenerator {

	void createFeatures(List<String> features, Element element);

}

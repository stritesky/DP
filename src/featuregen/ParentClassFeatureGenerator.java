package featuregen;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by komp2 on 12.10.2016.
 */
public class ParentClassFeatureGenerator implements IFeatureGenerator {
	private static final String PREFIX = "parentClass_";

	@Override
	public void createFeatures(List<String> features, Element element) {
		for (String className : element.parent().attr("class").split(" ")) {
			features.add(PREFIX + className);
		}
	}

}

package featuregen;

import org.jsoup.nodes.Element;

import java.util.List;


public class ParentParentClassFeatureGenerator implements IFeatureGenerator {
	private static final String PREFIX = "parentParentClass_";

	@Override
	public void createFeatures(List<String> features, Element element) {
		for (String className : element.parent().parent().attr("class").split(" ")) {
			if (!features.contains(PREFIX + className)) {
				features.add(PREFIX + className);
			}
		}
	}

}

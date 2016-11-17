package featuregen;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by komp2 on 12.10.2016.
 */
public class ParentClassFeatureGenerator implements IFeatureGenerator {
	private static final String PREFIX = "parentClass";

	@Override
	public void createFeatures(List<String> features, Element element) {
		int i = 0;
		while ( i <= 15) {
			if (element.parent() == null) {
				break;
			}
			element = element.parent();
			for (String className : element.classNames()) {
				if (!features.contains(PREFIX + className)) {
					String subPrefix = "Depth-" + i + "_";
					features.add(PREFIX + subPrefix + className);
				}
			}
			i++;
		}
	}

}

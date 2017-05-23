package featuregen;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by komp2 on 12.10.2016.
 */
public class ParentSiblingsTagNameGenerator implements IFeatureGenerator {
	private static final String PREFIX = "parentDepth-";
	private static final int MAX_DEPTH = 10;

	public void createFeatures(List<String> features, Element element) {
		int i = 0;
		while ( i <= MAX_DEPTH) {
			for (Element sibling :element.siblingElements()) {
				String subPrefix = i + "-SiblingTag_";
				if (!features.contains(PREFIX + subPrefix + sibling.tagName())) {
					features.add(PREFIX + subPrefix + sibling.tagName());
				}
			}

			if (element.parent() == null) {
				break;
			}
			element = element.parent();
			i++;
		}
	}
}

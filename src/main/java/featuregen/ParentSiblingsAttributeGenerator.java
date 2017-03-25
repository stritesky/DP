package featuregen;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by komp2 on 12.10.2016.
 */
public class ParentSiblingsAttributeGenerator implements IFeatureGenerator {
	private static final String PREFIX = "parentDepth-";
	private static final int MAX_DEPTH = 30;

	public void createFeatures(List<String> features, Element element) {
		int i = 0;
		while ( i <= MAX_DEPTH) {
			for (Element sibling :element.siblingElements()) {
				for (Attribute att : sibling.attributes()) {
					for (String value :att.getValue().split(" ")) {
						String feature =
								"parentDepth-" + i + "-SiblinkTag" + sibling.tagName() + "Attribut" + att.getKey()  + "_" + value;
						if (!features.contains(feature)) {
							features.add(feature);
						}
					}
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

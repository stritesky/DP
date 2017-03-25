package featuregen;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by komp2 on 12.10.2016.
 */
public class ParentAttributeGenerator implements IFeatureGenerator {
	private static final String PREFIX = "parentAttribute";
	private static final int MAX_DEPTH = 30;

	public void createFeatures(List<String> features, Element element) {
		int i = 0;
		while ( i <= MAX_DEPTH) {
			for (Attribute att : element.attributes()) {
				for (String name :att.getValue().split(" ")) {
					String subPrefix =  att.getKey() + "Depth-" + i + "_";
					if (!features.contains(PREFIX + subPrefix + name)) {
						features.add(PREFIX + subPrefix + name);
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

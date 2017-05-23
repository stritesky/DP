package featuregen;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by komp2 on 12.10.2016.
 */
public class ParentTagNameGenerator implements IFeatureGenerator {
	private static final String PREFIX = "parentTagName-";
	private static final int MAX_DEPTH = 10;

	public void createFeatures(
			List<String> features,
			Element element
	) {
		int i = 0;
		while ( i <= MAX_DEPTH) {
			String tag = element.tagName();
			String subPrefix = i + "_";
			if (!features.contains(PREFIX + subPrefix + tag)) {
				features.add(PREFIX + subPrefix + tag);
			}
			if (element.parent() == null) {
				break;
			}
			element = element.parent();
			i++;
		}
	}
}

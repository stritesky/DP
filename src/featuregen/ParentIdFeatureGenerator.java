package featuregen;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by komp2 on 12.10.2016.
 */
public class ParentIdFeatureGenerator implements IFeatureGenerator {
	private static final String PREFIX = "parentId";

	@Override
	public void createFeatures(List<String> features, Element element) {
		int i = 0;
		while ( i <= 15) {
			if (element.parent() == null) {
				break;
			}
			element = element.parent();
			for (String classId : element.attr("id").split(" ")) {
				String subPrefix = "Depth-" + i + "_";
				if (!features.contains(PREFIX + subPrefix + classId)) {
					features.add(PREFIX + subPrefix + classId);
				}
			}
			i++;
		}
	}
}

package blogr.vpm.fr.blogr.insertion;

import java.util.Map;

/**
 * Created by vince on 16/03/15.
 */
public interface MetadataProvider {

  /**
   * Provides one or multiple mappings for metadata key/value pairs
   * @return metadata key/value mappings
   */
  Map<String, String> getMappings();
}

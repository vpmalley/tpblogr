package blogr.vpm.fr.blogr.picture;

import java.util.HashMap;
import java.util.Map;

import blogr.vpm.fr.blogr.insertion.MetadataProvider;

/**
 * Created by vince on 16/03/15.
 */
public class PictureMetadataProvider implements MetadataProvider {

  private final String picUrl;

  public PictureMetadataProvider(String picUrl) {
    this.picUrl = picUrl;
  }

  @Override
  public Map<String, String> getMappings() {
    Map<String, String> picMapping = new HashMap<>();
    picMapping.put("pic", picUrl);
    return picMapping;
  }
}

package blogr.vpm.fr.blogr.picture;

import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;

/**
 * Created by vince on 24/10/14.
 */
public class PictureTpTagsProvider implements SurroundingTagsProvider {

  private final String pictureUrl;

  public PictureTpTagsProvider(String pictureUrl) {
    this.pictureUrl = pictureUrl;
  }

  @Override
  public String getStartTag() {
    StringBuilder startTagBuilder = new StringBuilder();
    startTagBuilder.append("[img=");
    startTagBuilder.append(pictureUrl);
    startTagBuilder.append(" caption=");
    return startTagBuilder.toString();
  }

  @Override
  public String getEndTag() {
    return " ]";
  }
}

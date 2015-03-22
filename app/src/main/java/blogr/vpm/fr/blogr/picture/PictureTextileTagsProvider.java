package blogr.vpm.fr.blogr.picture;

import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;

/**
 * Created by vincent on 19/10/14.
 */
public class PictureTextileTagsProvider implements SurroundingTagsProvider {

  private final String pictureUrl;

  public PictureTextileTagsProvider(String pictureUrl) {
    this.pictureUrl = pictureUrl;
  }

  @Override
  public String getStartTag() {
    StringBuilder startTagBuilder = new StringBuilder();
    startTagBuilder.append("!");
    startTagBuilder.append(pictureUrl);
    startTagBuilder.append("(");
    return startTagBuilder.toString();
  }

  @Override
  public String getEndTag() {
    return ")!";
  }
}

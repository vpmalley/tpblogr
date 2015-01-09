package blogr.vpm.fr.blogr.format;

import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;

/**
 * Created by vince on 08/01/15.
 */
public class AlignRightTagsProvider implements SurroundingTagsProvider {
  @Override
  public String getStartTag() {
    return "[right]";
  }

  @Override
  public String getEndTag() {
    return "[/right]";
  }
}

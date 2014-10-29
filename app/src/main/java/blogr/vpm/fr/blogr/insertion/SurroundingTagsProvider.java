package blogr.vpm.fr.blogr.insertion;

/**
 * Created by vincent on 08/10/14.
 * <p/>
 * Provides content as two surrounding tags
 */
public interface SurroundingTagsProvider {

  /**
   * Provides the starting tag  to surround a selection
   *
   * @return the text starting tag
   */
  String getStartTag();

  /**
   * Provides the ending tag  to surround a selection
   *
   * @return the text ending tag
   */
  String getEndTag();
}

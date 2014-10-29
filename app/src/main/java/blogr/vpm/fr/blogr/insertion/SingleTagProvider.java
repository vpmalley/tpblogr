package blogr.vpm.fr.blogr.insertion;

/**
 * Created by vincent on 08/10/14.
 * <p/>
 * Provides content as a single tag
 */
public interface SingleTagProvider {

  /**
   * Provides the tag to insert
   *
   * @return one text tag
   */
  String getTag();
}

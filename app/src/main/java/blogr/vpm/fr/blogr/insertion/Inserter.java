package blogr.vpm.fr.blogr.insertion;

import android.widget.EditText;

/**
 * Created by vincent on 08/10/14.
 * <p/>
 * Insert any text transformation tag or any extra content
 */
public interface Inserter {

  /**
   * Inserts in the content EditText one tag at the end of the current selection
   *
   * @param contentField the EditText of the content to update with the tag
   * @param tagProvider  the TagProvider providing one single tag to insert content
   * @return the whole content, updated
   */
  String insert(EditText contentField, SingleTagProvider tagProvider);

  /**
   * Inserts in the content EditText tags surrounding the current selection
   *
   * @param contentField the EditText of the content to update with the tags
   * @param tagProvider  the TagProvider providing two tags to insert
   * @return the whole content, updated
   */
  String insert(EditText contentField, SurroundingTagsProvider tagProvider);
}

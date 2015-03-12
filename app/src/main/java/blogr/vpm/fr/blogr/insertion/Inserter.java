package blogr.vpm.fr.blogr.insertion;

import android.widget.EditText;

import blogr.vpm.fr.blogr.bean.Post;

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

  /**
   * Prepends the tag before the post content, so that the new post content will contain the tag and then the initial content
   * @param post the initial post content that will appear after the tag
   * @param tagProvider provides the tag that will appear before the content
   */
  void prepend(Post post, SingleTagProvider tagProvider);

  /**
   * Appends the tag after the post content, so that the new post content will contain the initial content and then the tag
   * @param post the initial content that will appear before the tag
   * @param tagProvider provides the tag that will appear after the content
   */
  void append(Post post, SingleTagProvider tagProvider);

  /**
   * Surround the initial content with the tags
   * @param post the initial content that will be surrounded by tags
   * @param tagProvider provides the tag that will surround the content
   */
  void surround(Post post, SurroundingTagsProvider tagProvider);
}

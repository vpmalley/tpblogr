package blogr.vpm.fr.blogr.insertion;

import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 08/10/14.
 * <p/>
 * Extract any type of post from the content
 */
public interface Extracter {

  /**
   * Extracts a Post from a String
   * @param blog the blog of the post
   * @param post the post to extract from and modify
   */
  void extract(Blog blog, Post post);
}

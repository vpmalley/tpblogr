package blogr.vpm.fr.blogr.persistence;

import java.io.IOException;
import java.util.List;

import blogr.vpm.fr.blogr.bean.Blog;

/**
 * Created by vince on 01/12/14.
 */
public interface BlogRetriever {

  /**
   * Retrieves all persisted blogs
   *
   * @return the list of persisted blogs
   */
  List<Blog> retrieveAll() throws IOException;

  /**
   * Determines whether such a blog exists already
   * @param blog the blog to check
   * @return whether the blog exists
   */
  boolean exists(Blog blog) throws IOException;
}

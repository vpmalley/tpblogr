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
}

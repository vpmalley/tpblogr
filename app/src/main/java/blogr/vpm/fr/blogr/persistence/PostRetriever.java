package blogr.vpm.fr.blogr.persistence;

import java.util.List;

import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 08/10/14.
 */
public interface PostRetriever {

  /**
   * Retrieves all persisted blog posts
   *
   * @return the list of persisted posts
   */
  List<Post> retrieveAll();
}

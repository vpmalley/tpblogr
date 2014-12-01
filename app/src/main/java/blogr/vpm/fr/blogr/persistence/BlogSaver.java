package blogr.vpm.fr.blogr.persistence;

import java.io.IOException;

import blogr.vpm.fr.blogr.bean.Blog;

/**
 * Created by vince on 01/12/14.
 */
public interface BlogSaver {

  /**
   * Persists a blog metadata to be retrieved later
   *
   * @param blog the blog to save
   * @return whether it has been persisted successfully
   */
  boolean persist(Blog blog) throws IOException;
}

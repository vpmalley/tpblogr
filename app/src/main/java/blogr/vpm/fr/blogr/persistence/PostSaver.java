package blogr.vpm.fr.blogr.persistence;

import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 06/10/14.
 */
public interface PostSaver {

  /**
   * Determines whether the post exists in the storage
   *
   * @param post the post to then save
   * @return whether the post already exists
   */
  boolean exists(Post post);

  /**
   * Persists a blog post to be retrieved later
   *
   * @param post the post to save
   * @return whether it has been persisted successfully
   */
  boolean persist(Post post);

  /**
   * Deletes a persisted post
   *
   * @param post the post to delete
   * @return whether the post was deleted
   */
  boolean delete(Post post);

}

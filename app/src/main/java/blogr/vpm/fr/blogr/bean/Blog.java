package blogr.vpm.fr.blogr.bean;

import java.io.Serializable;

import blogr.vpm.fr.blogr.service.PostPublishingServiceProvider;

/**
 * Created by vincent on 29/08/14.
 *
 * Any kind of blog should implement these methods
 */
public interface Blog extends Serializable, PostPublishingServiceProvider {

  /**
   * The default folder to put posts in.
   */
  public static final String POSTS_DIR = "posts";

  /**
   * The title of the blog, the minimum to identify it
   * @return a title for the blog
   */
  String getTitle();

  /**
   * Where the posts are stored. Use {@link Blog#POSTS_DIR} if none specific
   * @return which folder posts are stored
   */
  String getPostsFolder();
}

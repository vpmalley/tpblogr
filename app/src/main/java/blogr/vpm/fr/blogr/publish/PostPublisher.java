package blogr.vpm.fr.blogr.publish;

import blogr.vpm.fr.blogr.bean.EmailBlog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 29/08/14.
 * <p/>
 * Interface for the publication of a blog post. Any blog post is posted on one blog.
 */
public interface PostPublisher {

  /**
   * Publishes the post on the blog.
   *
   * @param blog the blog to publish on
   * @param post the post to publish
   */
  void publish(EmailBlog blog, Post post);

  /**
   * Overrides the default formatter for this publisher
   *
   * @param formatter the formatter to use when publishing
   */
  void setFormatter(Formatter formatter);
}

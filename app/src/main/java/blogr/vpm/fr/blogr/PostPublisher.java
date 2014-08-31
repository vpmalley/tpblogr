package blogr.vpm.fr.blogr;

import javax.mail.MessagingException;

/**
 * Created by vincent on 29/08/14.
 *
 * Interface for the publication of a blog post. Any blog post is posted on one blog.
 */
public interface PostPublisher {

  /**
   * Publishes the post on the blog.
   * @param blog the blog to publish on
   * @param post the post to publish
   */
  public void publish(Blog blog, Post post) throws MessagingException;
}

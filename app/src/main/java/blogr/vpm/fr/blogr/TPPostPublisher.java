package blogr.vpm.fr.blogr;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

/**
 * Created by vincent on 29/08/14.
 */
public class TPPostPublisher implements PostPublisher {

  @Override
  public void publish(Blog blog, Post post) throws MessagingException {
   Message postMessage = generateEmail(post);
   postMessage.setRecipient(blog.getRecipient());
      Transport.send(postMessage);
  }

  private Message generateEmail(Post post) throws MessagingException {
    Message message = new Message();
    message.setSubject(post.getTitle());
    message.setDataHandler(new DataHandler());
    return message;
  }
}

package blogr.vpm.fr.blogr.publish;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 04/10/14.
 */
public class TPJavaMailPostPublisher implements PostPublisher {

  private Formatter formatter;

  @Override
  public void publish(Blog blog, Post post) {
    try {
      Message postMessage = generateEmail(post);
      //postMessage.setRecipient(blog.getRecipient());
      Transport.send(postMessage);
    } catch (MessagingException e) {
      // TODO display error
    }
  }

  @Override
  public void setFormatter(Formatter formatter) {
    this.formatter = formatter;
  }

  private Message generateEmail(Post post) throws MessagingException {
    Message message = new MimeMessage(Session.getDefaultInstance(new Properties()));
    message.setSubject(post.getTitle());
    //message.setDataHandler(new DataHandler);
    return message;
  }

}

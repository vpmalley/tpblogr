package blogr.vpm.fr.blogr.bean;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Created by vincent on 29/08/14.
 */
public class EmailBlog implements Blog {

  private String recipientEmail;

  private String title;

  public EmailBlog(String title, String recipientEmail) {
    this.title = title;
    this.recipientEmail = recipientEmail;
  }

  @Override
  public String getTitle() { return title;}

  @Override
  public String getPostsFolder() {
    return POSTS_DIR;
  }

  public Address getRecipient() throws AddressException {
    return new InternetAddress(recipientEmail);
  }

  public String getEmailAddress() {
    return recipientEmail;
  }
}

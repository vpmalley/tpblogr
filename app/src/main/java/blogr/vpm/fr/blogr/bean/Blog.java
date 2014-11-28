package blogr.vpm.fr.blogr.bean;

import java.io.Serializable;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Created by vincent on 29/08/14.
 */
public class Blog implements Serializable {

  private String recipientEmail;

  private String title;

  private String url;

  public Blog(String title, String recipientEmail) {
    this.title = title;
    this.recipientEmail = recipientEmail;
  }

  public String getTitle() { return title;}

  public Address getRecipient() throws AddressException {
    return new InternetAddress(recipientEmail);
  }

  public String getEmailAddress() {
    return recipientEmail;
  }
}

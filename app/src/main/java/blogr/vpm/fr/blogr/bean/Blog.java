package blogr.vpm.fr.blogr.bean;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Created by vincent on 29/08/14.
 */
public class Blog {

  private String recipientEmail;

  private String title;

  private String url;

  public Blog(String title, String recipientEmail) {
    this.title = title;
    this.recipientEmail = recipientEmail;
  }

  public Address getRecipient() throws AddressException {
      return new InternetAddress(recipientEmail);
  }

  public String getEmailAddress() {
        return recipientEmail;
    }
}

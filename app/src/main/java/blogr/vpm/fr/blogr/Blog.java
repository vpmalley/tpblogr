package blogr.vpm.fr.blogr;

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

  public Address getRecipient() throws AddressException {
      return new InternetAddress(recipientEmail);
  }
}

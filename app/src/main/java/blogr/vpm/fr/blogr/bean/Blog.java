package blogr.vpm.fr.blogr.bean;

import java.io.Serializable;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Created by vincent on 29/08/14.
 */
public interface Blog extends Serializable {

  String getTitle();
}

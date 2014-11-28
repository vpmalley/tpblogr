package blogr.vpm.fr.blogr.bean;

import android.content.Context;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;
import blogr.vpm.fr.blogr.picture.PictureMdTagsProvider;
import blogr.vpm.fr.blogr.publish.PostPublisher;
import blogr.vpm.fr.blogr.publish.StdEmailPostPublisher;

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

  @Override
  public PostPublisher getPublisherService(Context context) {
    return new StdEmailPostPublisher(context);
  }

  @Override
  public SurroundingTagsProvider getPictureTagsProvider(Context context, String pictureUrl) {
    return new PictureMdTagsProvider(pictureUrl);
  }

  public Address getRecipient() throws AddressException {
    return new InternetAddress(recipientEmail);
  }

  public String getEmailAddress() {
    return recipientEmail;
  }
}

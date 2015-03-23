package blogr.vpm.fr.blogr.bean;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import blogr.vpm.fr.blogr.insertion.Extracter;
import blogr.vpm.fr.blogr.insertion.SingleTagProvider;
import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;
import blogr.vpm.fr.blogr.picture.PictureMarkdownTagsProvider;
import blogr.vpm.fr.blogr.publish.PostPublisher;
import blogr.vpm.fr.blogr.publish.StdEmailPostPublisher;

/**
 * Created by vincent on 29/08/14.
 */
public class EmailBlog implements Blog {

  protected static final String TITLE_KEY = "title";
  protected static final String EMAIL_KEY = "email";

  protected String recipientEmail;

  protected String title;

  protected EmailBlog(){
  }

  public EmailBlog(String title, String recipientEmail) {
    this.title = title;
    this.recipientEmail = recipientEmail;
  }

  @Override
  public String getTitle() { return title;}

  @Override
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getPostsFolder() {
    return POSTS_DIR;
  }

  @Override
  public List<String> getMdKeys() {
    return new ArrayList<>();
  }

  @Override
  public PostPublisher getPublisherService(Context context) {
    return new StdEmailPostPublisher(context);
  }

  @Override
  public SurroundingTagsProvider getPictureTagsProvider(Context context, String pictureUrl) {
    return new PictureMarkdownTagsProvider(pictureUrl);
  }

  @Override
  public boolean hasMetadataProvider() {
    return false;
  }

  @Override
  public SingleTagProvider getMetadataProvider(Post post) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean hasMetadataExtracter() {
    return false;
  }

  @Override
  public Extracter getMetadataExtracter() {
    throw new UnsupportedOperationException();
  }

  public Address getRecipient() throws AddressException {
    return new InternetAddress(recipientEmail);
  }

  public String getEmailAddress() {
    return recipientEmail;
  }

  public void setRecipientEmail(String email) {
    this.recipientEmail = email;
  }

  @Override
  public String toString() {
    return getTitle();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    Bundle b = new Bundle();
    b.putString(TITLE_KEY, title);
    b.putString(EMAIL_KEY, recipientEmail);
    parcel.writeBundle(b);
  }

  private EmailBlog(Parcel in) {
    Bundle b = in.readBundle(Blog.class.getClassLoader());
    // without setting the classloader, it fails on BadParcelableException : ClassNotFoundException when
    // unmarshalling Media class
    title = b.getString(TITLE_KEY);
    recipientEmail = b.getString(EMAIL_KEY);
  }

  public static final Parcelable.Creator<EmailBlog> CREATOR
          = new Parcelable.Creator<EmailBlog>() {
    public EmailBlog createFromParcel(Parcel in) {
      return new EmailBlog(in);
    }

    public EmailBlog[] newArray(int size) {
      return new EmailBlog[size];
    }
  };


  public static class Storer implements Storage {

    @Override
    public Properties marshall(Blog blog) {
      Properties props = new Properties();
      props.setProperty(TITLE_KEY, blog.getTitle());
      props.setProperty(EMAIL_KEY, ((EmailBlog) blog).getEmailAddress());
      props.setProperty(TYPE_KEY, EmailBlog.class.getName());
      return props;
    }

    @Override
    public Blog unmarshall(Properties props) {
      String title = props.getProperty(TITLE_KEY);
      String email = props.getProperty(EMAIL_KEY);
      return new EmailBlog(title, email);
    }
  }
}

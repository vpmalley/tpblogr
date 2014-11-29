package blogr.vpm.fr.blogr.bean;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

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

  private static final String TITLE_KEY = "title";
  private static final String EMAIL_KEY = "email";
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
}

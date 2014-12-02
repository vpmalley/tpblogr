package blogr.vpm.fr.blogr.bean;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Properties;

import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;
import blogr.vpm.fr.blogr.picture.PictureTpTagsProvider;

/**
 * Created by vincent on 29/08/14.
 */
public class TPBlog extends EmailBlog {

  public TPBlog(String title, String recipientEmail) {
    super(title, recipientEmail);
  }

  @Override
  public SurroundingTagsProvider getPictureTagsProvider(Context context, String pictureUrl) {
    return new PictureTpTagsProvider(pictureUrl);
  }

  private TPBlog(Parcel in) {
    super();
    Bundle b = in.readBundle(Blog.class.getClassLoader());
    // without setting the classloader, it fails on BadParcelableException : ClassNotFoundException when
    // unmarshalling Media class
    title = b.getString(TITLE_KEY);
    recipientEmail = b.getString(EMAIL_KEY);
  }

  public static final Parcelable.Creator<TPBlog> CREATOR
          = new Parcelable.Creator<TPBlog>() {
    public TPBlog createFromParcel(Parcel in) {
      return new TPBlog(in);
    }

    public TPBlog[] newArray(int size) {
      return new TPBlog[size];
    }
  };

  public static class Storer implements Storage {

    @Override
    public Properties marshall(Blog blog) {
      Properties props = new Properties();
      props.setProperty(TITLE_KEY, blog.getTitle());
      props.setProperty(EMAIL_KEY, ((EmailBlog) blog).getEmailAddress());
      props.setProperty(TYPE_KEY, TPBlog.class.getName());
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

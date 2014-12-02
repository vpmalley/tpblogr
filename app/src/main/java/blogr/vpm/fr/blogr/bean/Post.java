package blogr.vpm.fr.blogr.bean;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vincent on 29/08/14.
 */
public class Post implements Parcelable {

  public static final String INTENT_EXTRA_KEY = "intent_extra_post";

  private static final String TITLE_KEY = "title";

  private static final String CONTENT_KEY = "content";

  private static final String DATE_KEY = "date";

  private static final String PICTURES_KEY = "pictures";

  private static final String BLOG_KEY = "blog";

  private static final String DATE_PATTERN = "yyyy-MM-dd-HH:mm:ss-ZZZZZ";

  private String title;

  private String content;

  private Date date;

  private final ArrayList<Uri> pictures;

  private Blog blog;

  public Post(String title, String content, Blog blog) {
    this.title = title;
    this.content = content;
    this.pictures = new ArrayList<Uri>();
    this.blog = blog;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getDate() {
    return date;
  }

  public Blog getBlog() {
    return blog;
  }

  public static Post emptyPost(Blog blog) {
    return new Post("", "", blog);
  }

  public void addPicture(Uri pictureUri) {
    pictures.add(pictureUri);
  }

  public ArrayList<Uri> getPicturesAsMediaContent() {
    return pictures;
  }

  public ArrayList<Uri> getPicturesAsFiles(Context context) {
    ArrayList<Uri> pictureFiles = new ArrayList<Uri>();
    for (Uri pic : pictures) {
      Cursor c = context.getContentResolver().query(pic, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
      if (c.getCount() > 0) {
        c.moveToFirst();
        String filePath = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
        File picFile = new File(filePath);
        picFile.setReadable(true, false);
        Uri pictureUri = Uri.fromFile(picFile);
        pictureFiles.add(pictureUri);
        c.close();
      }
    }
    return pictureFiles;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    Bundle b = new Bundle();
    b.putString(TITLE_KEY, title);
    b.putString(CONTENT_KEY, content);
    if (date != null) {
      b.putString(DATE_KEY, new SimpleDateFormat(DATE_PATTERN).format(date));
    }
    b.putParcelableArrayList(PICTURES_KEY, pictures);
    b.putParcelable(BLOG_KEY, blog);
    parcel.writeBundle(b);
  }

  private Post(Parcel in) {
    Bundle b = in.readBundle(Post.class.getClassLoader());
    // without setting the classloader, it fails on BadParcelableException : ClassNotFoundException when
    // unmarshalling Media class
    title = b.getString(TITLE_KEY);
    content = b.getString(CONTENT_KEY);
    try {
      String unparsedDate = b.getString(DATE_KEY);
      if (unparsedDate != null) {
        date = new SimpleDateFormat(DATE_PATTERN).parse(unparsedDate);
      }
    } catch (ParseException e) {
      date = Calendar.getInstance().getTime();
    }
    pictures = b.getParcelableArrayList(PICTURES_KEY);
    blog = b.getParcelable(BLOG_KEY);
  }

  public static final Parcelable.Creator<Post> CREATOR
          = new Parcelable.Creator<Post>() {
    public Post createFromParcel(Parcel in) {
      return new Post(in);
    }

    public Post[] newArray(int size) {
      return new Post[size];
    }
  };
}

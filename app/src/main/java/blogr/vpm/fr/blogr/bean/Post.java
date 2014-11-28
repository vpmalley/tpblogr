package blogr.vpm.fr.blogr.bean;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vincent on 29/08/14.
 */
public class Post implements Serializable {

  public static final String INTENT_EXTRA_KEY = "intent_extra_post";

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

  public static Post emptyPost() {
    return new Post("", "", new Blog("noblog", ""));
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
}

package blogr.vpm.fr.blogr.bean;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vince on 10/03/15.
 */
public class PostMetadata implements Parcelable{

  private static final String TITLE_KEY = "title";
  private static final String TRAVEL_DATE_KEY = "travelDate";
  private static final String EXCERPT_KEY = "excerpt";
  private static final String TAGS_KEY = "tags";
  private static final String DATA_KEY = "otherData";

  private static final String DATE_PATTERN = "yyyy-MM-dd-HH:mm:ss-ZZZ";

  public String postTitle;

  public String travelDate;

  public String excerpt;

  public ArrayList<String> tags;

  public HashMap<String, Parcelable> otherData;


  public PostMetadata() {
    this.tags = new ArrayList<>();
    this.otherData = new HashMap<>();
  }

  public PostMetadata(Post post, String travelDate, String excerpt) {
    this.postTitle = post.getTitle();
    this.travelDate = travelDate;
    this.excerpt = excerpt;
    this.tags = new ArrayList<>();
    this.otherData = new HashMap<>();
  }

  public void addTag(String tag) {
    tags.add(tag);
  }

  public void putData(String key, Parcelable value) {
    otherData.put(key, value);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    Bundle b = new Bundle();
    b.putString(TITLE_KEY, postTitle);
    b.putString(TRAVEL_DATE_KEY, travelDate);
    b.putString(EXCERPT_KEY, excerpt);
    b.putStringArrayList(TAGS_KEY, tags);
    b.putSerializable(DATA_KEY, otherData);
    parcel.writeBundle(b);
  }

  private PostMetadata(Parcel in) {
    Bundle b = in.readBundle(PostMetadata.class.getClassLoader());
    // without setting the classloader, it fails on BadParcelableException : ClassNotFoundException when
    // unmarshalling class
    postTitle = b.getString(TITLE_KEY);
    travelDate = b.getString(TRAVEL_DATE_KEY);
    excerpt = b.getString(EXCERPT_KEY);
    tags = b.getStringArrayList(TAGS_KEY);
    otherData = (HashMap<String, Parcelable>) b.getSerializable(DATA_KEY);
  }

  public static final Parcelable.Creator<PostMetadata> CREATOR
          = new Parcelable.Creator<PostMetadata>() {
    public PostMetadata createFromParcel(Parcel in) {
      return new PostMetadata(in);
    }

    public PostMetadata[] newArray(int size) {
      return new PostMetadata[size];
    }
  };

}

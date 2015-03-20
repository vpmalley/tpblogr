package blogr.vpm.fr.blogr.bean;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vince on 10/03/15.
 */
public class PostMetadata implements Parcelable{

  public static final String TITLE_KEY = "title";
  public static final String TRAVEL_DATE_KEY = "travelDate";
  private static final String EXCERPT_KEY = "excerpt";
  private static final String LAYOUT_KEY = "layout";
  private static final String TAGS_KEY = "tags";
  private static final String DATA_KEY = "otherData";

  private static final String DATE_PATTERN = "yyyy-MM-dd";

  public static final List<String> IMMUTABLE_KEYS = Arrays.asList(TITLE_KEY, TRAVEL_DATE_KEY, EXCERPT_KEY, TAGS_KEY);

  public String title;

  public String travelDate;

  public String excerpt;

  public String layout;

  public ArrayList<String> tags;

  private HashMap<String, Object> otherData;

  public PostMetadata() {
    this.tags = new ArrayList<>();
    this.otherData = new HashMap<>();
  }

  public PostMetadata(List<String> mdKeys) {
    this.title = "";
    this.travelDate = "";
    this.excerpt = "";
    this.layout = "";
    this.tags = new ArrayList<>();
    this.otherData = new HashMap<>();
    for (String key : mdKeys) {
      this.otherData.put(key, "");
    }
  }

  public PostMetadata(Map<String, Object> md) {
    this.title = String.valueOf(md.get(TITLE_KEY));
    this.travelDate = String.valueOf(md.get(TRAVEL_DATE_KEY));
    this.excerpt = String.valueOf(md.get(EXCERPT_KEY));
    this.layout = String.valueOf(md.get(LAYOUT_KEY));
    this.tags = (ArrayList<String>) md.get(TAGS_KEY);
    this.otherData = new HashMap<>(md);
  }

  public void addTag(String tag) {
    tags.add(tag);
  }

  public void putData(String key, Object value) {
    otherData.put(key, value);
  }

  public void addKeys(List<String> keys){
    for (String key : keys) {
      if (!otherData.containsKey(key)){
        otherData.put(key, "");
      }
    }
  }

  public void putData(Map<String, String> allData) {
    otherData.putAll(allData);
  }

  public HashMap<String, ?> getAsMap() {
    HashMap<String, Object> allMD = new HashMap<>();
    allMD.putAll(otherData);
    allMD.put(TITLE_KEY, title);
    allMD.put(TRAVEL_DATE_KEY, travelDate);
    allMD.put(EXCERPT_KEY, excerpt);
    allMD.put(LAYOUT_KEY, layout);
    if ((tags != null) && (!tags.isEmpty())) {
      allMD.put(TAGS_KEY, tags);
    }
    return allMD;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    Bundle b = new Bundle();
    b.putString(TITLE_KEY, title);
    b.putString(TRAVEL_DATE_KEY, travelDate);
    b.putString(EXCERPT_KEY, excerpt);
    b.putString(LAYOUT_KEY, layout);
    b.putStringArrayList(TAGS_KEY, tags);
    b.putSerializable(DATA_KEY, otherData);
    parcel.writeBundle(b);
  }

  private PostMetadata(Parcel in) {
    Bundle b = in.readBundle(PostMetadata.class.getClassLoader());
    // without setting the classloader, it fails on BadParcelableException : ClassNotFoundException when
    // unmarshalling class
    title = b.getString(TITLE_KEY);
    travelDate = b.getString(TRAVEL_DATE_KEY);
    excerpt = b.getString(EXCERPT_KEY);
    layout = b.getString(LAYOUT_KEY);
    tags = b.getStringArrayList(TAGS_KEY);
    otherData = (HashMap<String, Object>) b.getSerializable(DATA_KEY);
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

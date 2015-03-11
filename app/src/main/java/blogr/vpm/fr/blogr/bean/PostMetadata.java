package blogr.vpm.fr.blogr.bean;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import blogr.vpm.fr.blogr.R;

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

  public String getPostTitle() {
    return postTitle;
  }

  public String getTravelDate() {
    return travelDate;
  }

  public String getExcerpt() {
    return excerpt;
  }

  public void addTag(String tag) {
    tags.add(tag);
  }

  public void putData(String key, Parcelable value) {
    //otherData.put(key, value);
  }
  /**
   * Adds the metadata as YAML content at the beginning of the file
   * @param postFile the file to add the metadata to
   */
  public static void prependToFile(Context context, File postFile, Post post, PostMetadata postMD){
    if (!postFile.exists()) {
      return;
    }
    FileOutputStream postFileOut = null;
    YamlWriter yamlWriter = null;
    FileWriter postWriter = null;
    try {
      postFileOut = new FileOutputStream(postFile);
      try {
        yamlWriter = new YamlWriter(new FileWriter(postFile));
        yamlWriter.getConfig().setPropertyElementType(PostMetadata.class, "tags", String.class);
        yamlWriter.write(postMD);
      } finally {
        if (yamlWriter != null) {
          try {
            yamlWriter.close();
          } catch (YamlException e) {
            Log.w("IO", "file might not have been saved. " + e.toString());
          }
        }
      }
      try {
        postWriter = new FileWriter(postFile, true);
        postWriter.write(post.getContent());
      } finally {
        if (postWriter != null) {
          postWriter.close();
        }
      }
    } catch (IOException e) {
      Toast.makeText(context, context.getResources().getString(R.string.cannotsavepost), Toast.LENGTH_SHORT).show();
    } finally {
      if (postFileOut != null) {
        try {
          postFileOut.close();
        } catch (IOException e) {
          Log.w("IO", "file might not have been saved. " + e.toString());
        }
      }
    }

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
    //b.putStringArrayList(TAGS_KEY, tags);
    //b.putSerializable(DATA_KEY, otherData);
    parcel.writeBundle(b);
  }

  private PostMetadata(Parcel in) {
    Bundle b = in.readBundle(PostMetadata.class.getClassLoader());
    // without setting the classloader, it fails on BadParcelableException : ClassNotFoundException when
    // unmarshalling Media class
    postTitle = b.getString(TITLE_KEY);
    travelDate = b.getString(TRAVEL_DATE_KEY);
    excerpt = b.getString(EXCERPT_KEY);
    //tags = b.getStringArrayList(TAGS_KEY);
    //otherData = (HashMap<String, Parcelable>) b.getSerializable(DATA_KEY);
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

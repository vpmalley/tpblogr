package blogr.vpm.fr.blogr.bean;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.git.AsyncGithubBlogCloner;
import blogr.vpm.fr.blogr.insertion.Extracter;
import blogr.vpm.fr.blogr.insertion.SingleTagProvider;
import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;
import blogr.vpm.fr.blogr.metadata.YamlMetadataExtracter;
import blogr.vpm.fr.blogr.metadata.YamlMetadataProvider;
import blogr.vpm.fr.blogr.persistence.FileBlogManager;
import blogr.vpm.fr.blogr.picture.PictureTextileTagsProvider;
import blogr.vpm.fr.blogr.publish.GithubPublisher;
import blogr.vpm.fr.blogr.publish.PostPublisher;

/**
 * Created by vincent on 29/08/14.
 */
public class GithubBlog implements Blog {

  public static final String REPO_SUFFIX = ".github.io";
  public static final String GITHUB_DOMAIN = "https://github.com/";
  private static final String USERNAME_KEY = "username";
  private static final String MD_KEY = "mdKeys";

  private String username;

  private List<String> mdKeys = new ArrayList<>();

  public GithubBlog(String username) {
    this.username = username;
  }

  @Override
  public String getTitle() { return username + REPO_SUFFIX;}

  @Override
  public void setTitle(String title) {
    int suffixStart = title.indexOf(REPO_SUFFIX);
    if (suffixStart > -1) {
      this.username = title.substring(0, suffixStart);
    } else {
      this.username = title;
    }
  }

  @Override
  public String getPostsFolder() {
    return "_drafts";
  }

  /**
   * Adds comma-separated keys
   * @param keys comma-separated keys
   */
  public void addMdKeys(String keys){
    if ((keys != null) && (!keys.isEmpty())) {
      for (String key : keys.split(",")) {
        mdKeys.add(key);
      }
    }
  }

  @Override
  public List<String> getMdKeys() {
    return mdKeys;
  }

  public String getKeysAsString(){
    StringBuilder keys = new StringBuilder();
    if (!mdKeys.isEmpty()) {
      for (String key : mdKeys) {
        keys.append(key);
        keys.append(",");
      }
    }
    return keys.toString();
  }

  @Override
  public PostPublisher getPublisherService(Activity activity) {
    return new GithubPublisher(activity);
  }

  @Override
  public SurroundingTagsProvider getPictureTagsProvider(Context context, String pictureUrl) {
    return new PictureTextileTagsProvider(pictureUrl);
  }

  @Override
  public boolean hasMetadataProvider() {
    return true;
  }

  @Override
  public SingleTagProvider getMetadataProvider(Post post) {
    return new YamlMetadataProvider(post.getMd());
  }

  @Override
  public boolean hasMetadataExtracter() {
    return true;
  }

  @Override
  public Extracter getMetadataExtracter() {
    return new YamlMetadataExtracter();
  }

  public String getRepositoryUrl() {
    return GITHUB_DOMAIN + username + "/" + username + REPO_SUFFIX + ".git";
  }

  /**
   * Clones the repository associated with the github account.
   * The Github Pages repository should be created before.
   * This is to be done when creating the blog
   */
  public void cloneRepository(Context context){
    boolean exists = false;
    try {
      exists = new FileBlogManager().exists(this);
    } catch (IOException e) {
      Log.w("blog", "Could not access the blog metadata");
    }
    if (exists) {
      Toast.makeText(context, context.getString(R.string.blog_already_exists), Toast.LENGTH_SHORT).show();
    } else {
      new AsyncGithubBlogCloner(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this);
    }
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
    b.putString(USERNAME_KEY, username);
    b.putString(MD_KEY, getKeysAsString());
    parcel.writeBundle(b);
  }

  private GithubBlog(Parcel in) {
    Bundle b = in.readBundle(Blog.class.getClassLoader());
    // without setting the classloader, it fails on BadParcelableException : ClassNotFoundException when
    // unmarshalling Media class
    username = b.getString(USERNAME_KEY);
    addMdKeys(b.getString(MD_KEY));
  }

  public static final Parcelable.Creator<GithubBlog> CREATOR
          = new Parcelable.Creator<GithubBlog>() {
    public GithubBlog createFromParcel(Parcel in) {
      return new GithubBlog(in);
    }

    public GithubBlog[] newArray(int size) {
      return new GithubBlog[size];
    }
  };

  public static class Storer implements Storage {

    @Override
    public Properties marshall(Blog blog) {
      Properties props = new Properties();
      props.setProperty(USERNAME_KEY, blog.getTitle().replace(REPO_SUFFIX, ""));
      props.setProperty(TYPE_KEY, GithubBlog.class.getName());
      props.setProperty(MD_KEY, ((GithubBlog)blog).getKeysAsString());
      return props;
    }

    @Override
    public Blog unmarshall(Properties props) {
      String username = props.getProperty(USERNAME_KEY);
      String mdKeys = props.getProperty(MD_KEY);
      GithubBlog b = new GithubBlog(username);
      b.addMdKeys(mdKeys);
      return b;
    }
  }

}

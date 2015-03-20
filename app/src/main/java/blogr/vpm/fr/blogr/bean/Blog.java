package blogr.vpm.fr.blogr.bean;

import android.os.Parcelable;

import java.util.List;
import java.util.Properties;

import blogr.vpm.fr.blogr.service.PostPublishingServiceProvider;

/**
 * Created by vincent on 29/08/14.
 *
 * TODO fix bug: apparently serializing a Parcelable subclass is bad.
 * What matters is the explicit type of serialized object (i.e. not the result of instanceof)
 * cf. http://idlesun.blogspot.ca/2012/12/android-parcelable-example-3-subclass.html
 *
 * Any kind of blog should implement these methods
 */
public interface Blog extends Parcelable, PostPublishingServiceProvider {

  /**
   * The default folder to put posts in.
   */
  public static final String POSTS_DIR = "posts";

  /**
   * The title of the blog, the minimum to identify it
   * @return a title for the blog
   */
  String getTitle();

  void setTitle(String title);

  /**
   * Where the posts are stored. Use {@link Blog#POSTS_DIR} if none specific
   * @return which folder posts are stored
   */
  String getPostsFolder();

  /**
   * Lists all metadata keys specific to this blog
   * @return a list of metadata keys specific to this blog
   */
  List<String> getMdKeys();

  public interface Storage {

    public static final String TYPE_KEY = "type";

    Properties marshall(Blog blog);

    Blog unmarshall(Properties props);

  }
}

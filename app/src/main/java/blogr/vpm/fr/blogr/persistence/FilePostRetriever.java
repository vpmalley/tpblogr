package blogr.vpm.fr.blogr.persistence;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.googlecode.flickrjandroid.blogs.BlogsInterface;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 08/10/14.
 */
public class FilePostRetriever implements PostRetriever {

  private static final String BLOGS_DIR = "blogs";

  private static final String POSTS_DIR = "posts";

  private static final String APP_DIR = "BlogR";

  private final Context context;

  public FilePostRetriever(Context context) {
    this.context = context;
  }

  @Override
  public List<Post> retrieveAll() {
    List<Post> posts = new ArrayList<Post>();
    if (isExternalStorageReadable()) {

      File blogsDir = new File(Environment.getExternalStoragePublicDirectory(APP_DIR), BLOGS_DIR);

      List<Blog> blogs = retrieveBlogs(blogsDir);
      for (Blog blog : blogs) {
        File blogDir = new File(blogsDir, blog.getTitle());
        File postDir = new File(blogDir, POSTS_DIR);
        posts.addAll(retrievePosts(postDir, blog));
      }
    }
    return posts;
  }

  /**
   * Scans the given directory to retrieve all files, each corresponding to a blog
   * @param blogsDir the directory for the blogs
   * @return the list of blogs for this device
   */
  private List<Blog> retrieveBlogs(File blogsDir) {
    List<Blog> blogs = new ArrayList<Blog>();
    if (blogsDir.exists() && blogsDir.isDirectory()) {
      for (File blogDir : blogsDir.listFiles()) {
        blogs.add(new Blog(blogDir.getName(), ""));
      }
    }
    return blogs;
  }

  /**
   * Scans the given directory to retrieve all files, each corresponding to a post
   * @param postDir the directory for the posts
   * @param blog the blog to list posts for
   * @return the list of posts for this blog
   */
  private List<Post> retrievePosts(File postDir, Blog blog) {
    List<Post> posts = new ArrayList<Post>();
    if (postDir.exists() && postDir.isDirectory()) {
      for (File postFile : postDir.listFiles()) {
        StringWriter postWriter = new StringWriter();
        FileInputStream postFileIn = null;
        try {
          postFileIn = new FileInputStream(postFile);
          IOUtils.copy(postFileIn, postWriter, "UTF-8");
        } catch (IOException e) {
          Toast.makeText(context, context.getResources().getString(R.string.cannotgepost), Toast.LENGTH_SHORT).show();
        } finally {
          if (postFileIn != null) {
            try {
              postFileIn.close();
            } catch (IOException e) {
              Toast.makeText(context, context.getResources().getString(R.string.mightnotgepost), Toast.LENGTH_SHORT).show();
            }
          }
        }
        posts.add(new Post(postFile.getName().replace(".txt", "").replace('_', ' '), postWriter.toString(), blog));
      }
    }
    return posts;
  }

  private boolean isExternalStorageReadable() {
    String storageState = Environment.getExternalStorageState();
    return (Environment.MEDIA_MOUNTED.equals(storageState) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(storageState));
  }
}

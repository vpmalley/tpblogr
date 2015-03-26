package blogr.vpm.fr.blogr.persistence;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.EmailBlog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 08/10/14.
 */
public class FilePostRetriever implements PostRetriever {

  private final Context context;

  public FilePostRetriever(Context context) {
    this.context = context;
  }

  @Override
  public List<Post> retrieveAll() {
    List<Post> posts = new ArrayList<Post>();
    if (isExternalStorageReadable()) {

      File blogsDir = new FileBlogManager().getBlogsDir();

      List<Blog> blogs = null;
      try {
        blogs = new FileBlogManager().retrieveAll();
        for (Blog blog : blogs) {
          File blogDir = new File(blogsDir, blog.getTitle());
          File postDir = new File(blogDir, blog.getPostsFolder());
          posts.addAll(retrievePosts(postDir, blog));
        }
      } catch (IOException e) {
        Toast.makeText(context, context.getResources().getString(R.string.cannotgepost), Toast.LENGTH_SHORT).show();
      }
    }
    return posts;
  }

  /**
   * Scans the given directory to retrieve all files, each corresponding to a blog
   * @param blogsDir the directory for the blogs
   * @return the list of blogs for this device
   */
  private List<EmailBlog> retrieveBlogs(File blogsDir) {
    List<EmailBlog> blogs = new ArrayList<EmailBlog>();
    if (blogsDir.exists() && blogsDir.isDirectory()) {
      for (File blogDir : blogsDir.listFiles()) {
        blogs.add(new EmailBlog(blogDir.getName(), ""));
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
        if (!postFile.getPath().endsWith(".json")) {
          String content = readFile(postFile);
          Post post = new Post(postFile.getName().replace(".txt", "").replace('_', ' '), content, blog);
          if (blog.hasMetadataExtracter()) {
            blog.getMetadataExtracter().extract(blog, post);
          }
          File dataFile = new FileManager().getDataFileForPost(context, post);
          if (dataFile.exists()) {
            String serializedPlaces = readFile(dataFile);
            try {
              Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
              Post postData = gson.fromJson(serializedPlaces, Post.class);
              post.setPlaces(postData.getPlaces());
              post.setFlickrPictures(postData.getFlickrPictures());
            } catch(JsonSyntaxException e) {
              Log.w("json", "Error reading JSON data. " + e.toString());
            }
          }
          posts.add(post);
        }
      }
    }
    return posts;
  }

  private String readFile(File postFile) {
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
    return postWriter.toString();
  }

  private boolean isExternalStorageReadable() {
    String storageState = Environment.getExternalStorageState();
    return (Environment.MEDIA_MOUNTED.equals(storageState) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(storageState));
  }
}

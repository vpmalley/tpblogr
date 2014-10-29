package blogr.vpm.fr.blogr.persistence;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 06/10/14.
 */
public class FilePostSaver implements PostSaver {

  public static final String POSTS_DIR = "posts";
  public static final String APP_DIR = "BlogR";
  private final Context context;

  public FilePostSaver(Context context) {
    this.context = context;
  }

  @Override
  public boolean exists(Post post) {
    File postFile = getFileForPost(post);
    return postFile.exists();
  }

  @Override
  public boolean persist(Post post) {
    boolean saved = false;
    if (isExternalStorageWritable()) {
      File postFile = getFileForPost(post);
      if (!postFile.exists()) {
        try {
          postFile.createNewFile();
        } catch (IOException e) {
          Toast.makeText(context, context.getResources().getString(R.string.cannotsavepost), Toast.LENGTH_SHORT).show();
        }
      }
      // fill file
      FileOutputStream postFileOut = null;
      try {
        postFileOut = new FileOutputStream(postFile);
        IOUtils.copy(new StringReader(post.getContent()), postFileOut, "UTF-8");
        saved = true;
      } catch (IOException e) {
        Toast.makeText(context, context.getResources().getString(R.string.cannotsavepost), Toast.LENGTH_SHORT).show();
      } finally {
        if (postFileOut != null) {
          try {
            postFileOut.close();
          } catch (IOException e) {
            Toast.makeText(context, context.getResources().getString(R.string.mightnotsavepost), Toast.LENGTH_SHORT).show();
          }
        }
      }
    }
    return saved;
  }

  @Override
  public boolean delete(Post post) {
    boolean deleted = false;
    if (isExternalStorageWritable()) {
      File postFile = getFileForPost(post);
      deleted = postFile.delete();
    }
    return deleted;
  }

  /**
   * Returns the file, created if non-existent, for the post
   *
   * @param post the post we want the matching file for
   * @return an existing file for the post
   * @pre the external storage should be writable
   */
  private File getFileForPost(Post post) {
    File dir = new File(Environment.getExternalStoragePublicDirectory(APP_DIR), POSTS_DIR);
    dir.mkdirs();
    // create file if non existent
    File postFile = new File(dir, getFileName(post));
    return postFile;
  }

  /**
   * Produces the file name corresponding to a post.
   * - It handles the case post has no title
   * - It only returns the name of the file, i.e. not the full path.
   * - Posts should be stored in APP_DIR/POSTS_DIR
   *
   * @param post The file name produced matches this post title
   * @return a file name that must be used to retrieve/update/delete the post file
   */
  private String getFileName(Post post) {
    String postTitle = post.getTitle();
    if (postTitle.isEmpty()) {
      postTitle = context.getResources().getString(R.string.newpost);
    }
    return postTitle.replace(' ', '_') + ".txt";
  }

  /**
   * Finds out whether the external storage is writable
   *
   * @return whether it is writable
   */
  private boolean isExternalStorageWritable() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }
}

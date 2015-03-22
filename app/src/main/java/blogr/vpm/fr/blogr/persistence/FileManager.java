package blogr.vpm.fr.blogr.persistence;

import android.content.Context;

import java.io.File;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 04/02/15.
 *
 * Only gives the files for the different types (Blog and Post)
 */
public class FileManager {


  public File getFileForBlog(Blog blog){
    File blogsDir = new FileBlogManager().getBlogsDir();
    return new File(blogsDir, blog.getTitle());
  }

  /**
   * Returns the file (maybe non-existent) for the post
   *
   * @param post the post we want the matching file for
   * @return a file for the post
   * @pre the external storage should be writable
   */
  public File getFileForPost(Context context, Post post){
    File blogsDir = new FileBlogManager().getBlogsDir();
    File blogDir = new File(blogsDir, post.getBlog().getTitle());
    File postDir = new File(blogDir, post.getBlog().getPostsFolder());
    postDir.mkdirs();
    File postFile = new File(postDir, getFileName(context, post));
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
  private String getFileName(Context context, Post post) {
    String postTitle = post.getTitle();
    if (postTitle.isEmpty()) {
      postTitle = context.getResources().getString(R.string.newpost);
    }
    return postTitle.replace(' ', '_') + ".txt";
  }

  /**
   * Returns the file (maybe non-existent) for the post
   *
   * @param post the post we want the matching file for
   * @return a file to store data about the post
   * @pre the external storage should be writable
   */
  public File getDataFileForPost(Context context, Post post){
    File blogsDir = new FileBlogManager().getBlogsDir();
    File blogDir = new File(blogsDir, post.getBlog().getTitle());
    File postDir = new File(blogDir, post.getBlog().getPostsFolder());
    postDir.mkdirs();
    File postFile = new File(postDir, getFileName(context, post).replace(".txt", ".json"));
    return postFile;
  }
}

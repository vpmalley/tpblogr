package blogr.vpm.fr.blogr.publish;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.git.AsyncGithubBlogCommitter;
import blogr.vpm.fr.blogr.git.GitInteraction;
import blogr.vpm.fr.blogr.git.GitRepository;
import blogr.vpm.fr.blogr.persistence.FileBlogManager;
import blogr.vpm.fr.blogr.persistence.FileManager;

/**
 * Created by vince on 08/02/15.
 */
public class GithubPublisher implements PostPublisher {

  private final Context context;
  private static final String PUBLISHED_POSTS_DIR = "_posts";;

  public GithubPublisher(Context context) {
    this.context = context;
  }

  @Override
  public void publish(Blog blog, Post post) {
    // move draft to posts with right name
    File newFileForPost = getFileForPost(post);
    boolean result = new FileManager().getFileForPost(context, post).renameTo(newFileForPost);

    // add and commit
    GitInteraction gitClient = new GitRepository();
    GithubBlog githubBlog = (GithubBlog) post.getBlog();
    if (result) {
      result = gitClient.add(githubBlog, PUBLISHED_POSTS_DIR + "/" + getFileName(post), context);
    }
    if (result) {
      new AsyncGithubBlogCommitter(context, "published " + post.getTitle()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (GithubBlog) blog);
    }
  }

  @Override
  public void setFormatter(Formatter formatter) {
    // do nothing
  }


  /**
   * Returns the file (maybe non-existent) for the post
   *
   * @param post the post we want the matching file for
   * @return a file for the post
   * @pre the external storage should be writable
   */
  public File getFileForPost(Post post){
    File blogsDir = new FileBlogManager().getBlogsDir();
    File blogDir = new File(blogsDir, post.getBlog().getTitle());
    File postDir = new File(blogDir, PUBLISHED_POSTS_DIR);
    postDir.mkdirs();
    File postFile = new File(postDir, getFileName(post));
    return postFile;
  }

  /**
   * Provides the file name for publication
   * @param post the post to publish
   * @return
   */
  private String getFileName(Post post) {
    String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    String formattedTitle = post.getTitle().replace(' ', '-');
    String finalName = formattedDate + "-" + formattedTitle + ".md";
    return finalName;
  }
}

package blogr.vpm.fr.blogr.publish;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import blogr.vpm.fr.blogr.activity.GithubPublicationDialogFragment;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.git.AsyncGithubBlogCommitter;
import blogr.vpm.fr.blogr.git.AsyncGithubBlogPuller;
import blogr.vpm.fr.blogr.git.AsyncGithubBlogPusher;
import blogr.vpm.fr.blogr.git.GitInteraction;
import blogr.vpm.fr.blogr.git.GitRepository;
import blogr.vpm.fr.blogr.network.DefaultNetworkChecker;
import blogr.vpm.fr.blogr.persistence.FileBlogManager;
import blogr.vpm.fr.blogr.persistence.FileManager;
import blogr.vpm.fr.blogr.persistence.FilePostSaver;
import blogr.vpm.fr.blogr.picture.Picture;

/**
 * Created by vince on 08/02/15.
 */
public class GithubPublisher implements PostPublisher {

  private final Activity activity;
  private static final String PUBLISHED_POSTS_DIR = "_posts";

  private Blog blog;

  private Post post;

  public GithubPublisher(Activity activity) {
    this.activity = activity;
  }

  @Override
  public void publish(Blog blog, Post post) {
    this.blog = blog;
    this.post = post;

    if (new DefaultNetworkChecker().checkNetworkForDownload(activity, true)) {

      // pull to synchronise the local repo with the distant one
      new AsyncGithubBlogPuller(activity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (GithubBlog) blog);

      // upload pictures, and replace their references in the Post
      Map<Picture, Picture> replacements = new HashMap<>();
      for (Picture p : post.getAllPictures()) {
        if (p.shouldBeUploaded()) {
          Picture uploaded = p.upload(activity);
          if (uploaded != null) {
            replacements.put(p, uploaded);
          }
        }
      }
      this.post.replaceUploadedPictures(replacements);
      new FilePostSaver(activity).persist(this.post);

      // ask for the Git credentials - maybe move this to blog creation
      GithubPublicationDialogFragment fragment = GithubPublicationDialogFragment.newInstance(this, blog.getTitle().replace(GithubBlog.REPO_SUFFIX, ""));
      fragment.show(activity.getFragmentManager(), "credentialInput"); // try to remove this ugly cast
    }
  }

  @Override
  public void setFormatter(Formatter formatter) {
    // do nothing
  }

  /**
   * Publishes once the dialog asking for username and password is dismissed.
   * @param username
   * @param password
   */
  public void publish(String username, String password) {

    // move draft to posts with right name
    File newFileForPost = getFileForPost(post);
    boolean result = new FileManager().getFileForPost(activity, post).renameTo(newFileForPost);

    // add and commit
    GitInteraction gitClient = new GitRepository();
    GithubBlog githubBlog = (GithubBlog) post.getBlog();
    if (result) {
      result = gitClient.add(githubBlog, PUBLISHED_POSTS_DIR + "/" + getFileName(post), activity);
    }
    if (result) {
      AsyncGithubBlogPusher pusher = new AsyncGithubBlogPusher(activity, username, password);
      new AsyncGithubBlogCommitter(activity, "published " + post.getTitle(), pusher).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (GithubBlog) blog);
    }
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

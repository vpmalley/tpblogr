package blogr.vpm.fr.blogr.git;

import android.content.Context;
import android.os.AsyncTask;

import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.network.DefaultNetworkChecker;

/**
 * Created by vince on 08/02/15.
 *
 * As long as there is a bug with JGit's commit command needing network access, keep this class
 */
public class AsyncGithubBlogCommitter extends AsyncTask<GithubBlog, Integer, Boolean> {

  private final Context context;

  private final String message;

  private GithubBlog[] blogs;

  public AsyncGithubBlogCommitter(Context context, String message) {
    this.context = context;
    this.message = message;
  }

  @Override
  protected Boolean doInBackground(GithubBlog... blogs) {
    // we commit on each of the Github blogs
    Boolean result = false;
    this.blogs = blogs;
    GitInteraction gitClient = new GitRepository();
    if (new DefaultNetworkChecker().checkNetworkForDownload(context, false)) {
      result = true;
      for (GithubBlog b : blogs) {
        result &= gitClient.commit(b, message);
      }
    }
    return result;
  }

  // for now we commit only just before pushing. Move this if commits without pushes are allowed
  @Override
  protected void onPostExecute(Boolean result) {
    if (result) {
      for (GithubBlog blog : blogs) {
        new AsyncGithubBlogPusher(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (GithubBlog) blog);
      }
    }
  }
}

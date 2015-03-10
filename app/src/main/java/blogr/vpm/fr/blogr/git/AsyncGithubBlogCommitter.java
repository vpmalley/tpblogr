package blogr.vpm.fr.blogr.git;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

  private final AsyncGithubBlogPusher nextTask;

  private GithubBlog[] blogs;

  public AsyncGithubBlogCommitter(Context context, String message, AsyncGithubBlogPusher nextTask) {
    this.context = context;
    this.message = message;
    this.nextTask = nextTask;
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
    Log.d("committer", "success : " + result);
    if (result) {
      for (GithubBlog blog : blogs) {
        nextTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (GithubBlog) blog);
        //new AsyncGithubBlogPusher(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (GithubBlog) blog);
      }
    }
  }
}

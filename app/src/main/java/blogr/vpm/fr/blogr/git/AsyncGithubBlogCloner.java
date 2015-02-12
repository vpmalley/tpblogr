package blogr.vpm.fr.blogr.git;

import android.content.Context;
import android.os.AsyncTask;

import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.network.DefaultNetworkChecker;

/**
 * Created by vince on 08/02/15.
 */
public class AsyncGithubBlogCloner extends AsyncTask<GithubBlog, Integer, Boolean> {

  private final Context context;

  public AsyncGithubBlogCloner(Context context) {
    this.context = context;
  }

  @Override
  protected Boolean doInBackground(GithubBlog... blogs) {
    // we clone each of the Github blogs
    Boolean result = false;
    GitInteraction gitClient = new GitRepository();
    if (new DefaultNetworkChecker().checkNetworkForDownload(context, false)) {
      result = true;
      for (GithubBlog b : blogs) {
        result &= gitClient.clone(b);
      }
    }
    return result;
  }
}

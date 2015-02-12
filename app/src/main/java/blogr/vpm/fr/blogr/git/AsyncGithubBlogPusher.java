package blogr.vpm.fr.blogr.git;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.network.DefaultNetworkChecker;

/**
 * Created by vince on 08/02/15.
 */
public class AsyncGithubBlogPusher extends AsyncTask<GithubBlog, Integer, Boolean> {

  private final Context context;

  private final String username;

  private final String password;

  public AsyncGithubBlogPusher(Context context, String username, String password) {
    this.context = context;
    this.username = username;
    this.password = password;
  }

  @Override
  protected Boolean doInBackground(GithubBlog... blogs) {
    // we push each of the Github blogs
    Boolean result = false;
    GitInteraction gitClient = new GitRepository();
    if (new DefaultNetworkChecker().checkNetworkForDownload(context, false)) {
      result = true;
      for (GithubBlog b : blogs) {
        result &= gitClient.push(b, username, password);
      }
    }
    return result;
  }

  @Override
  protected void onPostExecute(Boolean pushed) {
    String result = "Blog update failed";
    if (pushed) {
      result = "Blog updated";
    }
    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    super.onPostExecute(pushed);
  }
}

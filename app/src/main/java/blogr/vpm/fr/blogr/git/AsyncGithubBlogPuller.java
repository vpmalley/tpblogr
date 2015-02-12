package blogr.vpm.fr.blogr.git;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.network.DefaultNetworkChecker;
import blogr.vpm.fr.blogr.persistence.FileBlogManager;

/**
 * Created by vince on 08/02/15.
 *
 * Pulls (synchronizes) all of the github blogs.
 * This should be done at app opening
 * Does not need to be specified in the args. However if it is, only these blogs are pulled.
 */
public class AsyncGithubBlogPuller extends AsyncTask<GithubBlog, Integer, Boolean> {

  private final Context context;

  public AsyncGithubBlogPuller(Context context) {
    this.context = context;
  }

  @Override
  protected Boolean doInBackground(GithubBlog... blogs) {
    // if no blog is passed, we search for blogs locally
    if (0 == blogs.length) {
      List<Blog> allBlogs = new ArrayList<>();
      try {
        allBlogs = new FileBlogManager().retrieveAll();
      } catch (IOException e) {
        Log.w("blogs", "Could not fetch the blogs from file system");
      }
      List<GithubBlog> githubBlogs = new ArrayList<>();

      for (Blog b : allBlogs){
        if (b instanceof GithubBlog){
          githubBlogs.add((GithubBlog) b);
        }
      }
      blogs = githubBlogs.toArray(new GithubBlog[githubBlogs.size()]);
    }

    // we pull each of the Github blogs
    Boolean result = false;
    GitInteraction gitClient = new GitRepository();
    if (new DefaultNetworkChecker().checkNetworkForDownload(context, false)) {
      result = true;
      for (GithubBlog b : blogs) {
        result &= gitClient.pull(b);
      }
    }
    return result;
  }

  @Override
  protected void onPostExecute(Boolean cloned) {
    if (!cloned) {
      Toast.makeText(context, context.getString(R.string.toast_cannot_clone), Toast.LENGTH_LONG).show();
    }
    super.onPostExecute(cloned);
  }
}

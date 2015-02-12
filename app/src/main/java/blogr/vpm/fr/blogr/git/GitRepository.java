package blogr.vpm.fr.blogr.git;

import android.content.Context;
import android.util.Log;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.IOException;

import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.persistence.FileManager;

/**
 * Created by vince on 04/02/15.
 */
public class GitRepository implements GitInteraction {

  private Git git;

  private void initGit(GithubBlog blog){
    try {
      git = Git.open(new FileManager().getFileForBlog(blog));
    } catch (IOException e) {
      Log.w("git", "Issue retrieving git repo " + blog.getTitle() + " due to " + e.toString());
    }
  }

  @Override
  public boolean clone(GithubBlog blog) {
    boolean result = true;
    CloneCommand gitClone = Git.cloneRepository().setBare(false).setBranch("master")
            .setDirectory(new FileManager().getFileForBlog(blog)).setURI(blog.getRepositoryUrl());
    try {
      git = gitClone.call();
    } catch (GitAPIException e) {
      Log.w("git", "Issue cloning " + blog.getRepositoryUrl() + " due to " + e.toString());
      result = false;
    }
    StoredConfig config = git.getRepository().getConfig();
    config.setString("user", null, "name", "vpmalley");
    config.setString("user", null, "email", "vpmalley@gmail.com");
    try {
      config.save();
    } catch (IOException e) {
      Log.w("git", "Could not save user config");
    }
    return result;
  }

  @Override
  public boolean pull(GithubBlog blog) {
    boolean result = true;
    if (git == null) {
      initGit(blog);
    }
    PullCommand pull = git.pull();
    PullResult pullResult = null;
    try {
      pullResult = pull.call();
      result = pullResult.isSuccessful();
    } catch (GitAPIException e) {
      Log.w("git", "Issue pulling " + blog.getRepositoryUrl() + " due to " + e.toString());
      result = false;
    }
    return result;
  }

  @Override
  public boolean add(GithubBlog blog, String postPath, Context context) {
    boolean result = true;
    if (git == null) {
      initGit(blog);
    }
    AddCommand add = git.add().addFilepattern(postPath);
    try {
      add.call();
    } catch (GitAPIException e) {
      Log.w("git", "Issue adding " + postPath + " due to " + e.toString());
      result = false;
    }
    return result;
  }

  @Override
  public boolean commit(GithubBlog blog, String message) {
    boolean result = true;
    if (git == null) {
      initGit(blog);
    }
    CommitCommand commit = git.commit().setAll(true).setMessage(message);
    try {
      commit.call();
    } catch (GitAPIException e) {
      Log.w("git", "Issue committing " + blog.getRepositoryUrl() + " due to " + e.toString());
      result = false;
    }
    return result;
  }

  @Override
  public boolean push(GithubBlog blog) {
    boolean result = true;
    if (git == null) {
      initGit(blog);
    }
    PushCommand push = git.push();
    push.setCredentialsProvider(new UsernamePasswordCredentialsProvider(blog.getTitle().replace(GithubBlog.REPO_SUFFIX, ""), "password"));
    try {
      push.call();
    } catch (GitAPIException e) {
      Log.w("git", "Issue pushing to " + blog.getRepositoryUrl() + " due to " + e.toString());
      result = false;
    }
    return result;
  }
}

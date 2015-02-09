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

import java.io.IOException;

import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.bean.Post;
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
  public boolean add(GithubBlog blog, Post post, Context context) {
    boolean result = true;
    if (git == null) {
      initGit(blog);
    }
    AddCommand add = git.add().addFilepattern(new FileManager().getFileForPost(context, post).getAbsolutePath());
    try {
      add.call();
    } catch (GitAPIException e) {
      Log.w("git", "Issue adding " + blog.getRepositoryUrl() + " due to " + e.toString());
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
    try {
      push.call();
    } catch (GitAPIException e) {
      Log.w("git", "Issue pushing to " + blog.getRepositoryUrl() + " due to " + e.toString());
      result = false;
    }
    return result;
  }
}

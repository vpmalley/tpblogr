package blogr.vpm.fr.blogr.git;

import android.content.Context;

import blogr.vpm.fr.blogr.bean.GithubBlog;

/**
 * Created by vince on 04/02/15.
 *
 * Common operations on a Git repository:
 * clone, pull and push
 */
public interface GitInteraction {

  boolean clone(GithubBlog blog);

  boolean pull(GithubBlog blog);

  boolean add(GithubBlog blog, String postPath, Context context);

  boolean commit(GithubBlog blog, String message);

  boolean push(GithubBlog blog);

}

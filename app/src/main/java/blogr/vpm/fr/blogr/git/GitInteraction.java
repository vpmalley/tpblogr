package blogr.vpm.fr.blogr.git;

import android.content.Context;

import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 04/02/15.
 *
 * Common operations on a Git repository:
 * clone, pull and push
 */
public interface GitInteraction {

  boolean clone(GithubBlog blog);

  boolean pull(GithubBlog blog);

  boolean add(GithubBlog blog, Post post, Context context);

  boolean commit(GithubBlog blog, String message);

  boolean push(GithubBlog blog);

}

package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.EmailBlog;
import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.bean.TPBlog;

/**
 * Created by vince on 17/10/14.
 */
public class BlogActivity extends Activity {

  public static final String BLOG_KEY = "blog";

  private Fragment blogEditionFragment;

  private Blog blog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_blog);
    //blogEditionFragment = getFragmentManager().findFragmentById(R.id.blogEditionFragment);
    setTitle("Blog");
    getActionBar().setDisplayHomeAsUpEnabled(true);


    Intent i = getIntent();
    if (i.hasExtra(BLOG_KEY)) {
      blog = i.getParcelableExtra(BLOG_KEY);
    }

    if (blog != null) {
      if (blog instanceof TPBlog) {
        blogEditionFragment = new EmailBlogEditionFragment();
        setTitle(getString(R.string.tp_blog));
      } else if (blog instanceof EmailBlog) {
        blogEditionFragment = new EmailBlogEditionFragment();
        setTitle(getString(R.string.email_blog));
      } else if (blog instanceof GithubBlog) {
        blogEditionFragment = new GithubBlogEditionFragment();
        setTitle(getString(R.string.github_blog));
      }
      Bundle args = new Bundle();
      args.putParcelable(BLOG_KEY, blog);
      blogEditionFragment.setArguments(args);
      getFragmentManager().beginTransaction().add(R.id.blog_activity, blogEditionFragment).commit();
    }


  }
}

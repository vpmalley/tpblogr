package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 17/10/14.
 */
public class PostListActivity extends Activity implements PostSelectionListener, DialogDismissedListener {

  public static final int NEW_BLOG_REQ = 1;

  public static final int NEW_POST_REQ = 2;

  private PostListFragment postListFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post_list);
    postListFragment = (PostListFragment) getFragmentManager().findFragmentById(R.id.postListFragment);
    setTitle("");
    getActionBar().setDisplayHomeAsUpEnabled(false); // no parent activity
  }

  @Override
  public void onActionModeFinished(ActionMode mode) {
    postListFragment.onInvalidatedModel();
  }

  @Override
  public void onPostSelection(Post post) {
    Intent i = new Intent(this, PostEditionActivity.class);
    i.putExtra(Post.INTENT_EXTRA_KEY, post);
    startActivity(i);
  }

  @Override
  public void onDialogDismissed(Blog blog, int requestCode) {
    switch (requestCode){
      case NEW_BLOG_REQ :
        Intent i = new Intent(this, BlogActivity.class);
        i.putExtra(BlogActivity.BLOG_KEY, blog);
        startActivity(i);
        break;
      case NEW_POST_REQ :
        onPostSelection(Post.emptyPost(blog));
        break;
      default: // do nothing
        break;
    }
  }
}

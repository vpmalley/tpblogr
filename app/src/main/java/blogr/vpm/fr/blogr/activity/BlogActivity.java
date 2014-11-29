package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.ActionMode;

import blogr.vpm.fr.blogr.R;

/**
 * Created by vince on 17/10/14.
 */
public class BlogActivity extends Activity {

  private PostListFragment blogEditionFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_blog);
    blogEditionFragment = (PostListFragment) getFragmentManager().findFragmentById(R.id.blogEditionFragment);
    setTitle("Blog");
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public void onActionModeFinished(ActionMode mode) {
    blogEditionFragment.onInvalidatedModel();
  }

}

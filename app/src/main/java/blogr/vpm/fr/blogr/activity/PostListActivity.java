package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 17/10/14.
 */
public class PostListActivity extends Activity implements PostSelectionListener {

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
}

package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.os.Bundle;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 17/10/14.
 */
public class PostEditionActivity extends Activity {

    private PostEditionFragment postEditionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_edition);
        postEditionFragment = (PostEditionFragment) getFragmentManager().findFragmentById(R.id.postEditionFragment);
        setTitle("");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(Post.INTENT_EXTRA_KEY)) {
            Post post = (Post) getIntent().getExtras().get(Post.INTENT_EXTRA_KEY);
            postEditionFragment.editPost(post);
        }
    }
}

package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.os.Bundle;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 13/03/15.
 */
public class PostMetadataActivity extends Activity {

  private PostMetadataFragment postMetadataFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post_metadata);
    postMetadataFragment = (PostMetadataFragment) getFragmentManager().findFragmentById(R.id.postMetadataFragment);
    setTitle("");

    if (getIntent().hasExtra(Post.INTENT_EXTRA_KEY)) {
      Post post = (Post) getIntent().getExtras().get(Post.INTENT_EXTRA_KEY);
      postMetadataFragment.editPost(post);
    }
  }
}

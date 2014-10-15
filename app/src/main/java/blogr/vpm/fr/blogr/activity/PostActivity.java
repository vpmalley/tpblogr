package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.ActionMode;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;


public class PostActivity extends Activity implements PostSelectionListener{

    private PostEditionFragment postEditionFragment;
    private Fragment postListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postEditionFragment = (PostEditionFragment) getFragmentManager().findFragmentById(R.id.postEditionFragment);
        postListFragment = getFragmentManager().findFragmentById(R.id.postListFragment);
        FragmentTransaction displayPostList = getFragmentManager().beginTransaction();
        displayPostList.hide(postEditionFragment);
        displayPostList.commit();
        setTitle("");
    }

    @Override
    public boolean onNavigateUp() {
        getFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onPostSelection(Post post) {
        postEditionFragment.editPost(post);
        displayPostEdition();
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        postListFragment.onResume();
    }

    /**
     * Displays the fragment to edit a post and hides the fragment with the list of posts
     */
    private void displayPostEdition() {
        FragmentTransaction displayPostEdition = getFragmentManager().beginTransaction();
        displayPostEdition.hide(postListFragment);
        displayPostEdition.show(postEditionFragment);
        displayPostEdition.addToBackStack("displayPostEdition");
        displayPostEdition.commit();
    }
}

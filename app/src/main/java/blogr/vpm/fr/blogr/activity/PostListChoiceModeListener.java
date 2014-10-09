package blogr.vpm.fr.blogr.activity;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.persistence.FilePostSaver;
import blogr.vpm.fr.blogr.persistence.PostSaver;

/**
 * Created by vincent on 08/10/14.
 */
public class PostListChoiceModeListener implements AbsListView.MultiChoiceModeListener {

    PostSaver postSaver;

    List<Post> selectedPosts;

    List<Post> posts;

    public PostListChoiceModeListener(Context context, List<Post> posts) {
        this.postSaver = new FilePostSaver(context);
        this.selectedPosts = new ArrayList<Post>();
        this.posts = posts;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.posts_context, menu);
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (R.id.action_delete == menuItem.getItemId()){
            for (Post selectedPost : selectedPosts){
                postSaver.delete(selectedPost);
            }
            actionMode.finish();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        selectedPosts.clear();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
        selectedPosts.add(posts.get(i));
    }
}

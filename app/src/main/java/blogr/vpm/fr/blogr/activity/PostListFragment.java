package blogr.vpm.fr.blogr.activity;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.persistence.FilePostRetriever;
import blogr.vpm.fr.blogr.persistence.PostRetriever;

/**
 * Created by vincent on 08/10/14.
 */
public class PostListFragment extends ListFragment {

    private PostRetriever retriever;

    private PostSelectionListener postSelectionListener;

    private List<Post> posts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // init services
        retriever = new FilePostRetriever(getActivity());
        postSelectionListener = (PostSelectionListener) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        posts = retriever.retrieveAll();
        List<String> postItems = new ArrayList<String>();
        for (Post post : posts) {
            postItems.add(post.getTitle());
        }
        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.post_item, postItems));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.posts, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), PreferenceActivity.class));
            return true;
        }
        else if (id == R.id.action_new) {
            postSelectionListener.onPostSelection(Post.emptyPost());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Post selectedPost = (Post) posts.get(position);
        postSelectionListener.onPostSelection(selectedPost);
    }
}

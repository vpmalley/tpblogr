package blogr.vpm.fr.blogr.activity;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.insertion.DefaultInserter;
import blogr.vpm.fr.blogr.insertion.Inserter;
import blogr.vpm.fr.blogr.location.LatLongTagProvider;
import blogr.vpm.fr.blogr.location.LocationProvider;
import blogr.vpm.fr.blogr.location.PlayServicesLocationProvider;
import blogr.vpm.fr.blogr.persistence.FilePostSaver;
import blogr.vpm.fr.blogr.persistence.PostSaver;
import blogr.vpm.fr.blogr.publish.PostPublisher;
import blogr.vpm.fr.blogr.publish.TPPostPublisher;

/**
 * Created by vincent on 07/10/14.
 */
public class PostEditionFragment extends Fragment{

    private PostPublisher publisher;

    private PostSaver saver;

    private LocationProvider locationProvider;

    private Blog currentBlog;

    private Post currentPost;

    private EditText contentField;

    private EditText titleField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // init services
        publisher = new TPPostPublisher(getActivity());
        saver = new FilePostSaver(getActivity());
        locationProvider = new PlayServicesLocationProvider(getActivity());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentBlog = new Blog(prefs.getString("pref_blog_name", ""), prefs.getString("pref_blog_email", ""));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container);
    }

    @Override
    public void onResume() {
        super.onResume();
        contentField = (EditText) getView().findViewById(R.id.postContent);
        refreshViewFromPost();
        locationProvider.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        refreshPostFromView();
        saveCurrentPost();
        locationProvider.disconnect();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.post, menu);
        titleField = (EditText) menu.findItem(R.id.action_title).getActionView();
        refreshViewFromPost();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), AllPreferencesActivity.class));
            return true;
        }
        else if (id == R.id.action_publish) {
            publisher.publish(currentBlog, new Post(titleField.getText().toString(), contentField.getText().toString()));
            return true;
        }
        else if (id == R.id.action_insert_location) {
            Inserter locationInserter = new DefaultInserter();
            locationInserter.insert(contentField, new LatLongTagProvider(locationProvider));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the current post with given instance and refreshes the view
     * @param post The new post to edit
     */
    public void editPost(Post post){
        currentPost = post;
        refreshViewFromPost();
    }

    /**
     * Refreshes the view with the current Post
     */
    private void refreshViewFromPost(){
        if (currentPost != null) {
            if (titleField != null) {
                titleField.setText(currentPost.getTitle());
            }
            if (contentField != null) {
                contentField.setText(currentPost.getContent());
            }
        }
    }

    /**
     * Refreshes the Post with the current view elements
     */
    private void refreshPostFromView(){
        if ((titleField != null) && (contentField != null)) {
            currentPost = new Post(titleField.getText().toString(), contentField.getText().toString());
        }
    }

    /**
     * Saves the post built from the view
     */
    private void saveCurrentPost() {
        // save only if non-empty post
        if ((currentPost != null) && (!"".equals(currentPost.getTitle())) && (!"".equals(currentPost.getTitle()))) {
            saver.persist(currentPost);
        }
    }
}

package blogr.vpm.fr.blogr.activity;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.PhotoList;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.apis.flickr.FlickrJAndroidProvider;
import blogr.vpm.fr.blogr.apis.flickr.FlickrProvider;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;
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

    private Blog currentBlog;

    private Post currentPost;

    private EditText contentField;

    private EditText titleField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Log.d("postF", "creating fragment");
        // init services
        publisher = new TPPostPublisher(getActivity());
        saver = new FilePostSaver(getActivity());

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
    }

    @Override
    public void onPause() {
        super.onPause();
        refreshPostFromView();
        saveCurrentPost();
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
            startActivity(new Intent(getActivity(), PreferenceActivity.class));
            return true;
        }
        else if (id == R.id.action_publish) {
            publisher.publish(currentBlog, new Post(titleField.getText().toString(), contentField.getText().toString()));
            return true;
        }
        else if (id == R.id.action_insert) {
            Log.d("flickr", "will call F");
            FlickrProvider flickrP = new FlickrJAndroidProvider(getActivity());
            PhotoList photos = flickrP.getUserPhotos("VinceTraveller", 5);
            Toast.makeText(getActivity(), photos.get(0).getTitle(), Toast.LENGTH_SHORT).show();
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
        if ((!"".equals(currentPost.getTitle())) && (!"".equals(currentPost.getTitle()))) {
            saver.persist(currentPost);
        }
    }
}

package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import blogr.vpm.fr.blogr.persistence.FilePostSaver;
import blogr.vpm.fr.blogr.persistence.PostSaver;
import blogr.vpm.fr.blogr.publish.PostPublisher;
import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.publish.TPPostPublisher;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;


public class PostActivity extends Activity {

    private PostPublisher publisher;

    private PostSaver saver;

    private Blog currentBlog;

    private Post currentPost;

    private EditText contentField;

    private EditText titleField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setTitle("");

        // init services
        publisher = new TPPostPublisher(this);
        saver = new FilePostSaver(this);

        contentField = (EditText) findViewById(R.id.postContent);
        refreshPost();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        currentBlog = new Blog(prefs.getString("pref_blog_name", ""), prefs.getString("pref_blog_email", ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Post> posts = saver.retrieveAll();
        if (!posts.isEmpty()){
            currentPost = posts.get(0);
            refreshPost();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePost();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post, menu);
        titleField = (EditText) menu.findItem(R.id.action_title).getActionView();
        refreshPost();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, PreferenceActivity.class));
            return true;
        }
        if (id == R.id.action_publish) {
            publisher.publish(currentBlog, new Post(titleField.getText().toString(), contentField.getText().toString()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Refreshes the view with the current Post
     */
    private void refreshPost(){
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
     * Saves the post built from the view
     */
    private void savePost() {
        Post post = new Post(titleField.getText().toString(), contentField.getText().toString());
        saver.persist(post);
    }
}

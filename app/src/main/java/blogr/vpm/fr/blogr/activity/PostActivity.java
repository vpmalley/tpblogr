package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import blogr.vpm.fr.blogr.publish.PostPublisher;
import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.publish.TPPostPublisher;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;


public class PostActivity extends Activity {

    private PostPublisher publisher;

    private Blog currentBlog;

    private EditText contentField;

    private EditText titleField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setTitle("");

        publisher = new TPPostPublisher(this);

        contentField = (EditText) findViewById(R.id.postContent);

        // get current blog from SharedPreferences
        currentBlog = new Blog("myblog@gmail.com");

        // saved instance state!

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post, menu);
        titleField = (EditText) menu.findItem(R.id.action_title).getActionView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_publish) {
            publisher.publish(currentBlog, new Post(titleField.getText().toString(), contentField.getText().toString()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

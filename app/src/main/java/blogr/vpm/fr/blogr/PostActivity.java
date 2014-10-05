package blogr.vpm.fr.blogr;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class PostActivity extends Activity {

    private PostPublisher publisher;

    private Blog currentBlog;

    private EditText contentField;

    private EditText titleField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // get current blog from SharedPreferences
        publisher = new TPPostPublisher(this);

        contentField = (EditText) findViewById(R.id.postContent);

        //titleField = (EditText) findViewById(R.id.postContent);

        currentBlog = new Blog("grosergio@gmail.com");

        // saved instance state!

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_publish) {
            publisher.publish(currentBlog, new Post("New Post", contentField.getText().toString()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by vincent on 05/10/14.
 */
public class PreferenceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Blog Settings");
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new BlogPreferenceFragment())
                .commit();
    }
}

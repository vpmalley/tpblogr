package blogr.vpm.fr.blogr.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import blogr.vpm.fr.blogr.R;

/**
 * Created by vincent on 05/10/14.
 */
public class BlogPreferenceFragment extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.blogpreferences);
    }

}

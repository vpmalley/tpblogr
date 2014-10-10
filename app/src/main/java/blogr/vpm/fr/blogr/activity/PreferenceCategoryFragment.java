package blogr.vpm.fr.blogr.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import blogr.vpm.fr.blogr.R;

/**
 * Created by vincent on 05/10/14.
 */
public class PreferenceCategoryFragment extends PreferenceFragment{

    public static final String CATEGORY = "category";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ("permissions".equals(getArguments().getString(CATEGORY))) {
            addPreferencesFromResource(R.xml.permissionspreferences);
        } else if ("blog".equals(getArguments().getString(CATEGORY))) {
            addPreferencesFromResource(R.xml.blogpreferences);
        }
    }

}

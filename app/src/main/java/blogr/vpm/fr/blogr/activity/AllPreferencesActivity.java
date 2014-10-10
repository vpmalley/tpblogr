package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;

import blogr.vpm.fr.blogr.R;

/**
 * Created by vincent on 05/10/14.
 */
public class AllPreferencesActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<android.preference.PreferenceActivity.Header> target) {
        loadHeadersFromResource(R.xml.preferenceheaders, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        // any preference screen should be implemented as defined in the headers section of doc.
        if ("blogr.vpm.fr.blogr.activity.PreferenceCategoryFragment".equals(fragmentName)){
            return true;
        }
        return false;
    }
}

package blogr.vpm.fr.blogr.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import blogr.vpm.fr.blogr.publish.PostPublisher;
import blogr.vpm.fr.blogr.publish.StdEmailPostPublisher;
import blogr.vpm.fr.blogr.publish.TPPostPublisher;

/**
 * Created by vince on 16/10/14.
 */
public class PostPublisherPreferencesProvider implements PostPublisherProvider {
    @Override
    public PostPublisher getService(Context context) {
        PostPublisher serviceInstance;
        String tpBlogValue = context.getResources().getString(R.string.pref_blog_service_value_blog_tp);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String prefValue = prefs.getString("pref_blog_service", tpBlogValue);
        if (tpBlogValue.equals(prefValue)){
            serviceInstance = new TPPostPublisher(context);
        } else if (context.getResources().getString(R.string.pref_blog_service_value_email_std).equals(prefValue)) {
            serviceInstance = new StdEmailPostPublisher(context);
        } else {
            throw new IllegalStateException("Unexpected publication service specified");
        }
        return serviceInstance;
    }
}

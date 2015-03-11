package blogr.vpm.fr.blogr.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.insertion.SingleTagProvider;
import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;
import blogr.vpm.fr.blogr.picture.PictureMdTagsProvider;
import blogr.vpm.fr.blogr.picture.PictureTpTagsProvider;
import blogr.vpm.fr.blogr.publish.PostPublisher;
import blogr.vpm.fr.blogr.publish.StdEmailPostPublisher;
import blogr.vpm.fr.blogr.publish.TPPostPublisher;

/**
 * Created by vince on 16/10/14.
 */
public class PostPublishingPreferencesProvider implements PostPublishingServiceProvider {

  private String tpBlog;
  private String stdEmail;

  private void initPreferencesValues(Context context) {
    tpBlog = context.getResources().getString(R.string.pref_blog_service_value_blog_tp);
    stdEmail = context.getResources().getString(R.string.pref_blog_service_value_email_std);
  }

  @Override
  public PostPublisher getPublisherService(Context context) {
    PostPublisher serviceInstance;
    initPreferencesValues(context);
    String prefValue = getPreferredPublisher(context);
    if (tpBlog.equals(prefValue)) {
      serviceInstance = new TPPostPublisher(context);
    } else if (stdEmail.equals(prefValue)) {
      serviceInstance = new StdEmailPostPublisher(context);
    } else {
      throw new IllegalStateException("Unexpected publication service specified");
    }
    return serviceInstance;
  }

  @Override
  public SurroundingTagsProvider getPictureTagsProvider(Context context, String pictureUrl) {
    SurroundingTagsProvider serviceInstance;
    initPreferencesValues(context);
    String prefValue = getPreferredPublisher(context);
    if (tpBlog.equals(prefValue)) {
      serviceInstance = new PictureTpTagsProvider(pictureUrl);
    } else if (stdEmail.equals(prefValue)) {
      serviceInstance = new PictureMdTagsProvider(pictureUrl);
    } else {
      throw new IllegalStateException("Unexpected publication service specified");
    }
    return serviceInstance;
  }

  @Override
  public boolean hasMetadataProvider() {
    return false;
  }

  @Override
  public SingleTagProvider getMetadataProvider(Post post) {
    throw new UnsupportedOperationException();
  }

  /**
   * Provides the value for the preferred publisher, to be used to decide which service to instantiate
   *
   * @param context the standard Android Context
   * @return the value for the publisher, i.e. one of the pref_blog_service_value_* string values
   */
  private String getPreferredPublisher(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString("pref_blog_service", context.getResources().getString(R.string.pref_blog_service_value_blog_tp));
  }
}

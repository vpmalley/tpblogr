package blogr.vpm.fr.blogr.service;

import android.content.Context;

import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;
import blogr.vpm.fr.blogr.publish.PostPublisher;

/**
 * Created by vince on 16/10/14.
 * <p/>
 * Provides a service to publish a post.
 */
public interface PostPublishingServiceProvider {

  /**
   * Provides a service to publish a post.
   *
   * @param context The Android Context, used to determine and instantiate the right service.
   * @return the instantiated PostPublisher service
   */
  PostPublisher getPublisherService(Context context);

  /**
   * Provides a service to provide tags for a picture.
   *
   * @param context    The Android Context, used to determine and instantiate the right service.
   * @param pictureUrl the url of the picture to insert
   * @return the instantiated SurroundingTagsProvider for pictures
   */
  SurroundingTagsProvider getPictureTagsProvider(Context context, String pictureUrl);
}

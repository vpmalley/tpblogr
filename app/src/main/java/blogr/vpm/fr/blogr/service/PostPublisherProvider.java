package blogr.vpm.fr.blogr.service;

import android.content.Context;

import blogr.vpm.fr.blogr.publish.PostPublisher;

/**
 * Created by vince on 16/10/14.
 *
 * Provides a service to publish a post.
 */
public interface PostPublisherProvider {

    /**
     * Provides a service to publish a post.
     * @param context The Android Context, used to determine and instantiate the right service.
     * @return the instantiated PostPublisher service
     */
    PostPublisher getService(Context context);
}

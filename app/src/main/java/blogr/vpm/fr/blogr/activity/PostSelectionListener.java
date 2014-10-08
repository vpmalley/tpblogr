package blogr.vpm.fr.blogr.activity;

import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 08/10/14.
 */
public interface PostSelectionListener {

    /**
     * Selects a post and calls listener of this event.
     * @param post selected post
     */
    void onPostSelection(Post post);
}

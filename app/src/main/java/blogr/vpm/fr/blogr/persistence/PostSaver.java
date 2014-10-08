package blogr.vpm.fr.blogr.persistence;

import java.util.List;

import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 06/10/14.
 */
public interface PostSaver {

    /**
     * Persists a blog post to be retrieved later
     * @param post the post to save
     * @return whether it has been persisted successfully
     */
    boolean persist(Post post);

}

package blogr.vpm.fr.blogr.picture;

/**
 * Created by vince on 21/10/14.
 *
 * Listener to a picture being chosen by user, to be inserted in the post
 */
public interface PicturePickedListener {

    /**
     * Called when a user picked a picture among a list, and that should be inserted
     * @param picUrl
     */
    void onPicturePicked(String picUrl);
}

package blogr.vpm.fr.blogr.picture;

/**
 * Created by vince on 21/10/14.
 * <p/>
 * Listener to a picture being chosen by user, to be inserted in the post
 */
public interface PicturePickedListener {

  /**
   * Called when a user picked a picture among a list, and this picture's url should be inserted
   *
   * @param picUrl the url to the picture file to be inserted
   */
  void onPicturePicked(String picUrl);
}

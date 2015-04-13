package blogr.vpm.fr.blogr.picture;

import android.widget.ImageView;

/**
 * Created by vince on 13/04/15.
 * <p/>
 * Pictures should offer some access to their content
 */
public interface Picture {

  /**
   * A url to load the picture or to insert it in a post
   *
   * @return
   */
  String getUrlForInsertion();

  /**
   * To display the picture, use this method
   *
   * @param view
   */
  void displayPicture(ImageView view);

  /**
   * The title for this picture
   *
   * @return
   */
  String getTitle();

  /**
   * The description for this picture
   *
   * @return
   */
  String getDescription();

  /**
   * Whether the picture exists only online and should be uploaded at publication
   *
   * @return
   */
  boolean shouldBeUploaded();

  /**
   * Uploads the picture if it should be. This should be called at publication.
   *
   * @pre {#shouldBeUploaded} should be checked before.
   */
  void upload();
}

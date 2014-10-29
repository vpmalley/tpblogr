package blogr.vpm.fr.blogr.apis.flickr;

import com.googlecode.flickrjandroid.photos.PhotoList;

/**
 * Created by vincent on 08/10/14.
 * <p/>
 * All content provided by Flickr
 */
public interface FlickrProvider {

  /**
   * Retrieves the n first photos of user identified by its username
   *
   * @param username the username to identify the user
   * @param count    the number of pictures to display
   * @return the List of photos
   */
  PhotoList getUserPhotos(String username, int count);
}

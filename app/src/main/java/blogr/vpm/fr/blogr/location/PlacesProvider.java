package blogr.vpm.fr.blogr.location;

import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 20/03/15.
 */
public interface PlacesProvider {

  /**
   * Acquires the location, if positioning is allowed, and inserts it in the given post.
   * If the device is online, a pop-up will appear and suggest a list of addresses to choose from
   * @param post the post to add location to
   * Note: This will display Toast messages and other UI components. Do not call from an asynchronous task
   */
  void acquireAndInsertLocation(Post post);

  /**
   * If a Geocoder is present in the device and the device is online, it will search for the placeName.
   * A pop-up will appear and suggest a list of addresses to choose from
   * @param post the post to add location to
   * @param placeName the name of the place to search for
   * Note: This will display Toast messages and other UI components. Do not call from an asynchronous task
   */
  void searchAndInsertAddress(Post post, String placeName);
}

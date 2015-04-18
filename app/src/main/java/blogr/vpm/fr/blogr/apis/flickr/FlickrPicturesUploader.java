package blogr.vpm.fr.blogr.apis.flickr;

import blogr.vpm.fr.blogr.picture.Picture;

/**
 * Created by vince on 17/04/15.
 */
public interface FlickrPicturesUploader {

  /**
   * Uploads the picture to Flickr. This uses the UI to display authorization request
   *
   * @param picture the picture to upload
   * @return the uploaded picture, with Flickr information
   */
  void uploadPicture(Picture picture);
}

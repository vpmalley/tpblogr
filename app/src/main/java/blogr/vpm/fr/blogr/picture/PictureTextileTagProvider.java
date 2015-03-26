package blogr.vpm.fr.blogr.picture;

import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;
import blogr.vpm.fr.blogr.insertion.SingleTagProvider;

/**
 * Created by vince on 26/03/15.
 */
public class PictureTextileTagProvider implements SingleTagProvider {

  private final ParcelableFlickrPhoto picture;

  public PictureTextileTagProvider(ParcelableFlickrPhoto picture) {
    this.picture = picture;
  }

  @Override
  public String getTag() {
    StringBuilder tagBuilder = new StringBuilder();

    tagBuilder.append("!");
    tagBuilder.append(picture.getLargeSizeUrl());
    tagBuilder.append("(");
    tagBuilder.append(picture.getTitle());
    tagBuilder.append(")!");
    return tagBuilder.toString();
  }
}

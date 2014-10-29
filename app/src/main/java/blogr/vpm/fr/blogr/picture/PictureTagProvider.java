package blogr.vpm.fr.blogr.picture;

import android.content.Context;

import blogr.vpm.fr.blogr.insertion.SingleTagProvider;

/**
 * Created by vincent on 16/10/14.
 * <p/>
 * Provides a tag for a picture from its url. This url should be publicly accessible.
 */
public class PictureTagProvider implements SingleTagProvider {

  private final Context context;

  private final String pictureUrl;

  public PictureTagProvider(Context context, String pictureUrl) {
    this.context = context;
    this.pictureUrl = pictureUrl;
  }

  @Override
  public String getTag() {
    StringBuilder picBuilder = new StringBuilder();
    picBuilder.append("[img=");
    picBuilder.append(pictureUrl);
    picBuilder.append("]");
    return picBuilder.toString();
  }
}

package blogr.vpm.fr.blogr.bean;

import android.content.Context;

import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;
import blogr.vpm.fr.blogr.picture.PictureTpTagsProvider;

/**
 * Created by vincent on 29/08/14.
 */
public class TPBlog extends EmailBlog {

  public TPBlog(String title, String recipientEmail) {
    super(title, recipientEmail);
  }

  @Override
  public SurroundingTagsProvider getPictureTagsProvider(Context context, String pictureUrl) {
    return new PictureTpTagsProvider(pictureUrl);
  }

}

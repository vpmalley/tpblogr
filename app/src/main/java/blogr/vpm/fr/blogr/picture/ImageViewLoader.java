package blogr.vpm.fr.blogr.picture;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 23/10/14.
 */
public class ImageViewLoader implements PictureLoadedListener {

  private final int position;

  private final ImageView pictureView;

  private final List<Bitmap> bitmaps;

  public ImageViewLoader(List<Bitmap> bitmaps, int position, ImageView pictureView) {
    this.position = position;
    this.pictureView = pictureView;
    this.bitmaps = bitmaps;
  }

  @Override
  public void onPictureLoaded(Bitmap pictureBitmap) {
    pictureView.setImageBitmap(pictureBitmap);
    bitmaps.add(position, pictureBitmap);
  }
}

package blogr.vpm.fr.blogr.picture;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by vince on 13/04/15.
 */
public class LocalPicture implements Picture {

  private final Uri localUri;

  private String title;

  private String description;

  public LocalPicture(Uri localUri) {
    this.localUri = localUri;
    this.title = "";
    this.description = "";
  }

  @Override
  public String getUrlForInsertion() {
    return null;
  }

  @Override
  public void displayPicture(ImageView view) {
    Picasso.with(view.getContext()).load(localUri).into(view);
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public boolean shouldBeUploaded() {
    return true;
  }

  @Override
  public void upload() {
    // TODO
    // retrieve the picture with uri
    // upload to Flickr or other service
  }

  @Override
  public String toString() {
    String description = "untitled";
    if (!getTitle().isEmpty()) {
      description = getTitle();
    } else if (!getDescription().isEmpty()) {
      description = getDescription();
    }
    return description;
  }
}

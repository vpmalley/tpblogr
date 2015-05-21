package blogr.vpm.fr.blogr.picture;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;

/**
 * Created by vince on 13/04/15.
 * <p/>
 * Pictures should offer some access to their content
 */
public interface Picture extends Parcelable {

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
   * @param context
   * @return the uploaded picture
   */
  Picture upload(Activity context);

  Parcelable.Creator<Picture> CREATOR
      = new Parcelable.Creator<Picture>() {

    @Override
    public Picture createFromParcel(Parcel parcel) {
      String className = parcel.readString();
      Picture p;
      if (LocalPicture.class.getCanonicalName().equals(className)) {
        p = LocalPicture.CREATOR.createFromParcel(parcel);
      } else {
        p = (Picture) ParcelableFlickrPhoto.CREATOR.createFromParcel(parcel);
      }
      return p;
    }

    @Override
    public Picture[] newArray(int size) {
      return new Picture[size];
    }
  };
}

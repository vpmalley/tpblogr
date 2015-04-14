package blogr.vpm.fr.blogr.picture;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.squareup.picasso.Picasso;

/**
 * Created by vince on 13/04/15.
 *
 * A representation of a picture stored on the device
 */
public class LocalPicture implements Picture {

  private static final String TITLE_KEY = "title";
  private static final String DESC_KEY = "description";
  private static final String URI_KEY = "localUri";
  public static final String UNTITLED = "untitled";

  @Expose
  private final Uri localUri;

  @Expose
  private String title;

  @Expose
  private String description;

  public LocalPicture(Uri localUri) {
    this.localUri = localUri;
    this.title = UNTITLED;
    this.description = UNTITLED;
  }

  public Uri getLocalUri() {
    return localUri;
  }

  @Override
  public String getUrlForInsertion() {
    return "placeholder-" + localUri.toString();
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
    // replace the placehoders?
  }

  @Override
  public String toString() {
    String description = UNTITLED;
    if (!getTitle().isEmpty()) {
      description = getTitle();
    } else if (!getDescription().isEmpty()) {
      description = getDescription();
    }
    return description;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(LocalPicture.class.getCanonicalName());
    Bundle b = new Bundle();
    b.putString(TITLE_KEY, title);
    b.putString(DESC_KEY, description);
    b.putParcelable(URI_KEY, localUri);
    parcel.writeBundle(b);
  }

  private LocalPicture(Parcel in) {
    Bundle b = in.readBundle(LocalPicture.class.getClassLoader());
    // without setting the classloader, it fails on BadParcelableException : ClassNotFoundException when
    // unmarshalling LocalPicture class
    title = b.getString(TITLE_KEY);
    description = b.getString(DESC_KEY);
    localUri = b.getParcelable(URI_KEY);
  }

  public static final Parcelable.Creator<LocalPicture> CREATOR
      = new Parcelable.Creator<LocalPicture>() {
    public LocalPicture createFromParcel(Parcel in) {
      return new LocalPicture(in);
    }

    public LocalPicture[] newArray(int size) {
      return new LocalPicture[size];
    }
  };
}

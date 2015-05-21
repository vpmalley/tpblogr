package blogr.vpm.fr.blogr.picture;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.squareup.picasso.Picasso;

import org.scribe.model.Token;

import blogr.vpm.fr.blogr.apis.flickr.FlickrJPicturesUploader;
import blogr.vpm.fr.blogr.apis.flickr.FlickrOAuthAuthoriser;
import blogr.vpm.fr.blogr.apis.flickr.FlickrOAuthTokenStore;
import blogr.vpm.fr.blogr.apis.flickr.FlickrPicturesUploader;

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
  private static final String UPLOAD_KEY = "upload";

  @Expose
  private final Uri localUri;

  @Expose
  private String title;

  @Expose
  private String description;

  @Expose
  private String uploadPhotoId;

  public LocalPicture(Uri localUri) {
    this.localUri = localUri;
    this.title = UNTITLED;
    this.description = UNTITLED;
    this.uploadPhotoId = "";
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
    Picasso.with(view.getContext()).load(localUri).fit().into(view);
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
    return getUploadPhotoId().isEmpty();
  }

  @Override
  public Picture upload(final Activity activity) {
    Picture uploaded = null;
    FlickrOAuthTokenStore store = new FlickrOAuthTokenStore();
    if (store.hasStoredToken(activity)) {
      Log.d("picture", "hasToken");
      Token token = store.getStoredToken(activity);
      FlickrPicturesUploader picUploader = new FlickrJPicturesUploader(activity, token);
      uploaded = picUploader.uploadPicture(LocalPicture.this);
    } else {

      FlickrOAuthAuthoriser.PostExecution postExecution = new FlickrOAuthAuthoriser.PostExecution() {
        @Override
        public void onPostExecute(Token accessToken) {
          FlickrPicturesUploader picUploader = new FlickrJPicturesUploader(activity, accessToken);
          picUploader.uploadPicture(LocalPicture.this);
        }
      };
      new FlickrOAuthAuthoriser(postExecution).launchAuthorizationFlow(activity);
    }
    return uploaded;
  }

  public String getUploadPhotoId() {
    return uploadPhotoId;
  }

  public void setUploadPhotoId(String uploadPhotoId) {
    this.uploadPhotoId = uploadPhotoId;
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
    b.putString(UPLOAD_KEY, uploadPhotoId);
    parcel.writeBundle(b);
  }

  private LocalPicture(Parcel in) {
    Bundle b = in.readBundle(LocalPicture.class.getClassLoader());
    // without setting the classloader, it fails on BadParcelableException : ClassNotFoundException when
    // unmarshalling LocalPicture class
    title = b.getString(TITLE_KEY);
    description = b.getString(DESC_KEY);
    localUri = b.getParcelable(URI_KEY);
    uploadPhotoId = b.getString(UPLOAD_KEY);
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

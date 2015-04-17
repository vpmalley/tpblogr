package blogr.vpm.fr.blogr.apis.flickr;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.googlecode.flickrjandroid.photos.PhotoList;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.activity.FlickrDialogFragment;
import blogr.vpm.fr.blogr.picture.PicturePickedListener;

/**
 * Created by vincent on 19/10/14.
 */
public class FlickrJAsyncPicturesProvider extends AsyncTask<FlickrJAsyncPicturesProvider.FlickrSearchBean, Integer, PhotoList> implements FlickrProvider {

  private final FlickrProvider delegate;

  private final Activity activity;

  private final PicturePickedListener listener;

  public FlickrJAsyncPicturesProvider(Activity activity, FlickrProvider delegate, PicturePickedListener listener) {
    this.listener = listener;
    this.delegate = delegate;
    this.activity = activity;
  }

  @Override
  public PhotoList getUserPhotos(String username, int count) {
    this.execute(new FlickrSearchBean(username, count));
    return new PhotoList();
  }

  @Override
  protected PhotoList doInBackground(FlickrSearchBean... search) {
    return delegate.getUserPhotos(search[0].username, search[0].count);
  }

  @Override
  protected void onPostExecute(PhotoList pics) {
    if (pics.size() > 0) {
      ParcelableFlickrPhoto[] pPics = getParcelablePictureArray(pics);
      new FlickrDialogFragment().openPicturePicker(activity, pPics, listener);
    } else {
      Toast.makeText(activity, activity.getResources().getString(R.string.no_access_flickr), Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * Transforms the list of pictures into an array of parcelable (i.e. serializable) pictures
   *
   * @param pics the list of pictures from the Flickr API
   * @return an array of parcelable pictures
   */
  private ParcelableFlickrPhoto[] getParcelablePictureArray(PhotoList pics) {
    ParcelableFlickrPhoto[] pPics = new ParcelableFlickrPhoto[pics.size()];
    for (int i = 0; i < pics.size(); i++) {
      pPics[i] = new ParcelableFlickrPhoto(pics.get(i));
    }
    return pPics;
  }

  public class FlickrSearchBean {
    String username;
    int count;

    private FlickrSearchBean(String username, int count) {
      this.username = username;
      this.count = count;
    }
  }
}

package blogr.vpm.fr.blogr.apis.flickr;

import android.content.Context;
import android.util.Log;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;

import org.json.JSONException;

import java.io.IOException;

import blogr.vpm.fr.blogr.R;

/**
 * Created by vincent on 08/10/14.
 *
 * Uses the flickrj-android library to retrieve pictures for a specific user.
 * This must be called from an asynchronous task, this cannot be called on the UI thread.
 */
public class FlickrJPicturesProvider implements FlickrProvider {

  private Context context;

  public FlickrJPicturesProvider(Context context) {
    this.context = context;
  }

  @Override
  public PhotoList getUserPhotos(String username, int count) {
    PhotoList photos = new PhotoList();
    Flickr f = new Flickr(context.getResources().getString(R.string.flickr_api_key));
    try {
      User user = f.getPeopleInterface().findByUsername(username);
      photos = f.getPeopleInterface().getPublicPhotos(user.getId(), count, 1);
    } catch (IOException | JSONException | FlickrException e) {
      Log.d("flickr", e.getMessage());
    }
    return photos;
  }

  public Photo getPhotoForId(String photoId) {
    Photo photo = null;
    Flickr f = new Flickr(context.getResources().getString(R.string.flickr_api_key));
    try {
      photo = f.getPhotosInterface().getInfo(photoId, "");
    } catch (IOException | JSONException | FlickrException e) {
      Log.d("flickr", e.getMessage());
    }
    return photo;
  }
}

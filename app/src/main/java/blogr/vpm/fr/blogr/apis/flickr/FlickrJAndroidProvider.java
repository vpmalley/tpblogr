package blogr.vpm.fr.blogr.apis.flickr;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.PhotoList;

import org.json.JSONException;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;

import blogr.vpm.fr.blogr.R;

/**
 * Created by vincent on 08/10/14.
 */
public class FlickrJAndroidProvider implements FlickrProvider {

    private Context context;

    public FlickrJAndroidProvider(Context context) {
        this.context = context;
    }

    @Override
    public PhotoList getUserPhotos(String username, int count) {
        PhotoList photos = new PhotoList();
        Flickr f = new Flickr(context.getResources().getString(R.string.flickr_api_key));
        try {
            User user = f.getPeopleInterface().findByUsername(username);
            photos = f.getPeopleInterface().getPublicPhotos(user.getId(), count, 1);
        } catch (IOException e) {
            Log.d("flickr", e.getMessage());
        } catch (JSONException e) {
            Log.d("flickr", e.getMessage());
        } catch (FlickrException e) {
            Log.d("flickr", e.getMessage());
        }
        return photos;
    }
}

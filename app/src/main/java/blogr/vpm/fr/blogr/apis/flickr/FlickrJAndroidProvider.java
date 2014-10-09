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
            Log.d("flickr", "start getting user pics");
            User user = f.getPeopleInterface().findByUsername(username);
            photos = f.getPhotosInterface().getContactsPublicPhotos(user.getId(), count, false, false, false);
        } catch (IOException e) {
            Log.d("flickr", e.getMessage());
            //Toast.makeText(context, "Could not retrieve pictures for user " + username, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Log.d("flickr", e.getMessage());
            //Toast.makeText(context, "Could not retrieve pictures for user " + username, Toast.LENGTH_SHORT).show();
        } catch (FlickrException e) {
            Log.d("flickr", e.getMessage());
            //Toast.makeText(context, "Could not retrieve pictures for user " + username, Toast.LENGTH_SHORT).show();
        }
        return photos;
    }
}

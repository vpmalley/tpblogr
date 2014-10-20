package blogr.vpm.fr.blogr.apis.flickr;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.googlecode.flickrjandroid.photos.PhotoList;

import java.util.Collections;

/**
 * Created by vincent on 19/10/14.
 */
public class FlickrJAsyncTaskProvider extends AsyncTask<FlickrJAsyncTaskProvider.FlickrSearchBean, Integer, PhotoList> implements FlickrProvider {

    private static final int MAX_PICS = 20;
    private final FlickrProvider delegate;

    private final Context context;

    public FlickrJAsyncTaskProvider(Context context, FlickrProvider delegate) {
        this.delegate = delegate;
        this.context = context;
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
    protected void onPostExecute(PhotoList photos) {
        Toast.makeText(context, photos.get(0).getTitle(), Toast.LENGTH_SHORT).show();
    }

    private class FlickrSearchBean {
        String username;
        int count;

        private FlickrSearchBean(String username, int count) {
            this.username = username;
            this.count = count;
        }
    }
}

package blogr.vpm.fr.blogr.apis.flickr;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;

import blogr.vpm.fr.blogr.activity.FlickrDialogFragment;

/**
 * Created by vincent on 19/10/14.
 */
public class FlickrJAsyncTaskProvider extends AsyncTask<FlickrJAsyncTaskProvider.FlickrSearchBean, Integer, PhotoList> implements FlickrProvider {

    private final FlickrProvider delegate;

    private final Activity activity;

    public FlickrJAsyncTaskProvider(Activity activity, FlickrProvider delegate) {
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

        String[] picDescriptions = new String[pics.size()];
        for (int i = 0; i < pics.size(); i++){
            picDescriptions[i] = pics.get(i).getTitle();
        }

        DialogFragment flickrFragment = new FlickrDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray(FlickrDialogFragment.ARG_PICS, picDescriptions);
        flickrFragment.setArguments(args);
        flickrFragment.show(activity.getFragmentManager(), "flickrPicker");

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

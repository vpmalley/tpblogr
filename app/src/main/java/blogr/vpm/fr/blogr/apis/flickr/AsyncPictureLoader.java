package blogr.vpm.fr.blogr.apis.flickr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by vincent on 22/10/14.
 *
 * An asynchronous task to download a picture in order to display it in an ImageView.
 */
public class AsyncPictureLoader extends AsyncTask<String, Integer, Bitmap> {

    private final ImageView pictureView;

    public AsyncPictureLoader(ImageView pictureView) {
        this.pictureView = pictureView;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        if (urls.length == 0){
            throw new IllegalArgumentException("only one picture url was expected.");
        }
        String pictureUrl = urls[0];
        InputStream pictureStream = null;
        Bitmap pictureBitmap = null;
        try {
            pictureStream = new URL(pictureUrl).openStream();
            pictureBitmap = BitmapFactory.decodeStream(pictureStream);
        } catch (IOException e) {
            throw new IllegalArgumentException("A well-formatted url was expected.");
        } finally {
            if (pictureStream != null) {
                try {
                    pictureStream.close();
                } catch (IOException e) {
                    Log.e("flickr", e.getMessage());
                }
            }
        }
        return pictureBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap pictureBitmap) {
        pictureView.setImageBitmap(pictureBitmap);
    }
}

package blogr.vpm.fr.blogr.apis.flickr;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.uploader.UploadMetaData;
import com.googlecode.flickrjandroid.uploader.Uploader;

import org.scribe.model.Token;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.picture.LocalPicture;
import blogr.vpm.fr.blogr.picture.Picture;

/**
 * Created by vince on 17/04/15.
 */
public class FlickrJPicturesUploader extends AsyncTask<Picture, Integer, List<Picture>> implements FlickrPicturesUploader {

  private final Context context;

  private final Token accessToken;

  public FlickrJPicturesUploader(Context context, Token accessToken) {
    this.context = context;
    this.accessToken = accessToken;
  }

  @Override
  public void uploadPicture(Picture picture) {
    executeOnExecutor(THREAD_POOL_EXECUTOR, picture);
  }

  @Override
  protected List<Picture> doInBackground(Picture... pictures) {
    RequestContext requestContext = RequestContext.getRequestContext();
    OAuth oauth = new OAuth();
    oauth.setToken(new OAuthToken(accessToken.getToken(), accessToken.getSecret()));

    requestContext.setOAuth(oauth);
    Uploader uploader = new Uploader(context.getString(R.string.flickr_api_key), context.getString(R.string.flickr_api_secret));
    List<String> ticketIds = new ArrayList<>();
    for (Picture pic : pictures) {
      if (pic instanceof LocalPicture) {
        InputStream picStream = null;
        try {
          picStream = context.getContentResolver().openInputStream(((LocalPicture) pic).getLocalUri());
        } catch (FileNotFoundException e) {
          Log.w("inputStream", "failed opening the stream for picture to upload. " + e.toString());
        }

        UploadMetaData uploadMetaData = new UploadMetaData();
        uploadMetaData.setAsync(true);
        uploadMetaData.setTitle(pic.getTitle());
        uploadMetaData.setDescription(pic.getDescription());
        if (picStream != null) {
          try {
            ticketIds.add(uploader.upload(pic.getTitle(), picStream, uploadMetaData));
            Log.d("upload successful", "with ticket id :" + ticketIds.get(ticketIds.size() - 1));
          } catch (IOException e) {
            Log.w("inputStream", "failed uploading picture. " + e.toString());
          } catch (FlickrException e) {
            Log.w("inputStream", "failed uploading picture. " + e.toString());
          } catch (SAXException e) {
            Log.w("inputStream", "failed uploading picture. " + e.toString());
          }
        }
      }
    }

    // TODO gather info about picture
    return new ArrayList<>();
  }
}

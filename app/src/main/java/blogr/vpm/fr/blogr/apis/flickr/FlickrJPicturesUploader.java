package blogr.vpm.fr.blogr.apis.flickr;

import android.content.Context;
import android.util.Log;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.upload.Ticket;
import com.googlecode.flickrjandroid.photos.upload.UploadInterface;
import com.googlecode.flickrjandroid.uploader.UploadMetaData;
import com.googlecode.flickrjandroid.uploader.Uploader;

import org.json.JSONException;
import org.scribe.model.Token;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.picture.LocalPicture;
import blogr.vpm.fr.blogr.picture.Picture;

/**
 * Created by vince on 17/04/15.
 */
public class FlickrJPicturesUploader implements FlickrPicturesUploader {

  private final Context context;

  private final Token accessToken;

  private final List<String> userNotifs = new ArrayList<>();

  public FlickrJPicturesUploader(Context context, Token accessToken) {
    this.context = context;
    this.accessToken = accessToken;
  }

  @Override
  public Picture uploadPicture(Picture picture) {
    Picture uploaded = null;
    List<Picture> pictures = new ArrayList<>(1);
    pictures.add(picture);
    List<String> photoIds = requestUpload(pictures);
    List<Picture> uploadedPics = retrievePictures(photoIds);
    if (!uploadedPics.isEmpty()) {
      uploaded = uploadedPics.get(0);
    }
    return uploaded;
  }

  private List<String> requestUpload(List<Picture> pictures) {
    RequestContext requestContext = RequestContext.getRequestContext();
    OAuth oauth = new OAuth();
    oauth.setToken(new OAuthToken(accessToken.getToken(), accessToken.getSecret()));

    requestContext.setOAuth(oauth);
    Uploader uploader = new Uploader(context.getString(R.string.flickr_api_key), context.getString(R.string.flickr_api_secret));
    List<String> photoIds = new ArrayList<>();
    for (Picture pic : pictures) {
      if (pic instanceof LocalPicture) {
        InputStream picStream = null;
        try {
          picStream = context.getContentResolver().openInputStream(((LocalPicture) pic).getLocalUri());
        } catch (FileNotFoundException e) {
          Log.w("inputStream", "failed opening the stream for picture to upload. " + e.toString());
        }

        UploadMetaData uploadMetaData = new UploadMetaData();
        uploadMetaData.setAsync(false);
        uploadMetaData.setTitle(pic.getTitle());
        uploadMetaData.setDescription(pic.getDescription());
        if (picStream != null) {
          try {
            String photoId = uploader.upload(pic.getTitle(), picStream, uploadMetaData);
            photoIds.add(photoId);
            ((LocalPicture) pic).setUploadPhotoId(photoId);
            Log.d("upload successful", "with photo id :" + photoIds.get(photoIds.size() - 1));
          } catch (IOException | FlickrException | SAXException e) {
            Log.w("inputStream", "failed uploading picture. " + e.toString());
          }
        }
      }
    }
    return photoIds;
  }

  private List<Picture> retrievePictures(List<String> photoIds) {
    List<Picture> uploadedPics = new ArrayList<>();
    FlickrJPicturesProvider picturesProvider = new FlickrJPicturesProvider(context);
    for (String photoId : photoIds) {
      Photo p = picturesProvider.getPhotoForId(photoId);
      uploadedPics.add(new ParcelableFlickrPhoto(p));
    }
    return uploadedPics;
  }

  private List<Picture> requestPictureInformation(List<String> ticketIds) {
    List<Picture> pictures = new ArrayList<>();
    RequestContext requestContext = RequestContext.getRequestContext();
    OAuth oauth = new OAuth();
    oauth.setToken(new OAuthToken(accessToken.getToken(), accessToken.getSecret()));
    requestContext.setOAuth(oauth);

    try {
      UploadInterface ui = new UploadInterface(context.getString(R.string.flickr_api_key), context.getString(R.string.flickr_api_secret), new REST("api.flickr.com/services"));
      List<Ticket> tickets = ui.checkTickets(new HashSet<Object>(ticketIds));
      for (Ticket t : tickets) {
        if (Ticket.COMPLETED != t.getStatus()) {
          FlickrJPicturesProvider picturesProvider = new FlickrJPicturesProvider(context);
          Photo p = picturesProvider.getPhotoForId(t.getPhotoId());
          pictures.add(new ParcelableFlickrPhoto(p));
          Log.d("upload", "uploaded a picture with title " + p.getTitle());
          userNotifs.add("uploaded a picture with title " + p.getTitle());
        } else if (Ticket.FAILED == t.getStatus()) {
          Log.d("upload", "a picture upload has failed");
          userNotifs.add("a picture upload has failed");
        } else {
          Log.d("upload", "picture still uploading");
          userNotifs.add("picture still uploading");
        }
      }
    } catch (ParserConfigurationException | JSONException | IOException | FlickrException e) {
      Log.w("uploader", "failed to retrieve ticket");
    }
    return pictures;
  }

}

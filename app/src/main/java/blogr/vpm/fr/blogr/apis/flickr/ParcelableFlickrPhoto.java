package blogr.vpm.fr.blogr.apis.flickr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.Photo;

import blogr.vpm.fr.blogr.picture.PictureLoadedListener;

/**
 * Created by vince on 22/10/14.
 *
 * The bean to store a picture from Flickr.
 */
public class ParcelableFlickrPhoto implements Parcelable, PictureLoadedListener {

    private static final String PAR_ID = "parceled_id";
    private static final String PAR_URL = "parceled_url";
    private static final String PAR_TITLE = "parceled_title";
    private static final String PAR_DESC = "parceled_desc";
    private static final String PAR_URL_T = "parceled_url_thumbnail";
    private static final String PAR_URL_S = "parceled_url_small";
    private static final String PAR_URL_M = "parceled_url_medium";
    private static final String PAR_URL_L = "parceled_url_large";
    private static final String PAR_URL_O = "parceled_url_original";
    private static final String PAR_URL_SQ = "parceled_url_square";
    private static final String PAR_URL_LSQ = "parceled_url_largesquare";

    private final String id;

    private final String picUrl;

    private final String title;

    private final String description;

    private final String thumbnailSizeUrl;

    private final String smallSizeUrl;

    private final String mediumSizeUrl;

    private final String largeSizeUrl;

    private String originalSizeUrl;

    private final String largeSquareSizeUrl;

    private final String squareSizeUrl;

    private Bitmap smallBitmap;

    public ParcelableFlickrPhoto(Photo photo) {
        this.id = photo.getId();
        this.picUrl = photo.getUrl();
        this.title = photo.getTitle();
        this.description = photo.getDescription();
        this.thumbnailSizeUrl = photo.getThumbnailUrl();
        this.smallSizeUrl = photo.getSmallUrl();
        this.mediumSizeUrl = photo.getMediumUrl();
        this.largeSizeUrl = photo.getLargeUrl();
        try {
            this.originalSizeUrl = photo.getOriginalUrl();
        } catch (FlickrException e) {
            this.originalSizeUrl = "";
            Log.e("flickr", "could not retrieve original sized picture.");
        }
        this.squareSizeUrl = photo.getSmallSquareUrl();
        this.largeSquareSizeUrl = photo.getLargeSquareUrl();
    }

    private ParcelableFlickrPhoto(Parcel p){
        this.id = p.readBundle().getString(PAR_ID);
        this.picUrl = p.readBundle().getString(PAR_URL);
        this.title = p.readBundle().getString(PAR_TITLE);
        this.description = p.readBundle().getString(PAR_DESC);
        this.thumbnailSizeUrl = p.readBundle().getString(PAR_URL_T);
        this.smallSizeUrl = p.readBundle().getString(PAR_URL_S);
        this.mediumSizeUrl = p.readBundle().getString(PAR_URL_M);
        this.largeSizeUrl = p.readBundle().getString(PAR_URL_L);
        this.originalSizeUrl = p.readBundle().getString(PAR_URL_O);
        this.squareSizeUrl = p.readBundle().getString(PAR_URL_SQ);
        this.largeSquareSizeUrl = p.readBundle().getString(PAR_URL_LSQ);
    }

    public String getId() {
        return id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailSizeUrl() {
        return thumbnailSizeUrl;
    }

    public String getSmallSizeUrl() {
        return smallSizeUrl;
    }

    public String getMediumSizeUrl() {
        return mediumSizeUrl;
    }

    public String getLargeSizeUrl() {
        return largeSizeUrl;
    }

    public String getOriginalSizeUrl() {
        return originalSizeUrl;
    }

    public String getLargeSquareSizeUrl() {
        return largeSquareSizeUrl;
    }

    public String getSquareSizeUrl() {
        return squareSizeUrl;
    }

    public Bitmap getSmallBitmap() {
        return smallBitmap;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle b = new Bundle();
        b.putString(PAR_ID, id);
        b.putString(PAR_URL, picUrl);
        b.putString(PAR_TITLE, title);
        b.putString(PAR_DESC, description);
        b.putString(PAR_URL_T, thumbnailSizeUrl);
        b.putString(PAR_URL_S, smallSizeUrl);
        b.putString(PAR_URL_M, mediumSizeUrl);
        b.putString(PAR_URL_L, largeSizeUrl);
        b.putString(PAR_URL_O, originalSizeUrl);
        b.putString(PAR_URL_SQ, squareSizeUrl);
        b.putString(PAR_URL_LSQ, largeSquareSizeUrl);
        parcel.writeBundle(b);
    }

    @Override
    public void onPictureLoaded(Bitmap pictureBitmap) {
        this.smallBitmap = pictureBitmap;
    }

    public static final Creator CREATOR = new Creator<ParcelableFlickrPhoto>(){

        @Override
        public ParcelableFlickrPhoto createFromParcel(Parcel parcel) {
            return new ParcelableFlickrPhoto(parcel);
        }

        @Override
        public ParcelableFlickrPhoto[] newArray(int size) {
            return new ParcelableFlickrPhoto[size];
        }
    };
}

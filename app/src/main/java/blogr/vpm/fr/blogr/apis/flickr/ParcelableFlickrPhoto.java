package blogr.vpm.fr.blogr.apis.flickr;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;

/**
 * Created by vince on 22/10/14.
 */
public class ParcelableFlickrPhoto implements Parcelable {

    private static final String DEFAULT_ORIGINAL_IMAGE_SUFFIX = "_o.jpg";
    private static final String SMALL_SQUARE_IMAGE_SUFFIX = "_s.jpg";
    private static final String SMALL_IMAGE_SUFFIX = "_m.jpg";
    private static final String THUMBNAIL_IMAGE_SUFFIX = "_t.jpg";
    private static final String MEDIUM_IMAGE_SUFFIX = ".jpg";
    private static final String LARGE_IMAGE_SUFFIX = "_b.jpg";
    private static final String LARGE_SQUARE_IMAGE_SUFFIX = "_q.jpg";

    private static final String PAR_ID = "parceled_id";
    private static final String PAR_URL = "parceled_id";
    private static final String PAR_TITLE = "parceled_id";
    private static final String PAR_DESC = "parceled_id";

    private final String id;

    private final String picUrl;

    private final String title;

    private final String description;

    public ParcelableFlickrPhoto(Photo photo) {
        this.id = photo.getId();
        this.picUrl = photo.getUrl();
        this.title = photo.getTitle();
        this.description = photo.getDescription();
    }

    private ParcelableFlickrPhoto(Parcel p){
        this.id = p.readBundle().getString(PAR_ID);
        this.picUrl = p.readBundle().getString(PAR_URL);
        this.title = p.readBundle().getString(PAR_TITLE);
        this.description = p.readBundle().getString(PAR_DESC);
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
        parcel.writeBundle(b);
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

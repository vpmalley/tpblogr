package blogr.vpm.fr.blogr.bean;

import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;
import blogr.vpm.fr.blogr.picture.LocalPicture;
import blogr.vpm.fr.blogr.picture.Picture;

/**
 * Created by vincent on 29/08/14.
 */
public class Post implements Parcelable {

  public static final String INTENT_EXTRA_KEY = "intent_extra_post";

  private static final String TITLE_KEY = "title";

  private static final String CONTENT_KEY = "content";

  private static final String PICTURES_KEY = "pictures";

  private static final String BLOG_KEY = "blog";

  private static final String MD_KEY = "metadata";

  private static final String PLACES_KEY = "places";

  private static final String DATE_PATTERN = "yyyy-MM-dd-HH:mm:ss-ZZZZZ";

  private String title;

  private String content;

  private Blog blog;

  private PostMetadata md;

  @Expose
  private ArrayList<Place> places;

  @Expose
  private ArrayList<Picture> allPictures;

  public Post(String title, String content, Blog blog) {
    this.title = title;
    this.content = content;
    this.blog = blog;
    this.places = new ArrayList<>();
    this.md = new PostMetadata(new ArrayList<String>());
    this.allPictures = new ArrayList<>();
  }

  public Post(Post post) {
    this.title = post.getTitle();
    this.content = post.getContent();
    this.blog = post.getBlog();
    this.places = post.getPlaces();
    this.md = post.getMd();
    this.allPictures = post.getAllPictures();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Blog getBlog() {
    return blog;
  }

  public static Post emptyPost(Blog blog) {
    return new Post("", "", blog);
  }

  public PostMetadata getMd() {
    return md;
  }

  public void setMd(PostMetadata md) {
    this.md = md;
  }

  /**
   * Adds a place to the list associated with this post
   * @param place a {@link android.location.Location} representing a place
   */
  public void addPlace(Location place) {
    places.add(new Place(place));
  }

  /**
   * Adds a place to the list associated with this post
   * @param place a {@link android.location.Address} representing a place
   */
  public void addPlace(Address place) {
    places.add(new Place(place));
  }

  public ArrayList<Place> getPlaces() {
    return places;
  }

  public void setPlaces(Collection<Place> places) {
    this.places = new ArrayList<>(places);
  }

  public ArrayList<ParcelableFlickrPhoto> getFlickrPictures() {
    ArrayList<ParcelableFlickrPhoto> flickrPics = new ArrayList<>();
    for (Picture pic : getAllPictures()) {
      if (pic instanceof ParcelableFlickrPhoto) {
        flickrPics.add((ParcelableFlickrPhoto) pic);
      }
    }
    return flickrPics;
  }

  public void addFlickrPicture(ParcelableFlickrPhoto pic) {
    addPicture(pic);
  }

  public void setAllPictures(ArrayList<Picture> pictures) {
    this.allPictures = pictures;
  }

  public void addPicture(Picture picture) {
    allPictures.add(picture);
    Log.d("add pic", "count: " + allPictures.size());
  }

  public ArrayList<Uri> getPicturesAsFiles(Context context) {
    ArrayList<Uri> pictureFiles = new ArrayList<>();
    for (Picture pic : allPictures) {
      if (pic instanceof LocalPicture) {
        Cursor c = context.getContentResolver().query(((LocalPicture) pic).getLocalUri(),
            new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        if (c.getCount() > 0) {
          c.moveToFirst();
          String filePath = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
          File picFile = new File(filePath);
          picFile.setReadable(true, false);
          Uri pictureUri = Uri.fromFile(picFile);
          pictureFiles.add(pictureUri);
          c.close();
        }
      }
    }
    return pictureFiles;
  }

  public void replaceUploadedPictures(Map<Picture, Picture> replacements) {
    for (Map.Entry<Picture, Picture> replacement : replacements.entrySet()) {
      // replace tags in content
      String insertedTag = replacement.getKey().getUrlForInsertion();
      String tagToInsert = replacement.getValue().getUrlForInsertion();
      String contentWithReplacedPictures = getContent().replace(insertedTag, tagToInsert);
      setContent(contentWithReplacedPictures);

      // replace picture in list
      getAllPictures().remove(replacement.getKey());
      getAllPictures().add(replacement.getValue());
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    Bundle b = new Bundle();
    b.putString(TITLE_KEY, title);
    b.putString(CONTENT_KEY, content);
    b.putParcelable(BLOG_KEY, blog);
    b.putParcelable(MD_KEY, md);
    parcel.writeBundle(b);
    parcel.writeTypedList(places);
    parcel.writeTypedList(allPictures);
  }

  private Post(Parcel in) {
    Bundle b = in.readBundle(Post.class.getClassLoader());
    // without setting the classloader, it fails on BadParcelableException : ClassNotFoundException when
    // unmarshalling Post class
    title = b.getString(TITLE_KEY);
    content = b.getString(CONTENT_KEY);
    blog = b.getParcelable(BLOG_KEY);
    md = b.getParcelable(MD_KEY);
    places = new ArrayList<>();
    in.readTypedList(places, Place.CREATOR);
    allPictures = new ArrayList<>();
    in.readTypedList(allPictures, Picture.CREATOR);
  }

  public static final Parcelable.Creator<Post> CREATOR
          = new Parcelable.Creator<Post>() {
    public Post createFromParcel(Parcel in) {
      return new Post(in);
    }

    public Post[] newArray(int size) {
      return new Post[size];
    }
  };

  public ArrayList<Picture> getAllPictures() {
    return allPictures;
  }

}

package blogr.vpm.fr.blogr.picture;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;

/**
 * Created by vince on 14/04/15.
 * <p/>
 * Gson serializer for Picture
 */
public class PictureSerializer implements JsonSerializer<Picture> {

  private static final String TYPE_KEY = "type";

  @Override
  public JsonElement serialize(Picture src, Type typeOfSrc, JsonSerializationContext context) {
    JsonElement serializedPicture;
    if (src instanceof LocalPicture) {
      serializedPicture = context.serialize(src, LocalPicture.class);
      serializedPicture.getAsJsonObject().addProperty(TYPE_KEY, LocalPicture.class.getCanonicalName());
    } else {
      serializedPicture = context.serialize(src, ParcelableFlickrPhoto.class);
      serializedPicture.getAsJsonObject().addProperty(TYPE_KEY, ParcelableFlickrPhoto.class.getCanonicalName());
    }
    return serializedPicture;
  }
}

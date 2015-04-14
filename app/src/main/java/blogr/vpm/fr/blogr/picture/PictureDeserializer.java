package blogr.vpm.fr.blogr.picture;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;

/**
 * Created by vince on 14/04/15.
 * <p/>
 * Gson deserializer for Picture
 */
public class PictureDeserializer implements JsonDeserializer<Picture> {
  @Override
  public Picture deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    String className = json.getAsJsonObject().get("type").getAsString();
    json.getAsJsonObject().remove("type");
    Picture p;
    if (LocalPicture.class.getCanonicalName().equals(className)) {
      p = context.deserialize(json, LocalPicture.class);
    } else {
      p = context.deserialize(json, ParcelableFlickrPhoto.class);
    }
    return p;
  }
}

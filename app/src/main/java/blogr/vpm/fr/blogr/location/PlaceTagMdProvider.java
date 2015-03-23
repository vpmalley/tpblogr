package blogr.vpm.fr.blogr.location;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Place;
import blogr.vpm.fr.blogr.insertion.MetadataProvider;
import blogr.vpm.fr.blogr.insertion.SingleTagProvider;

/**
 * Created by vince on 23/03/15.
 */
public class PlaceTagMdProvider implements SingleTagProvider, MetadataProvider {

  private final Place place;

  private final Context context;

  public PlaceTagMdProvider(Place place, Context context) {
    this.place = place;
    this.context = context;
  }

  @Override
  public Map<String, String> getMappings() {
    Map<String, String> mappings = new HashMap<>();
    mappings.put(context.getResources().getString(R.string.latitude), String.valueOf(place.getLatitude()));
    mappings.put(context.getResources().getString(R.string.longitude), String.valueOf(place.getLongitude()));
    mappings.put(context.getResources().getString(R.string.address), String.valueOf(place.getAddressDisplay()));
    if (!place.getCountryName().isEmpty()) {
      mappings.put(context.getResources().getString(R.string.country), String.valueOf(place.getCountryName()));
    }
    if (!place.getFeatureName().isEmpty()) {
      mappings.put(context.getResources().getString(R.string.feature), String.valueOf(place.getFeatureName()));
    }
    return mappings;
  }

  @Override
  public String getTag() {
    return place.toString();
  }
}

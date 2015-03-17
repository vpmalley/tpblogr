package blogr.vpm.fr.blogr.location;

import android.content.Context;
import android.location.Location;

import java.util.HashMap;
import java.util.Map;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.insertion.MetadataProvider;
import blogr.vpm.fr.blogr.insertion.SingleTagProvider;

/**
 * Created by vincent on 09/10/14.
 */
public class LatLongTagProvider implements SingleTagProvider, MetadataProvider {

  private final LocationProvider locationProvider;

  private final Context context;

  public LatLongTagProvider(Context context, LocationProvider locationProvider) {
    this.context = context;
    this.locationProvider = locationProvider;
  }

  @Override
  public String getTag() {
    Location currentLocation = locationProvider.getCurrentLocation();
    StringBuilder locationBuilder = new StringBuilder();
    if (currentLocation != null) {
      locationBuilder.append(context.getResources().getString(R.string.latitude));
      locationBuilder.append(" ");
      locationBuilder.append(currentLocation.getLatitude());
      locationBuilder.append(context.getResources().getString(R.string.longitude));
      locationBuilder.append(" ");
      locationBuilder.append(currentLocation.getLongitude());
      locationBuilder.append(context.getResources().getString(R.string.altitude));
      locationBuilder.append(" ");
      locationBuilder.append(currentLocation.getAltitude());
    }
    return locationBuilder.toString();
  }

  @Override
  public Map<String, String> getMappings() {
    Location currentLocation = locationProvider.getCurrentLocation();
    Map<String, String> locationMappings = new HashMap<>();
    if (currentLocation != null) {
      locationMappings.put(context.getResources().getString(R.string.latitude), String.valueOf(currentLocation.getLatitude()));
      locationMappings.put(context.getResources().getString(R.string.longitude), String.valueOf(currentLocation.getLongitude()));
      locationMappings.put(context.getResources().getString(R.string.altitude), String.valueOf(currentLocation.getAltitude()));
    }
    return locationMappings;
  }
}

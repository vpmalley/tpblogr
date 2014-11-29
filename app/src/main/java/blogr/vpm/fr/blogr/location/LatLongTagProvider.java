package blogr.vpm.fr.blogr.location;

import android.content.Context;
import android.location.Location;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.insertion.SingleTagProvider;

/**
 * Created by vincent on 09/10/14.
 */
public class LatLongTagProvider implements SingleTagProvider {

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
}

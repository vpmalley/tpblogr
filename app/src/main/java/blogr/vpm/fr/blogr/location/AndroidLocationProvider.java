package blogr.vpm.fr.blogr.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by vince on 28/11/14.
 */
public class AndroidLocationProvider implements LocationProvider, LocationListener {

  private final Context context;

  private Location lastLocation;

  public AndroidLocationProvider(Context context) {
    this.context = context;
  }

  @Override
  public void connect() {
    LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    Criteria locCriteria = new Criteria();
    locCriteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
    locCriteria.setSpeedRequired(false);
    locCriteria.setAltitudeRequired(true);
    locCriteria.setCostAllowed(false);
    locCriteria.setVerticalAccuracy(Criteria.ACCURACY_MEDIUM);

    locMan.requestSingleUpdate(locCriteria, this, Looper.myLooper());
    Toast.makeText(context, "providers " + locMan.getProviders(locCriteria, true).toString(), Toast.LENGTH_SHORT).show();
  }

  @Override
  public Location getCurrentLocation() {

    if (lastLocation == null){
      LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
      lastLocation = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
    return lastLocation;
  }

  @Override
  public void disconnect() {
    // do nothing
  }

  @Override
  public void onLocationChanged(Location location) {
    lastLocation = location;
    Toast.makeText(context, "location changed", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    Toast.makeText(context, "status changed for " + provider, Toast.LENGTH_SHORT).show();
    // do nothing
  }

  @Override
  public void onProviderEnabled(String provider) {
    Toast.makeText(context, "provider enabled : " + provider, Toast.LENGTH_SHORT).show();
    // do nothing
  }

  @Override
  public void onProviderDisabled(String provider) {
    Toast.makeText(context, "provider disabled : " + provider, Toast.LENGTH_SHORT).show();
    // do nothing
  }
}

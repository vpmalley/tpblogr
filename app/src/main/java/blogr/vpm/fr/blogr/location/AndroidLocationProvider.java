package blogr.vpm.fr.blogr.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import blogr.vpm.fr.blogr.R;

/**
 * Created by vince on 28/11/14.
 */
public class AndroidLocationProvider implements LocationProvider, LocationListener {

  private final Context context;

  private Location lastLocation;

  private Criteria locCriteria;

  public AndroidLocationProvider(Context context) {
    this.context = context;
    locCriteria = new Criteria();
    locCriteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
    locCriteria.setSpeedRequired(false);
    locCriteria.setAltitudeRequired(false);
    locCriteria.setCostAllowed(false);
    locCriteria.setVerticalAccuracy(Criteria.ACCURACY_MEDIUM);
  }

  @Override
  public void connect() {
    // do nothing
  }

  @Override
  public Location getCurrentLocation() {
    if (lastLocation == null){
      LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
      String provider = locMan.getBestProvider(locCriteria, true);
      lastLocation = locMan.getLastKnownLocation(provider);
      if (lastLocation == null) {
        acquireLocation();
      }
    }
    return lastLocation;
  }

  @Override
  public void disconnect() {
    // do nothing
  }


  private void acquireLocation(){
    LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    List<String> availableProviders = locMan.getProviders(locCriteria, true);
    if ((availableProviders != null) && (!availableProviders.isEmpty())) {
      locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 2, this);
      Toast.makeText(context, context.getString(R.string.location_in_progress), Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(context, context.getString(R.string.no_location_provider), Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onLocationChanged(Location location) {
    lastLocation = location;
    Toast.makeText(context, context.getString(R.string.location_available), Toast.LENGTH_SHORT).show();
    LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    locMan.removeUpdates(this);
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    // do nothing
  }

  @Override
  public void onProviderEnabled(String provider) {
    // do nothing
  }

  @Override
  public void onProviderDisabled(String provider) {
    // do nothing
  }
}

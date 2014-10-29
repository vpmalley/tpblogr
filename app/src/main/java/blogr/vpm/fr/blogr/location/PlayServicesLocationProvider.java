package blogr.vpm.fr.blogr.location;

import android.app.Activity;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import blogr.vpm.fr.blogr.R;

/**
 * Created by vincent on 09/10/14.
 */
public class PlayServicesLocationProvider implements LocationProvider, GooglePlayServicesClient.ConnectionCallbacks {

  private final Activity activity;

  private LocationClient locationClient;

  private final boolean isEnabled;

  public PlayServicesLocationProvider(Activity activity) {
    this.activity = activity;
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    isEnabled = prefs.getBoolean("pref_permissions_position", false);
  }

  @Override
  public void connect() {
    if (!isServiceAvailable()) {
      Toast.makeText(activity, activity.getResources().getString(R.string.outdatedplayservices),
          Toast.LENGTH_SHORT).show();
    }
    if (isEnabled) {
      locationClient = new LocationClient(activity, this, new LocationFailedConnectionListener(activity));
      locationClient.connect();
    }
  }

  @Override
  public Location getCurrentLocation() {
    // if it has not been instantiated yet
    if (isEnabled && (locationClient == null)) {
      Toast.makeText(activity, activity.getResources().getString(R.string.disconnectedplayservices),
          Toast.LENGTH_SHORT).show();
    }
    Location lastLocation = null;
    if (isEnabled) {
      lastLocation = locationClient.getLastLocation();
    }
    if (lastLocation == null) {
      Toast.makeText(activity, activity.getResources().getString(R.string.checkpositionparams),
          Toast.LENGTH_SHORT).show();
    }
    return lastLocation;
  }

  @Override
  public void disconnect() {
    if (isEnabled) {
      locationClient.disconnect();
    }
  }

  private boolean isServiceAvailable() {
    int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
    if (ConnectionResult.SUCCESS == isAvailable) {
      return true;
    }
    return false;
  }

  @Override
  public void onConnected(Bundle bundle) {
    // nothing to do
  }

  @Override
  public void onDisconnected() {
    // nothing to do
  }

  public class LocationFailedConnectionListener implements GooglePlayServicesClient.OnConnectionFailedListener {

    private Activity activity;

    public LocationFailedConnectionListener(Activity activity) {
      this.activity = activity;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
      if (connectionResult.hasResolution()) {
        try {
          connectionResult.startResolutionForResult(activity, 0);
        } catch (IntentSender.SendIntentException e) {
          Log.e("location", e.getMessage());
        }
      }
    }
  }
}


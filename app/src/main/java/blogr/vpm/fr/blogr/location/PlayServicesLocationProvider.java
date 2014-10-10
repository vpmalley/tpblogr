package blogr.vpm.fr.blogr.location;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationClient;

/**
 * Created by vincent on 09/10/14.
 */
public class PlayServicesLocationProvider implements LocationProvider, GooglePlayServicesClient.ConnectionCallbacks {

    private Activity activity;

    private LocationClient locationClient;

    public PlayServicesLocationProvider(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void connect() {
        if (!isServiceAvailable()){
            throw new IllegalStateException("Play Services are not up-to-date and location cannot be obtained");
        }

        locationClient = new LocationClient(activity, this, new LocationFailedConnectionListener(activity));
        locationClient.connect();
    }

    @Override
    public Location getCurrentLocation() {
        // if it has not been instantiated yet
        if (locationClient == null){
            throw new IllegalStateException("Play Services are not connected to and location cannot be obtained");
        }
        Location lastLocation = locationClient.getLastLocation();
        return lastLocation;
    }

    @Override
    public void disconnect() {
        locationClient.disconnect();
    }

    private boolean isServiceAvailable(){
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (ConnectionResult.SUCCESS == isAvailable){
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
            if (connectionResult.hasResolution()){
                try {
                    connectionResult.startResolutionForResult(activity, 0);
                } catch (IntentSender.SendIntentException e) {
                    Log.e("location", e.getMessage());
                }
            }
        }
    }
}


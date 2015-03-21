package blogr.vpm.fr.blogr.location;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import blogr.vpm.fr.blogr.activity.RefreshListener;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.network.DefaultNetworkChecker;

/**
 * Created by vince on 20/03/15.
 */
public class AndroidPlacesProvider implements PlacesProvider {

  private static final long LOCATION_EXPIRY = 360000;
  private static final int DEFAULT_MAX_RESULTS = 20;

  private final Context context;

  private final AddressesListener addressesListener;

  private final RefreshListener refreshListener;

  public AndroidPlacesProvider(Context context, AddressesListener addressesListener, RefreshListener refreshListener) {
    this.context = context;
    this.addressesListener = addressesListener;
    this.refreshListener = refreshListener;
  }

  @Override
  public void acquireAndInsertLocation(Post post) {
    Criteria locCriteria = new Criteria();
    locCriteria.setAccuracy(Criteria.ACCURACY_LOW);
    locCriteria.setSpeedRequired(false);
    locCriteria.setAltitudeRequired(false);
    locCriteria.setCostAllowed(false);

    LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    Location lastLocation = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    if ((lastLocation == null) || (lastLocation.getTime() < System.currentTimeMillis() - LOCATION_EXPIRY)) {
      List<String> availableProviders = locMan.getProviders(locCriteria, true);
      if ((availableProviders != null) && (!availableProviders.isEmpty())) {
        locMan.requestLocationUpdates(3, 2, locCriteria, new LocationReceiver(context, post), Looper.getMainLooper());
      } else {
        Toast.makeText(context, "Positioning is not enabled. Please check your settings (GPS, mobile data) and try again", Toast.LENGTH_SHORT).show();
      }
    } else {
      onLocationFound(post, lastLocation);
    }
  }

  private class LocationReceiver implements LocationListener {

    private final Context context;

    private final Post post;

    private LocationReceiver(Context context, Post post) {
      this.context = context;
      this.post = post;
    }

    @Override
    public void onLocationChanged(Location location) {
      LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
      locMan.removeUpdates(this);
      onLocationFound(post, location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
      // doing nothing
    }

    @Override
    public void onProviderEnabled(String s) {
      // doing nothing
    }

    @Override
    public void onProviderDisabled(String s) {
      // doing nothing
    }
  }

  private void onLocationFound(Post post, Location lastLocation) {
    if (canSearchAddress()) {
      searchAndInsertAddress(post, lastLocation);
    } else {
      post.addPlace(lastLocation);
      refreshListener.refreshViewFromPost();
      Toast.makeText(context, "Position acquired and inserted.", Toast.LENGTH_SHORT).show();
    }
  }

  private boolean canSearchAddress() {
    boolean canSearch = false;
    if (!Geocoder.isPresent()) {
      Toast.makeText(context, "Searching for places is not possible on this device", Toast.LENGTH_SHORT).show();
    } else if (!new DefaultNetworkChecker().checkNetworkForDownload(context, true)) {
      Toast.makeText(context, "You are offline. Please check your connection", Toast.LENGTH_SHORT).show();
    } else {
      canSearch = true;
    }
    return canSearch;
  }

  private void searchAndInsertAddress(Post post, Location location) {
    List<Address> nearbyAddresses = new ArrayList<>();
    try {
      nearbyAddresses = new Geocoder(context).getFromLocation(location.getLatitude(), location.getLongitude(), DEFAULT_MAX_RESULTS);
    } catch (IOException e) {
      Log.w("location", e.toString());
      Toast.makeText(context, "Failed to find addresses. Please try again.", Toast.LENGTH_SHORT).show();
    }
    onAddressesFound(post, nearbyAddresses);
  }

  @Override
  public void searchAndInsertAddress(Post post, String placeName) {
    List<Address> nearbyAddresses = new ArrayList<>();
    if (canSearchAddress()) {
      try {
        nearbyAddresses = new Geocoder(context).getFromLocationName(placeName, DEFAULT_MAX_RESULTS);
      } catch (IOException e) {
        Log.w("location", e.toString());
        Toast.makeText(context, "Failed to find addresses. Please try again.", Toast.LENGTH_SHORT).show();
      }
      onAddressesFound(post, nearbyAddresses);
    }
  }

  private void onAddressesFound(Post post, List<Address> nearbyAddresses) {
    if (nearbyAddresses.isEmpty()) {
      Toast.makeText(context, "No place found with that name", Toast.LENGTH_SHORT).show();
    } else if (nearbyAddresses.size() == 1){
      post.addPlace(nearbyAddresses.get(0));
      refreshListener.refreshViewFromPost();
    } else {
      addressesListener.onAddressesFound(new ArrayList<>(nearbyAddresses), post);
    }
  }
}

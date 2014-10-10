package blogr.vpm.fr.blogr.location;

import android.location.Location;

import blogr.vpm.fr.blogr.insertion.SingleTagProvider;

/**
 * Created by vincent on 09/10/14.
 */
public class LatLongTagProvider implements SingleTagProvider {

    LocationProvider locationProvider;

    public LatLongTagProvider(LocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }

    @Override
    public String getTag() {
        Location currentLocation = locationProvider.getCurrentLocation();
        StringBuilder locationBuilder = new StringBuilder();
        if (currentLocation != null) {
            locationBuilder.append("latitude ");
            locationBuilder.append(currentLocation.getLatitude());
            locationBuilder.append(", longitude ");
            locationBuilder.append(currentLocation.getLongitude());
        }
        return locationBuilder.toString();
    }
}

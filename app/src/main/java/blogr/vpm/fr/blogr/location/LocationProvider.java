package blogr.vpm.fr.blogr.location;

import android.location.Location;

/**
 * Created by vincent on 09/10/14.
 *
 * Provides the location (geographic position)
 */
public interface LocationProvider {

    /**
     * Before querying the current location, the provider should first be connected.
     */
    void connect();


    /**
     * Provides the current position as a one-time query
     * @return the current location
     */
    Location getCurrentLocation();

    /**
     * After the provider is used, it is necessary to disconnect it.
     */
    void disconnect();

}

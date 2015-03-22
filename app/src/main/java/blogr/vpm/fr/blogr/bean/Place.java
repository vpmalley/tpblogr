package blogr.vpm.fr.blogr.bean;

import android.location.Address;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * Created by vince on 22/03/15.
 */
public class Place implements Parcelable {

  private boolean hasLocation = false;
  private float accuracy;
  private double altitude;
  private float bearing;
  private double latitude = 0;
  private double longitude = 0;
  private float speed;

  private boolean hasAddress = false;
  private String addressDisplay;
  private String adminArea;
  private String countryCode;
  private String countryName;
  private Locale locale;
  private String featureName;
  private String locality;
  private String url;

  public Place(Location location) {
    if (location.hasAccuracy()) {
      accuracy = location.getAccuracy();
    }
    if (location.hasAltitude()) {
      altitude = location.getAltitude();
    }
    if (location.hasBearing()) {
      bearing = location.getBearing();
    }
    if (location.hasSpeed()) {
      speed = location.getSpeed();
    }
    latitude = location.getLatitude();
    longitude = location.getLongitude();
    hasLocation = true;
  }

  public Place(Address address) {
    setAddress(address);
  }

  public void setAddress(Address address) {
    if (!hasLocation && address.hasLatitude()) {
      latitude = address.getLatitude();
    }
    if (!hasLocation && address.hasLongitude()) {
      longitude = address.getLongitude();
    }
    addressDisplay = getAddressLines(address);
    adminArea = address.getAdminArea();
    countryCode = address.getCountryCode();
    countryName = address.getCountryName();
    locale = address.getLocale();
    featureName = address.getFeatureName();
    locality = address.getLocality();
    url = address.getUrl();
    hasAddress = true;
  }

  public static String getAddressLines(Address address) {
    StringBuilder addressLines = new StringBuilder();
    int i = 0;
    while (i < address.getMaxAddressLineIndex()){
      addressLines.append(address.getAddressLine(i++));
    }
    return addressLines.toString();
  }

  public float getAccuracy() {
    return accuracy;
  }

  public double getAltitude() {
    return altitude;
  }

  public float getBearing() {
    return bearing;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public float getSpeed() {
    return speed;
  }

  public String getAddressDisplay() {
    return addressDisplay;
  }

  public String getAdminArea() {
    return adminArea;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public String getCountryName() {
    return countryName;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getFeatureName() {
    return featureName;
  }

  public String getLocality() {
    return locality;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public String toString() {
    String display = "";
    if (hasAddress) {
      display = addressDisplay;
    } else {
      display = "Lat. " + latitude + ", Lon. " + longitude;
    }
    return display;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeInt(hasLocation ? 1 : 0);
    parcel.writeInt(hasAddress ? 1 : 0);
    parcel.writeDouble(latitude);
    parcel.writeDouble(longitude);
    if (hasLocation) {
      parcel.writeFloat(accuracy);
      parcel.writeDouble(altitude);
      parcel.writeFloat(bearing);
      parcel.writeFloat(speed);
    }
    if (hasAddress) {
      parcel.writeString(addressDisplay);
      parcel.writeString(adminArea);
      parcel.writeString(countryCode);
      parcel.writeString(countryName);
      parcel.writeString(featureName);
      parcel.writeString(locality);
      parcel.writeString(url);
    }
  }

  public Place(Parcel in) {
    hasLocation = (in.readInt() > 0);
    hasAddress = (in.readInt() > 0);
    latitude = in.readDouble();
    longitude = in.readDouble();
    if (hasLocation) {
      accuracy = in.readFloat();
      altitude = in.readDouble();
      bearing = in.readFloat();
      speed = in.readFloat();
    }
    if (hasAddress) {
      addressDisplay = in.readString();
      adminArea = in.readString();
      countryCode = in.readString();
      countryName = in.readString();
      featureName = in.readString();
      locality = in.readString();
      url = in.readString();
    }
  }

  public static final Parcelable.Creator<Place> CREATOR
          = new Parcelable.Creator<Place>() {
    public Place createFromParcel(Parcel in) {
      return new Place(in);
    }

    public Place[] newArray(int size) {
      return new Place[size];
    }
  };

}

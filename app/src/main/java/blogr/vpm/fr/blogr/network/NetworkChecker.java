package blogr.vpm.fr.blogr.network;

import android.content.Context;

/**
 * Created by vince on 28/10/14.
 */
public interface NetworkChecker {

  /**
   * Checks whether it is possible to download a media
   *
   * @param context the current Context
   * @param displayError whether an error should be displayed if it is not possible to download a media
   * @return whether the network services are enabled
   */
  public boolean checkNetworkForDownload(Context context, boolean displayError);

  /**
   * Checks whether media downloads are allowed over Wifi by user in this context
   *
   * @param context the current Context
   * @return whether media can be downloaded
   */
  boolean isDownloadOverWifiAllowed(Context context);

  /**
   * Checks whether media downloads are allowed over mobile network by user in this context
   *
   * @param context the current Context
   * @return whether media can be downloaded
   */
  boolean isDownloadOverMobileAllowed(Context context);
}

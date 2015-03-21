package blogr.vpm.fr.blogr.location;

import android.location.Address;

import java.util.ArrayList;

import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 20/03/15.
 */
public interface AddressesListener {

  void onAddressesFound(ArrayList<Address> addresses, Post post);
}

package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.location.Address;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * This is a dialog to pick an address among others. Two arguments must be passed to this Fragment:
 * {@link AddressPickerFragment#ARGS_ADDR} should link to a list of Address, one for each address
 * {@link AddressPickerFragment#ARGS_POST} should be a Post to add the picked place to
 *
 * Created by vince on 29/12/14.
 */
public class AddressPickerFragment extends DialogFragment {

  public static final String ARGS_ADDR = "args_addresses";

  public static final String ARGS_POST = "args_post";

  private Post post;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    post = getArguments().getParcelable(ARGS_POST);
    final ArrayList<Address> allAddresses = getArguments().getParcelableArrayList(ARGS_ADDR);
    ArrayList<String> printableAddresses = getPrintableAddresses(allAddresses);

    ArrayAdapter<String> blogsAdapter = new ArrayAdapter<>(getActivity(), R.layout.post_item, printableAddresses);

    return new AlertDialog.Builder(getActivity())
            .setTitle(R.string.pick_address)
            .setAdapter(blogsAdapter, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int position) {
                post.addPlace(allAddresses.get(position));
              }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing - just close the dialog
              }
            })
            .create();
  }

  /**
   * Opens a dialog to pick an address among a list
   * @param activity used to obtain the fragment manager
   * @param addresses the list of pickable addresses
   * @param post the post to add the address to
   */
  public void openAddressPicker(Activity activity, ArrayList<Address> addresses, Post post){
    Bundle args = new Bundle();
    args.putParcelableArrayList(AddressPickerFragment.ARGS_ADDR, addresses);
    args.putParcelable(AddressPickerFragment.ARGS_POST, post);
    setArguments(args);
    show(activity.getFragmentManager(), "addressPicker");
  }



  /**
   * Transforms a list of addresses to a list of strings for display purposes. The order stays the same
   * @param addresses the list of addresses to transform
   * @return a list of displayable String, with the same length and in the same order as the list of Address
   */
  private ArrayList<String> getPrintableAddresses(List<Address> addresses) {
    ArrayList<String> printableAddresses = new ArrayList<>();
    for (Address address : addresses) {
      printableAddresses.add(getAddressLines(address));
    }
    return printableAddresses;
  }

  public static String getAddressLines(Address address) {
    StringBuilder addressLines = new StringBuilder();
    int i = 0;
    while (i < address.getMaxAddressLineIndex()){
      addressLines.append(address.getAddressLine(i++));
    }
    return addressLines.toString();
  }

}

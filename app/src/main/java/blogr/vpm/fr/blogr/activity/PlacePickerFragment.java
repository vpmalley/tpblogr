package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Place;
import blogr.vpm.fr.blogr.location.PlaceTagMdProvider;

/**
 * This is a dialog to pick an address among others. Two arguments must be passed to this Fragment:
 * {@link blogr.vpm.fr.blogr.activity.PlacePickerFragment#ARGS_PLACES} should link to a list of Address, one for each address
 * {@link blogr.vpm.fr.blogr.activity.PlacePickerFragment#ARGS_REQ} should be a Post to add the picked place to
 *
 * Created by vince on 29/12/14.
 */
public class PlacePickerFragment extends DialogFragment {

  public static final String ARGS_PLACES = "args_places";

  public static final String ARGS_REQ = "args_req";
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    final int request = getArguments().getInt(ARGS_REQ);
    final ArrayList<Place> allAddresses = getArguments().getParcelableArrayList(ARGS_PLACES);
    ArrayAdapter<Place> placesAdapter = new ArrayAdapter<>(getActivity(), R.layout.post_item, allAddresses);

    return new AlertDialog.Builder(getActivity())
            .setTitle(R.string.pick_address)
            .setAdapter(placesAdapter, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int position) {

                ((PlacePickedListener)getActivity()).onPlacePicked(new PlaceTagMdProvider(allAddresses.get(position), getActivity()), request);
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
   * @param places the list of pickable places
   */
  public void openPlacesPicker(Activity activity, ArrayList<Place> places, int request){
    Bundle args = new Bundle();
    args.putInt(ARGS_REQ, request);
    args.putParcelableArrayList(PlacePickerFragment.ARGS_PLACES, places);
    setArguments(args);
    show(activity.getFragmentManager(), "placePicker");
  }

}

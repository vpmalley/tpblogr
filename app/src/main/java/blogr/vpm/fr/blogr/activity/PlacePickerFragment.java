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
 * {@link blogr.vpm.fr.blogr.activity.PlacePickedListener} should be a listener that will update a Post and/or a View
 *
 * Created by vince on 29/12/14.
 */
public class PlacePickerFragment extends DialogFragment {

  public static final String ARGS_PLACES = "args_places";

  private PlacePickedListener listener;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final ArrayList<Place> allPlaces = getArguments().getParcelableArrayList(ARGS_PLACES);
    ArrayAdapter<Place> placesAdapter = new ArrayAdapter<>(getActivity(), R.layout.post_item, allPlaces);

    return new AlertDialog.Builder(getActivity())
            .setTitle(R.string.pick_address)
            .setAdapter(placesAdapter, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int position) {
                listener.onPlacePicked(new PlaceTagMdProvider(allPlaces.get(position), getActivity()));
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
  public void openPlacesPicker(Activity activity, ArrayList<Place> places, PlacePickedListener listener){
    this.listener = listener;
    Bundle args = new Bundle();
    args.putParcelableArrayList(PlacePickerFragment.ARGS_PLACES, places);
    setArguments(args);
    show(activity.getFragmentManager(), "placePicker");
  }

}

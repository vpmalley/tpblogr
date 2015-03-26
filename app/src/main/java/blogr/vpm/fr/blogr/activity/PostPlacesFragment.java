package blogr.vpm.fr.blogr.activity;


import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Place;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.location.AddressesListener;
import blogr.vpm.fr.blogr.location.AndroidPlacesProvider;
import blogr.vpm.fr.blogr.location.PlacesProvider;

/**
 * Created by vince on 21/03/15.
 */
public class PostPlacesFragment extends Fragment implements AddressesListener {

  private EditText searchField;

  private ImageButton searchButton;

  private ImageButton locateButton;

  private ArrayAdapter<Place> placesAdapter;

  private Post getCurrentPost() {
    return ((PostEditionActivity) getActivity()).getCurrentPost();
  }

  private void setCurrentPost(Post currentPost) {
    ((PostEditionActivity) getActivity()).setCurrentPost(currentPost);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_post_places, container, false);
    AbsListView placesList = (AbsListView) v.findViewById(R.id.places);
    searchField = (EditText) v.findViewById(R.id.searchField);
    searchButton = (ImageButton) v.findViewById(R.id.searchButton);
    locateButton = (ImageButton) v.findViewById(R.id.locateButton);

    placesAdapter = new ArrayAdapter<Place>(getActivity(), R.layout.post_item, R.id.postItem, getCurrentPost().getPlaces());
    placesList.setAdapter(placesAdapter);
    refreshViewFromPost();

    locateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PlacesProvider placesProvider = new AndroidPlacesProvider(getActivity(), PostPlacesFragment.this, (RefreshListener) getActivity());
        placesProvider.acquireAndInsertLocation(getCurrentPost());
      }
    });

    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PlacesProvider placesProvider = new AndroidPlacesProvider(getActivity(), PostPlacesFragment.this, (RefreshListener) getActivity());
        if ((searchField.getText() != null) && (!searchField.getText().toString().isEmpty())) {
          placesProvider.searchAndInsertAddress(getCurrentPost(), searchField.getText().toString());
        }
      }
    });


    return v;
  }

  /**
   * Notes on lifecycle
   * before onResume, the currentPost has the right content and title
   * between onResume and onPause, the view has the right content and title
   * between onPause and onResume, the currentPost has the right content and title but the view should not change
   * (e.g. when onActivityResult() is called)
   * after onPause, the currentPost has the right content and title but the view should not change
   */
  @Override
  public void onResume() {
    super.onResume();
    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    refreshViewFromPost();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.postplaces, menu);
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings:
        startActivity(new Intent(getActivity(), AllPreferencesActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  void refreshViewFromPost() {
    if (placesAdapter != null) {
      placesAdapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onAddressesFound(ArrayList<Address> addresses, Post post) {
    new AddressPickerFragment().openAddressPicker(getActivity(), addresses, post);
  }
}

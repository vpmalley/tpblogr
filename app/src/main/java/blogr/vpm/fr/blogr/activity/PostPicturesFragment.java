package blogr.vpm.fr.blogr.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.apis.flickr.FlickrJAndroidProvider;
import blogr.vpm.fr.blogr.apis.flickr.FlickrJAsyncTaskProvider;
import blogr.vpm.fr.blogr.apis.flickr.FlickrProvider;
import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.picture.PicturePickedListener;

/**
 * Created by vince on 21/03/15.
 */
public class PostPicturesFragment extends Fragment implements PicturePickedListener {

  private ArrayAdapter<ParcelableFlickrPhoto> picturesAdapter;

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
    View v = inflater.inflate(R.layout.fragment_post_pictures, container, false);
    AbsListView picturesList = (AbsListView) v.findViewById(R.id.pictures);

    picturesAdapter = new ArrayAdapter<ParcelableFlickrPhoto>(getActivity(), R.layout.post_item, R.id.postItem, getCurrentPost().getFlickrPictures());
    picturesList.setAdapter(picturesAdapter);
    refreshViewFromPost();

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
    inflater.inflate(R.menu.postpictures, menu);
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_new:
        FlickrProvider flickrD = new FlickrJAndroidProvider(getActivity());
        FlickrProvider flickrP = new FlickrJAsyncTaskProvider(getActivity(), flickrD, this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String flickrUsername = prefs.getString("pref_flickr_username", "");
        int picNb = Integer.valueOf(prefs.getString("pref_flickr_number_pics", "20"));
        flickrP.getUserPhotos(flickrUsername, picNb);
        return true;
      case R.id.action_settings:
        startActivity(new Intent(getActivity(), AllPreferencesActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  void refreshViewFromPost() {
    if (picturesAdapter != null) {
      picturesAdapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onPicturePicked(ParcelableFlickrPhoto pic) {
    getCurrentPost().addFlickrPicture(pic);
    refreshViewFromPost();
  }
}

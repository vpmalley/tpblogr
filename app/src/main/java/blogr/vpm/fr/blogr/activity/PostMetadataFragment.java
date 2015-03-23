package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.apis.flickr.FlickrJAndroidProvider;
import blogr.vpm.fr.blogr.apis.flickr.FlickrJAsyncTaskProvider;
import blogr.vpm.fr.blogr.apis.flickr.FlickrProvider;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.bean.PostMetadata;
import blogr.vpm.fr.blogr.insertion.MetadataProvider;
import blogr.vpm.fr.blogr.location.AndroidLocationProvider;
import blogr.vpm.fr.blogr.location.LocationProvider;
import blogr.vpm.fr.blogr.persistence.FilePostSaver;
import blogr.vpm.fr.blogr.persistence.PostSaver;
import blogr.vpm.fr.blogr.picture.PictureMetadataProvider;
import blogr.vpm.fr.blogr.picture.PicturePickedListener;

/**
 * Created by vince on 13/03/15.
 */
public class PostMetadataFragment extends Fragment implements PicturePickedListener {

  public static final int PICK_PIC_REQ_CODE = 32;

  private PostSaver saver;

  private LocationProvider locationProvider;

  private AbsListView metadataList;

  private MetadataAdapter metadataAdapter;


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

    Log.d("postMDF", "creating fragment");
    // init services
    saver = new FilePostSaver(getActivity());
    locationProvider = new AndroidLocationProvider(getActivity());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_post_metadata, container, false);
    metadataList = (AbsListView) v.findViewById(R.id.allitems);
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
    locationProvider.connect();
  }

  @Override
  public void onPause() {
    super.onPause();
    refreshPostFromView();
    saveCurrentPost();
    locationProvider.disconnect();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.postmetadata, menu);
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_new:
        Map.Entry<String, Object> newEntry = new AbstractMap.SimpleEntry<String, Object>("", "");
        metadataAdapter.add(newEntry);
        return true;
      case R.id.action_insert_location:
        refreshPostFromView();
        new PlacePickerFragment().openPlacesPicker(getActivity(), getCurrentPost().getPlaces(), PostEditionActivity.REQ_MD);
        refreshViewFromPost();
        return true;
      case R.id.action_insert_picture:
        refreshPostFromView();
        Intent picIntent = new Intent(Intent.ACTION_PICK);
        picIntent.setType("image/*");
        startActivityForResult(picIntent, PICK_PIC_REQ_CODE);
        return true;
      case R.id.action_insert_flickr:
        FlickrProvider flickrD = new FlickrJAndroidProvider(getActivity());
        FlickrProvider flickrP = new FlickrJAsyncTaskProvider(getActivity(), flickrD);
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

  // called before onResume
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if ((PICK_PIC_REQ_CODE == requestCode) && (Activity.RESULT_OK == resultCode)) {
      Uri pictureUri = data.getData();
      getCurrentPost().addPicture(pictureUri);
      onPicturePicked(pictureUri.toString());
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override
  public void onPicturePicked(String picUrl) {
    MetadataProvider picProvider = new PictureMetadataProvider(picUrl);
    getCurrentPost().getMd().putData(picProvider.getMappings());
    refreshViewFromPost();
  }

  /**
   * Refreshes the view with the current Post
   */
  void refreshViewFromPost() {
    if (metadataAdapter == null) {
      getCurrentPost().getMd().addKeys(getCurrentPost().getBlog().getMdKeys());
      getActivity().setTitle(getCurrentPost().getTitle());
      Map<String, Object> postMd = (Map<String, Object>) getCurrentPost().getMd().getAsMap();
      metadataAdapter = new MetadataAdapter(getActivity(), postMd);
      metadataList.setAdapter(metadataAdapter);
    } else {
      metadataAdapter.clear();
      metadataAdapter.addAll(new ArrayList<Map.Entry<String, ?>>(getCurrentPost().getMd().getAsMap().entrySet()));
    }
  }

  /**
   * Refreshes the Post with the current view elements
   */
  private void refreshPostFromView() {
    if (getCurrentPost() != null) {
      getCurrentPost().setMd(new PostMetadata(metadataAdapter.getMd()));
    }
  }

  /**
   * Saves the post built from the view
   */
  private void saveCurrentPost() {
    // save only if post has content or title
    if ((getCurrentPost() != null) ) {
      saver.persist(getCurrentPost());
    }
  }

  public class MetadataAdapter extends ArrayAdapter<Map.Entry<String, ?>> {

    private final int resource;

    private final Map<String, Object> mds;

    // the views currently in focus
    private EditText focusedKeyView;
    private EditText focusedValueView;

    public MetadataAdapter(Context context, Map<String, Object> mds) {
      super(context, R.layout.metadata_item, new ArrayList<Map.Entry<String, ?>>(mds.entrySet()));
      this.mds = mds;
      this.resource = R.layout.metadata_item;
    }

    public Map<String, Object> getMd() {
      if ((focusedKeyView != null) && (focusedValueView != null)) {
        String key = focusedKeyView.getText().toString();
        String value = focusedValueView.getText().toString();
        if (!key.isEmpty()) {
          Log.d("md", "put(" + key + "=>" + value + ")");
          mds.put(key, value);
        }
      }
      Log.d("md", mds.toString());
      return mds;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder itemHolder;

      // retrieving the view
      if (convertView == null) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        convertView = layoutInflater.inflate(resource, parent, false);
        EditText mdKeyView = (EditText) convertView.findViewById(R.id.md_key);
        EditText mdValueView = (EditText) convertView.findViewById(R.id.md_value);

        itemHolder = new ViewHolder(mdKeyView, mdValueView);
        convertView.setTag(itemHolder);
      } else {
        itemHolder = (ViewHolder) convertView.getTag();
      }

      // retrieving the data
      Map.Entry<String, ?> md = getItem(position);

      // setting the view
      itemHolder.mdKeyView.setText(md.getKey());
      /*
      if (PostMetadata.IMMUTABLE_KEYS.contains(md.getKey())) {
        Log.d("key", "immutable");
        itemHolder.mdKeyView.setInputType(InputType.TYPE_NULL);
      }
      */
      itemHolder.mdValueView.setText(md.getValue().toString());
      if (PostMetadata.TRAVEL_DATE_KEY.equals(md.getKey())) {
        itemHolder.mdValueView.setHint("yyyy-MM-dd");
      } else {
        itemHolder.mdValueView.setHint("type a " + md.getKey());
      }

      return convertView;
    }

    /**
     * Keeps a reference to the views associated with a item. Only views should be stored there,
     * i.e. NO DATA should be linked to that object.
     */
    public class ViewHolder {

      final EditText mdKeyView;

      final EditText mdValueView;

      public ViewHolder(EditText mdKey, final EditText mdValue) {
        this.mdKeyView = mdKey;
        this.mdValueView = mdValue;

        View.OnFocusChangeListener onPairChanged = new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View view, boolean focused) {
            if (focused) {
              String key = mdKeyView.getText().toString();
              if (!key.isEmpty()) {
                mds.remove(key);
              }
              focusedKeyView = mdKeyView;
              focusedValueView = mdValueView;
            } else {
              String key = mdKeyView.getText().toString();
              String value = mdValueView.getText().toString();
              Log.d("md", "value is null : " + (mdValueView.getText() == null));
              if (!key.isEmpty()) {
                Log.d("md (focus)", "put(" + key + "=>" + value + ")");
                mds.put(key, value);
              }
              focusedKeyView = null;
              focusedValueView = null;
              refreshPostFromView();
            }
          }
        };
        mdKeyView.setOnFocusChangeListener(onPairChanged);
        mdValueView.setOnFocusChangeListener(onPairChanged);
      }
    }
  }

}

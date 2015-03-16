package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
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

import java.util.ArrayList;
import java.util.Map;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.bean.PostMetadata;
import blogr.vpm.fr.blogr.persistence.FilePostSaver;
import blogr.vpm.fr.blogr.persistence.PostSaver;

/**
 * Created by vince on 13/03/15.
 */
public class PostMetadataFragment extends Fragment {

  private PostSaver saver;

  private Post currentPost;

  private AbsListView metadataList;

  private MetadataAdapter metadataAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    Log.d("postMDF", "creating fragment");
    // init services
    saver = new FilePostSaver(getActivity());
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
    getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
    refreshViewFromPost();
  }

  @Override
  public void onPause() {
    super.onPause();
    refreshPostFromView();
    saveCurrentPost();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.postmetadata, menu);
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_settings:
        startActivity(new Intent(getActivity(), AllPreferencesActivity.class));
        return true;
      case R.id.action_save:
        refreshPostFromView();
        Intent i = new Intent();
        i.putExtra(Post.INTENT_EXTRA_KEY, currentPost);
        getActivity().setResult(Activity.RESULT_OK, i);
        getActivity().finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }


  /**
   * Refreshes the view with the current Post
   */
  private void refreshViewFromPost() {
    if (currentPost != null) {
      getActivity().setTitle(currentPost.getTitle());
      Map<String, Object> postMd = (Map<String, Object>) currentPost.getMd().getAsMap();
      metadataAdapter = new MetadataAdapter(getActivity(), postMd);
      metadataList.setAdapter(metadataAdapter);
    }
  }

  /**
   * Refreshes the Post with the current view elements
   */
  private void refreshPostFromView() {
    if (currentPost != null) {
      currentPost.setMd(new PostMetadata(metadataAdapter.getMd()));
    }
  }

  /**
   * Saves the post built from the view
   */
  private void saveCurrentPost() {
    // save only if post has content or title
    if ((currentPost != null) ) {
      saver.persist(currentPost);
    }
  }

  /**
   * Updates the current post with given instance and refreshes the view
   *
   * @param post The new post to edit
   */
  public void editPost(Post post) {
    currentPost = post;
    refreshViewFromPost();
    getActivity().invalidateOptionsMenu();
  }

  public class MetadataAdapter extends ArrayAdapter<Map.Entry<String, ?>> {

    private final int resource;

    private final Map<String, Object> mds;
    private Map<String, Parcelable> MD;

    public MetadataAdapter(Context context, Map<String, Object> mds) {
      super(context, R.layout.metadata_item, new ArrayList<Map.Entry<String, ?>>(mds.entrySet()));
      this.mds = mds;
      this.resource = R.layout.metadata_item;
    }

    public Map<String, Object> getMd() {
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
      if (PostMetadata.IMMUTABLE_KEYS.contains(md.getKey())) {
        Log.d("key", "immutable");
        itemHolder.mdKeyView.setInputType(InputType.TYPE_NULL);
      }
      itemHolder.mdValueView.setText(md.getValue().toString());

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
            if (!focused) {
              Log.d("changed", mdKeyView.getText().toString() + " => " + mdValueView.getText().toString());
              // TODO if we leave the fragment, this is not triggered
              mds.put(mdKeyView.getText().toString(), mdValueView.getText().toString());
            }
          }
        };
        mdKeyView.setOnFocusChangeListener(onPairChanged);
        mdValueView.setOnFocusChangeListener(onPairChanged);
      }
    }
  }

}

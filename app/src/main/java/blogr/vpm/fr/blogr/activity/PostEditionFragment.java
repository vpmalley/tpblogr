package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
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
import android.widget.EditText;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.format.AlignCenterTagsProvider;
import blogr.vpm.fr.blogr.format.AlignLeftTagsProvider;
import blogr.vpm.fr.blogr.format.AlignRightTagsProvider;
import blogr.vpm.fr.blogr.insertion.DefaultInserter;
import blogr.vpm.fr.blogr.insertion.Inserter;
import blogr.vpm.fr.blogr.location.AndroidLocationProvider;
import blogr.vpm.fr.blogr.location.LocationProvider;
import blogr.vpm.fr.blogr.location.PlaceTagMdProvider;
import blogr.vpm.fr.blogr.picture.PicturePickedListener;
import blogr.vpm.fr.blogr.picture.PictureTextileTagProvider;
import blogr.vpm.fr.blogr.publish.PostPublisher;
import blogr.vpm.fr.blogr.service.PostPublishingPreferencesProvider;
import blogr.vpm.fr.blogr.service.PostPublishingServiceProvider;

/**
 * Created by vincent on 07/10/14.
 */
public class PostEditionFragment extends Fragment {

  public static final int PICK_PIC_REQ_CODE = 32;

  private PostPublishingServiceProvider publisherProvider;

  private LocationProvider locationProvider;

  private Blog currentBlog;

  private EditText contentField;

  private EditText titleField;

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

    Log.d("postF", "creating fragment");
    // init services
    publisherProvider = new PostPublishingPreferencesProvider();
    locationProvider = new AndroidLocationProvider(getActivity());

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_post, container, false);
    contentField = (EditText) v.findViewById(R.id.postContent);
    contentField.setOnFocusChangeListener(new OnFocusChanged());
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
    locationProvider.disconnect();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.postedition, menu);
    titleField = (EditText) menu.findItem(R.id.action_title).getActionView();
    titleField.setOnFocusChangeListener(new OnFocusChanged());
    refreshViewFromPost();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    final Inserter tagsInserter = new DefaultInserter(getActivity());
    switch (item.getItemId()) {
      case R.id.action_settings:
        startActivity(new Intent(getActivity(), AllPreferencesActivity.class));
        return true;
      case R.id.action_publish:
        refreshPostFromView();
        ((PostEditionActivity) getActivity()).saveCurrentPost();
        PostPublisher publisher = currentBlog.getPublisherService(getActivity());
        publisher.publish(currentBlog, getCurrentPost());
        return true;
      case R.id.action_insert_location:
        new PlacePickerFragment().openPlacesPicker(getActivity(), getCurrentPost().getPlaces(), new PlacePickedListener() {
          @Override
          public void onPlacePicked(PlaceTagMdProvider provider) {
            tagsInserter.insert(contentField, provider);
            refreshPostFromView();
          }
        });
        return true;
      case R.id.action_insert_picture:
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, PICK_PIC_REQ_CODE);
        return true;
      case R.id.action_insert_flickr:
        new FlickrDialogFragment().openPicturePicker(getActivity(),
                getCurrentPost().getFlickrPictures().toArray(new ParcelableFlickrPhoto[getCurrentPost().getFlickrPictures().size()]),
                new PicturePickedListener() {
                  @Override
                  public void onPicturePicked(ParcelableFlickrPhoto pic) {
                    tagsInserter.insert(contentField, new PictureTextileTagProvider(pic));
                    refreshPostFromView();
                  }
                });
        return true;
      case R.id.action_align_left:
        tagsInserter.insert(contentField, new AlignLeftTagsProvider());
        return true;
      case R.id.action_align_center:
        tagsInserter.insert(contentField, new AlignCenterTagsProvider());
        return true;
      case R.id.action_align_right:
        tagsInserter.insert(contentField, new AlignRightTagsProvider());
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  // called before onResume
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d("onActResult", "RQCODE: " + requestCode);
    if ((PICK_PIC_REQ_CODE == requestCode) && (Activity.RESULT_OK == resultCode)) {
      Uri pictureUri = data.getData();
      getCurrentPost().addPicture(pictureUri);
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  /**
   * Updates the current post with given instance and refreshes the view
   *
   * @param post The new post to edit
   */
  public void editPost(Post post) {
    setCurrentPost(post);
    currentBlog = post.getBlog();
    refreshViewFromPost();
    getActivity().invalidateOptionsMenu();
  }

  /**
   * Refreshes the view with the current Post
   */
  void refreshViewFromPost() {
    if (getCurrentPost() != null) {
      if (titleField != null) {
        titleField.setText(getCurrentPost().getTitle());
      }
      if (contentField != null) {
        contentField.setText(getCurrentPost().getContent());
      }
    }
  }

  /**
   * Refreshes the Post with the current view elements
   */
  void refreshPostFromView() {
    if ((titleField != null) && (contentField != null)) {
      if (titleField.getText() != null) {
        getCurrentPost().setTitle(titleField.getText().toString());
      }
      if (contentField.getText() != null) {
        getCurrentPost().setContent(contentField.getText().toString());
      }
    }
  }

  private class OnFocusChanged implements View.OnFocusChangeListener {
    @Override
    public void onFocusChange(View view, boolean focused) {
      if (!focused) {
        refreshPostFromView();
        getActivity().setTitle(getCurrentPost().getTitle());
      }
    }
  }

}

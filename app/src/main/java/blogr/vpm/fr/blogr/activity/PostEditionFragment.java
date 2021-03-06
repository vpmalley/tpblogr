package blogr.vpm.fr.blogr.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.Toast;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.insertion.DefaultInserter;
import blogr.vpm.fr.blogr.insertion.Inserter;
import blogr.vpm.fr.blogr.location.AndroidLocationProvider;
import blogr.vpm.fr.blogr.location.LocationProvider;
import blogr.vpm.fr.blogr.location.PlaceTagMdProvider;
import blogr.vpm.fr.blogr.picture.Picture;
import blogr.vpm.fr.blogr.picture.PicturePickedListener;
import blogr.vpm.fr.blogr.picture.PictureTextileTagProvider;
import blogr.vpm.fr.blogr.publish.PostPublisher;
import blogr.vpm.fr.blogr.service.PostPublishingPreferencesProvider;
import blogr.vpm.fr.blogr.service.PostPublishingServiceProvider;

/**
 * Created by vincent on 07/10/14.
 *
 * Tab for editing content
 */
public class PostEditionFragment extends Fragment {

  private PostPublishingServiceProvider publisherProvider;

  private LocationProvider locationProvider;

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
    contentField.setCustomSelectionActionModeCallback(new PostContentEditionActions(new DefaultInserter(getActivity()), contentField));
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
        boolean hasPicsToUpload = false;
        for (Picture pic : getCurrentPost().getAllPictures()) {
          if (pic.shouldBeUploaded()) {
            hasPicsToUpload = true;
          }
        }
        if (hasPicsToUpload) {
          Toast.makeText(getActivity(), "First, pictures should be uploaded. " +
              "Please be patient, it might take minutes.", Toast.LENGTH_LONG).show();
        }
        new AsyncTask<Post, Integer, Post>() {
          @Override
          protected Post doInBackground(Post... posts) {
            if (posts.length == 1) {
              PostPublisher publisher = posts[0].getBlog().getPublisherService(getActivity());
              publisher.publish(posts[0].getBlog(), posts[0]);
            }
            return posts[0];
          }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getCurrentPost());
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
        new FlickrDialogFragment().openPicturePicker(getActivity(),
            getCurrentPost().getAllPictures().toArray(new Picture[getCurrentPost().getAllPictures().size()]),
            new PicturePickedListener() {
              @Override
              public void onPicturePicked(Picture pic) {
                tagsInserter.insert(contentField, new PictureTextileTagProvider(pic));
                refreshPostFromView();
              }
            });
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  /**
   * Updates the current post with given instance and refreshes the view
   *
   * @param post The new post to edit
   */
  public void editPost(Post post) {
    setCurrentPost(post);
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

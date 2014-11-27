package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.apis.flickr.FlickrJAndroidProvider;
import blogr.vpm.fr.blogr.apis.flickr.FlickrJAsyncTaskProvider;
import blogr.vpm.fr.blogr.apis.flickr.FlickrProvider;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.insertion.DefaultInserter;
import blogr.vpm.fr.blogr.insertion.Inserter;
import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;
import blogr.vpm.fr.blogr.location.LatLongTagProvider;
import blogr.vpm.fr.blogr.location.LocationProvider;
import blogr.vpm.fr.blogr.location.PlayServicesLocationProvider;
import blogr.vpm.fr.blogr.persistence.FilePostSaver;
import blogr.vpm.fr.blogr.persistence.PostSaver;
import blogr.vpm.fr.blogr.picture.PicturePickedListener;
import blogr.vpm.fr.blogr.publish.PostPublisher;
import blogr.vpm.fr.blogr.service.PostPublishingPreferencesProvider;
import blogr.vpm.fr.blogr.service.PostPublishingServiceProvider;

/**
 * Created by vincent on 07/10/14.
 */
public class PostEditionFragment extends Fragment implements PicturePickedListener {

  public static final int PICK_PIC_REQ_CODE = 32;
  public static final int MAX_NEW_POST_FILES = 100;

  private PostPublishingServiceProvider publisherProvider;

  private PostSaver saver;

  private LocationProvider locationProvider;

  private Blog currentBlog;

  private Post currentPost;

  private EditText contentField;

  private EditText titleField;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    Log.d("postF", "creating fragment");
    // init services
    publisherProvider = new PostPublishingPreferencesProvider();
    saver = new FilePostSaver(getActivity());
    locationProvider = new PlayServicesLocationProvider(getActivity());

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    currentBlog = new Blog(prefs.getString("pref_blog_name", ""), prefs.getString("pref_blog_email", ""));
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_post, container, false);
    contentField = (EditText) v.findViewById(R.id.postContent);
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
    inflater.inflate(R.menu.postedition, menu);
    titleField = (EditText) menu.findItem(R.id.action_title).getActionView();
    refreshViewFromPost();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.action_settings:
        startActivity(new Intent(getActivity(), AllPreferencesActivity.class));
        return true;
      case R.id.action_publish:
        PostPublisher publisher = publisherProvider.getPublisherService(getActivity());
        refreshPostFromView();
        publisher.publish(currentBlog, currentPost);
        return true;
      case R.id.action_insert_location:
        Inserter locationInserter = new DefaultInserter(getActivity());
        locationInserter.insert(contentField, new LatLongTagProvider(getActivity(), locationProvider));
        return true;
      case R.id.action_insert_picture:
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, PICK_PIC_REQ_CODE);
        return true;
      case R.id.action_insert_flickr:
        FlickrProvider flickrD = new FlickrJAndroidProvider(getActivity());
        FlickrProvider flickrP = new FlickrJAsyncTaskProvider(getActivity(), flickrD);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String flickrUsername = prefs.getString("pref_flickr_username", "");
        int picNb = Integer.valueOf(prefs.getString("pref_flickr_number_pics", "20"));
        flickrP.getUserPhotos(flickrUsername, picNb);
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
      currentPost.addPicture(pictureUri);
      onPicturePicked(pictureUri.toString());
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override
  public void onPicturePicked(String picUrl) {
    SurroundingTagsProvider pictureTagProvider = publisherProvider.getPictureTagsProvider(getActivity(), picUrl);
    String updatedPostContent = new DefaultInserter(getActivity()).insert(contentField, pictureTagProvider);
    // the currentPost must be updated because this may be called before onResume
    currentPost.setContent(updatedPostContent);
  }

  /**
   * Updates the current post with given instance and refreshes the view
   *
   * @param post The new post to edit
   */
  public void editPost(Post post) {
    currentPost = post;
    refreshViewFromPost();
  }

  /**
   * Refreshes the view with the current Post
   */
  private void refreshViewFromPost() {
    if (currentPost != null) {
      if (titleField != null) {
        titleField.setText(currentPost.getTitle());
      }
      if (contentField != null) {
        contentField.setText(currentPost.getContent());
      }
    }
  }

  /**
   * Refreshes the Post with the current view elements
   */
  private void refreshPostFromView() {
    if ((titleField != null) && (contentField != null)) {
      currentPost.setTitle(titleField.getText().toString());
      currentPost.setContent(contentField.getText().toString());
    }
  }

  /**
   * Saves the post built from the view
   */
  private void saveCurrentPost() {
    // save only if post has content or title
    if ((currentPost != null) && (!isPostTitleEmpty() || !isPostContentEmpty())) {
      if (isPostTitleEmpty()) {
        determineAvailablePostTitle();
      }
      saver.persist(currentPost);
    }
  }

  /**
   * Determines a title for the post that does not exist yet - in order not to override written post.
   */
  private void determineAvailablePostTitle() {
    String newPostTitle = getActivity().getResources().getString(R.string.newpost);
    currentPost.setTitle(newPostTitle);
    if (saver.exists(currentPost)) {
      for (int i = 1; saver.exists(currentPost) && (i < MAX_NEW_POST_FILES); i++) {
        currentPost.setTitle(newPostTitle + " " + i);
      }
    }
  }

  private boolean isPostContentEmpty() {
    return ("".equals(currentPost.getContent()));
  }

  private boolean isPostTitleEmpty() {
    return ("".equals(currentPost.getTitle()));
  }
}

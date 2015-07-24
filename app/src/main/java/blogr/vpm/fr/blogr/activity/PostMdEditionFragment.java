package blogr.vpm.fr.blogr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.bean.PostMetadata;
import blogr.vpm.fr.blogr.insertion.DefaultInserter;
import blogr.vpm.fr.blogr.insertion.Inserter;
import blogr.vpm.fr.blogr.insertion.SingleTagProvider;
import blogr.vpm.fr.blogr.location.PlaceTagMdProvider;
import blogr.vpm.fr.blogr.metadata.YamlMetadataExtracter;
import blogr.vpm.fr.blogr.metadata.YamlMetadataProvider;
import blogr.vpm.fr.blogr.picture.Picture;
import blogr.vpm.fr.blogr.picture.PicturePickedListener;

/**
 * Created by vincent on 07/10/14.
 * <p/>
 * Tab for editing content
 */
public class PostMdEditionFragment extends Fragment {

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
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.postedition, menu);
    titleField = (EditText) menu.findItem(R.id.action_title).getActionView();
    titleField.setOnFocusChangeListener(new OnFocusChanged());
    refreshViewFromPost();
    menu.findItem(R.id.action_publish).setVisible(false);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    final Inserter tagsInserter = new DefaultInserter(getActivity());
    switch (item.getItemId()) {
      case R.id.action_settings:
        startActivity(new Intent(getActivity(), AllPreferencesActivity.class));
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
              public void onPicturePicked(final Picture pic) {
                tagsInserter.insert(contentField, new SingleTagProvider() {
                  @Override
                  public String getTag() {
                    return pic.getUrlForInsertion();
                  }
                });
                refreshPostFromView();
              }
            });
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
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
        contentField.setText(new YamlMetadataProvider(getCurrentPost().getMd()).getTag());
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
        String mdContent = contentField.getText().toString();
        PostMetadata postMd = new YamlMetadataExtracter().readMd(mdContent);
        getCurrentPost().setMd(postMd);
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

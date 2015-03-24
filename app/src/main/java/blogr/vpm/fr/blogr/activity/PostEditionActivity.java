package blogr.vpm.fr.blogr.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.location.PlaceTagMdProvider;
import blogr.vpm.fr.blogr.persistence.FilePostSaver;
import blogr.vpm.fr.blogr.persistence.PostSaver;
import blogr.vpm.fr.blogr.picture.PicturePickedListener;

/**
 * Created by vince on 17/10/14.
 */
public class PostEditionActivity extends FragmentActivity implements PicturePickedListener, RefreshListener, PlacePickedListener {

  public static final int REQ_MD = 41;
  public static final int MAX_NEW_POST_FILES = 100;

  private PostEditionFragment postEditionFragment;

  private PostMetadataFragment postMetadataFragment;

  private PostPlacesFragment postPlacesFragment;

  private ViewPager viewPager;
  private Post currentPost;

  private PostSaver saver;

  public Post getCurrentPost() {
    return currentPost;
  }

  public void setCurrentPost(Post currentPost) {
    this.currentPost = currentPost;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post_edition);
    viewPager = (ViewPager) findViewById(R.id.pager);
    postEditionFragment = new PostEditionFragment();
    postMetadataFragment = new PostMetadataFragment();
    postPlacesFragment = new PostPlacesFragment();
    setTitle("");
    getActionBar().setDisplayHomeAsUpEnabled(true);

    if (getIntent().hasExtra(Post.INTENT_EXTRA_KEY)) {
      currentPost = (Post) getIntent().getExtras().get(Post.INTENT_EXTRA_KEY);
    }

    FragmentPagerAdapter rssItemAdapter = new PostPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(rssItemAdapter);
    if (savedInstanceState == null) {
      viewPager.setCurrentItem(0);
    }

    saver = new FilePostSaver(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    refreshPostFromView();
    saveCurrentPost();
  }

  @Override
  public void refreshViewFromPost() {
    setTitle(currentPost.getTitle());
    postPlacesFragment.refreshViewFromPost();
    postMetadataFragment.refreshViewFromPost();
  }

  public void refreshPostFromView() {
    postEditionFragment.refreshPostFromView();
    postMetadataFragment.refreshPostFromView();
  }

  /**
   * Saves the post built from the view
   */
  void saveCurrentPost() {
    // save only if post has content or title
    if ((getCurrentPost() != null) && (!isPostTitleEmpty() || !isPostContentEmpty())) {
      if (isPostTitleEmpty()) {
        determineAvailablePostTitle();
      }
      saver.persist(getCurrentPost());
    }
  }

  /**
   * Determines a title for the post that does not exist yet - in order not to override written post.
   */
  private void determineAvailablePostTitle() {
    String newPostTitle = getString(R.string.newpost);
    getCurrentPost().setTitle(newPostTitle);
    if (saver.exists(getCurrentPost())) {
      for (int i = 1; saver.exists(getCurrentPost()) && (i < MAX_NEW_POST_FILES); i++) {
        getCurrentPost().setTitle(newPostTitle + " " + i);
      }
    }
  }

  private boolean isPostContentEmpty() {
    return ("".equals(getCurrentPost().getContent()));
  }

  private boolean isPostTitleEmpty() {
    return ("".equals(getCurrentPost().getTitle()));
  }

  @Override
  public void onPicturePicked(String picUrl) {
    // This only delegates to the fragment
    postEditionFragment.onPicturePicked(picUrl);
  }

  @Override
  public void onPlacePicked(PlaceTagMdProvider provider, int request) {
    if (REQ_MD == request) {
      currentPost.getMd().putData(provider.getMappings());
    }
    refreshViewFromPost();
  }

  private class PostPagerAdapter extends FragmentPagerAdapter {

    public PostPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      Fragment f = null;
      if (0 == position) {
        f = postEditionFragment;
      } else if (1 == position) {
        f = postMetadataFragment;
      } else if (2 == position) {
        f = postPlacesFragment;
      }
      return f;
    }

    @Override
    public int getCount() {
      return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      String title = "";
      if (0 == position) {
        title = "Free text";
      } else if (1 == position) {
        title = "Metadata";
      } else if (2 == position) {
        title = "Places";
      }
      return title;
    }
  }
}

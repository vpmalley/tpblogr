package blogr.vpm.fr.blogr.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.persistence.FilePostSaver;
import blogr.vpm.fr.blogr.persistence.PostSaver;

/**
 * Created by vince on 17/10/14.
 */
public class PostEditionActivity extends FragmentActivity implements RefreshListener {

  public static final int REQ_MD = 41;
  public static final int MAX_NEW_POST_FILES = 100;

  private PostEditionFragment postEditionFragment;

  private PostMetadataFragment postMetadataFragment;

  private PostPlacesFragment postPlacesFragment;

  private PostPicturesFragment postPicturesFragment;

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
    postPicturesFragment = new PostPicturesFragment();
    setTitle("");
    if (getActionBar() != null) {
      getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    if (getIntent().hasExtra(Post.INTENT_EXTRA_KEY)) {
      currentPost = (Post) getIntent().getExtras().get(Post.INTENT_EXTRA_KEY);
    }

    FragmentPagerAdapter fragmentAdapter = new PostPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(fragmentAdapter);
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
    postPicturesFragment.refreshViewFromPost();
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

  private class PostPagerAdapter extends FragmentPagerAdapter {

    public PostPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      Fragment f = null;
      switch (position) {
        case 0:
          f = postEditionFragment;
          break;
        case 1:
          f = postMetadataFragment;
          break;
        case 2:
          f = postPlacesFragment;
          break;
        case 3:
          f = postPicturesFragment;
          break;
      }
      return f;
    }

    @Override
    public int getCount() {
      return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      String title = "";
      switch (position) {
        case 0:
          title = "Free text";
          break;
        case 1:
          title = "Metadata";
          break;
        case 2:
          title = "Places";
          break;
        case 3:
          title = "Pictures";
          break;
      }
      return title;
    }
  }
}

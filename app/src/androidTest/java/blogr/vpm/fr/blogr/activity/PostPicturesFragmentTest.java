package blogr.vpm.fr.blogr.activity;

import android.content.Intent;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.AbsListView;

import org.hamcrest.Matchers;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;
import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 06/04/15.
 * <p/>
 * Stub to test edition
 */
public class PostPicturesFragmentTest extends ActivityInstrumentationTestCase2<PostEditionActivity> {

  private static final String pic_url = "http://i.answers.microsoft.com/static/images/msheader.png";

  private PostEditionActivity editionActivity;

  private ViewPager viewPager;

  public PostPicturesFragmentTest() {
    super(PostEditionActivity.class);
  }

  public void setUp() throws Exception {
    super.setUp();
    setActivityInitialTouchMode(true);
    Intent i = new Intent();
    Post post = new Post("Hello", "Hello World", new GithubBlog("jojo"));
    post.addFlickrPicture(new ParcelableFlickrPhoto("Some picture", pic_url));
    i.putExtra(Post.INTENT_EXTRA_KEY, post);
    setActivityIntent(i);
    editionActivity = getActivity();
    viewPager = (ViewPager) editionActivity.findViewById(R.id.pager);
    editionActivity.runOnUiThread(new Runnable() {
      public void run() {
        viewPager.setCurrentItem(3);
      }
    });
    assertNotNull("Pager does not exist", viewPager);
    Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeRight());
    Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeLeft());
  }

  public void testPreConditions() {
    AbsListView pictures = (AbsListView) editionActivity.findViewById(R.id.pictures);
    assertNotNull("pictures does not exist", pictures);
    ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), pictures);
  }

  @MediumTest
  public void testSwipeAndBack() {
    AbsListView pictures = (AbsListView) editionActivity.findViewById(R.id.pictures);
    ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), pictures);

    Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeRight());
    Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeLeft());

    ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), pictures);
    assertEquals("Some picture", editionActivity.getCurrentPost().getFlickrPictures().get(0).getTitle());
    assertEquals(pic_url, editionActivity.getCurrentPost().getFlickrPictures().get(0).getUrlForInsertion());
  }

  @MediumTest
  public void testPictureClick() {
    DataInteraction dataInteraction = Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(R.id.pictures)).atPosition(0);
    dataInteraction.check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("Some picture"))));
    dataInteraction.perform(ViewActions.click());
  }

  @MediumTest
  public void testInsertFlickrPicture() {
    DataInteraction dataInteraction = Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(R.id.pictures)).atPosition(0);
    dataInteraction.check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("Some picture"))));
    dataInteraction.perform(ViewActions.click());

    Espresso.onView(ViewMatchers.withId(R.id.action_new)).perform(ViewActions.click());
    Espresso.onView(ViewMatchers.withText(R.string.action_insert_flickr)).perform(ViewActions.click());
    DataInteraction dataInteraction1 = Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withClassName(Matchers.containsString("ListView"))).atPosition(0);
    dataInteraction1.perform(ViewActions.click());

    assertEquals(2, editionActivity.getCurrentPost().getFlickrPictures().size());
    DataInteraction dataInteraction2 = Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(R.id.pictures)).atPosition(1);
    dataInteraction2.check(ViewAssertions.matches(ViewMatchers.withText(editionActivity.getCurrentPost().getFlickrPictures().get(1).getTitle())));
  }

  /*
  @MediumTest
  public void testInsertGalleryPicture() {
    DataInteraction dataInteraction = Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(R.id.pictures)).atPosition(0);
    dataInteraction.check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("Some picture"))));
    dataInteraction.perform(ViewActions.click());

    Espresso.onView(ViewMatchers.withId(R.id.action_new)).perform(ViewActions.click());
    Espresso.onView(ViewMatchers.withText(R.string.action_insert_gallery)).perform(ViewActions.click());

    // TODO use a ActivityUnitTestCase

    Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo("ImageView"))).perform(ViewActions.click());

    assertEquals(2, editionActivity.getCurrentPost().getFlickrPictures().size());
    DataInteraction dataInteraction2 = Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(R.id.pictures)).atPosition(1);
    dataInteraction2.check(ViewAssertions.matches(ViewMatchers.withText(editionActivity.getCurrentPost().getFlickrPictures().get(1).getTitle())));
  }
  */
}

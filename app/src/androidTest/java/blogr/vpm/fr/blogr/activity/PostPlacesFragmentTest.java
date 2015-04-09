package blogr.vpm.fr.blogr.activity;

import android.content.Intent;
import android.location.Location;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.AbsListView;

import org.hamcrest.Matchers;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 06/04/15.
 *
 * Stub to test edition
 */
public class PostPlacesFragmentTest extends ActivityInstrumentationTestCase2<PostEditionActivity> {

    private PostEditionActivity editionActivity;

    private ViewPager viewPager;

    public PostPlacesFragmentTest() {
        super(PostEditionActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        Intent i = new Intent();
        Post post = new Post("Hello", "Hello World", new GithubBlog("jojo"));
        post.addPlace(new Location("jesaisoujesuis"));
        i.putExtra(Post.INTENT_EXTRA_KEY, post);
        setActivityIntent(i);
        editionActivity = getActivity();
        viewPager = (ViewPager) editionActivity.findViewById(R.id.pager);
        editionActivity.runOnUiThread(new Runnable() {
            public void run() {
                viewPager.setCurrentItem(2);
            }
        });
        assertNotNull("Pager does not exist", viewPager);
        Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeRight());
        Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeLeft());
    }

    public void testPreConditions() {
        AbsListView places = (AbsListView) editionActivity.findViewById(R.id.places);
        assertNotNull("places does not exist", places);
        ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), places);
    }

    @MediumTest
    public void testSwipeAndBack() {
        AbsListView places = (AbsListView) editionActivity.findViewById(R.id.places);
        ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), places);

        Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeRight());
        Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeLeft());

        ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), places);
        assertEquals(0.0, editionActivity.getCurrentPost().getPlaces().get(0).getLatitude());
        assertEquals(0.0, editionActivity.getCurrentPost().getPlaces().get(0).getLongitude());
    }

    @MediumTest
    public void testPlaceClick() {
        DataInteraction dataInteraction = Espresso.onData(Matchers.anything());
        DataInteraction dataInteraction1 = dataInteraction.inAdapterView(ViewMatchers.withId(R.id.places));
        DataInteraction dataInteraction2 = dataInteraction1.atPosition(0);
        dataInteraction2.perform(ViewActions.click());
    }



}

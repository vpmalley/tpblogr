package blogr.vpm.fr.blogr.activity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
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

import java.util.HashMap;
import java.util.Map;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;
import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.bean.PostMetadata;
import blogr.vpm.fr.blogr.picture.LocalPicture;

/**
 * Created by vince on 06/04/15.
 *
 * Stub to test edition
 */
public class PostMetadataFragmentTest extends ActivityInstrumentationTestCase2<PostEditionActivity> {

    private static final String PIC_URI = "content://media/external/images/media/300";
    private static final String PIC_URL = "http://i.answers.microsoft.com/static/images/msheader.png";

    private PostEditionActivity editionActivity;

    private ViewPager viewPager;


    public PostMetadataFragmentTest() {
        super(PostEditionActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        Intent i = new Intent();
        Post post = new Post("Hello", "Hello World", new GithubBlog("jojo"));
        Map<String, Object> md = new HashMap<>();
        md.put("title", "mytitle");
        post.setMd(new PostMetadata(md));
        Location location = new Location("jesaisoujesuis");
        location.setLatitude(23);
        post.addPlace(location);
        post.addPicture(new ParcelableFlickrPhoto("Some picture", PIC_URL));
        post.addPicture(new LocalPicture(Uri.parse(PIC_URI)));
        i.putExtra(Post.INTENT_EXTRA_KEY, post);
        setActivityIntent(i);
        editionActivity = getActivity();
        viewPager = (ViewPager) editionActivity.findViewById(R.id.pager);
        editionActivity.runOnUiThread(new Runnable() {
            public void run() {
                viewPager.setCurrentItem(1);
            }
        });
        assertNotNull("Pager does not exist", viewPager);
        Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeRight());
        Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeLeft());
    }

    public void testPreConditions() {
        assertNotNull("Pager does not exist", viewPager);
        AbsListView mdItems = (AbsListView) editionActivity.findViewById(R.id.allitems);
        ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), mdItems);
        assertEquals("mytitle", editionActivity.getCurrentPost().getMd().getAsMap().get("title"));
        for (Map.Entry<String, ?> pair : editionActivity.getCurrentPost().getMd().getAsMap().entrySet()) {
            assertNotNull(pair.getValue());
        }
    }

    @MediumTest
    public void testTitleEdition() {
        AbsListView mdItems = (AbsListView) editionActivity.findViewById(R.id.allitems);
        ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), mdItems);

        DataInteraction mdValueInteraction = Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(R.id.allitems)).atPosition(0)
            .onChildView(ViewMatchers.withId(R.id.md_value));
        mdValueInteraction.perform(ViewActions.click());
        mdValueInteraction.perform(ViewActions.replaceText("Bonjour"));

        Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeRight());
        Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeLeft());

        mdValueInteraction.check(ViewAssertions.matches(ViewMatchers.withText("Bonjour")));
        assertTrue("MD do not contain the value 'Bonjour'", editionActivity.getCurrentPost().getMd().getAsMap().containsValue("Bonjour"));
    }

    @MediumTest
    public void testSwipeAndBack() {
        AbsListView mdItems = (AbsListView) editionActivity.findViewById(R.id.allitems);
        ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), mdItems);
        assertEquals("mytitle", editionActivity.getCurrentPost().getMd().getAsMap().get("title"));

        Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeRight());
        Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeLeft());

        ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), mdItems);
        assertEquals("mytitle", editionActivity.getCurrentPost().getMd().getAsMap().get("title"));
    }

    @MediumTest
    public void testInsertPlace() {
        DataInteraction mdValueInteraction = Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(R.id.allitems)).atPosition(0)
            .onChildView(ViewMatchers.withId(R.id.md_value));
        mdValueInteraction.perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.action_insert)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(R.string.action_insert_location)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("Lat. 23.0, Lon. 0.0")).perform(ViewActions.click());

        assertEquals("23.0", editionActivity.getCurrentPost().getMd().getAsMap().get("latitude"));
        assertEquals("0.0", editionActivity.getCurrentPost().getMd().getAsMap().get("longitude"));

        Espresso.onView(ViewMatchers.withText("latitude")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("longitude")).perform(ViewActions.click());
    }

    @MediumTest
    public void testInsertFlickrPicture() {
        DataInteraction mdValueInteraction = Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(R.id.allitems)).atPosition(0)
            .onChildView(ViewMatchers.withId(R.id.md_value));
        mdValueInteraction.perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.action_insert)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(R.string.action_insert_picture)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("Some picture")).perform(ViewActions.click());

        assertEquals("Some picture", editionActivity.getCurrentPost().getMd().getAsMap().get("picalt"));
        assertEquals(PIC_URL, editionActivity.getCurrentPost().getMd().getAsMap().get("pic"));

        Espresso.onView(ViewMatchers.withText("pic")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("picalt")).perform(ViewActions.click());
    }

    @MediumTest
    public void testInsertGalleryPicture() {
        DataInteraction mdValueInteraction = Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(R.id.allitems)).atPosition(0)
            .onChildView(ViewMatchers.withId(R.id.md_value));
        mdValueInteraction.perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.action_insert)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(R.string.action_insert_picture)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("untitled")).perform(ViewActions.click());

        assertEquals("untitled", editionActivity.getCurrentPost().getMd().getAsMap().get("picalt"));
        assertEquals("placeholder-" + PIC_URI, editionActivity.getCurrentPost().getMd().getAsMap().get("pic"));

        Espresso.onView(ViewMatchers.withText("pic")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("picalt")).perform(ViewActions.click());
    }

    @MediumTest
    public void testInsertNewRow() {
        AbsListView mdItems = (AbsListView) editionActivity.findViewById(R.id.allitems);
        int initSize = mdItems.getAdapter().getCount();

        DataInteraction mdValueInteraction = Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(R.id.allitems)).atPosition(0)
            .onChildView(ViewMatchers.withId(R.id.md_value));
        mdValueInteraction.perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.action_new)).perform(ViewActions.click());

        assertEquals(initSize + 1, mdItems.getAdapter().getCount());
    }
}

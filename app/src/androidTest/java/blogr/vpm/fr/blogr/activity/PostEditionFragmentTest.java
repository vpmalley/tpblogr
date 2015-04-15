package blogr.vpm.fr.blogr.activity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.EditText;

import org.hamcrest.Matchers;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;
import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.picture.LocalPicture;

/**
 * Created by vince on 06/04/15.
 *
 * Stub to test edition
 */
public class PostEditionFragmentTest extends ActivityInstrumentationTestCase2<PostEditionActivity> {

    private PostEditionActivity editionActivity;

    private ViewPager viewPager;

    private static final String PIC_URI = "content://media/external/images/media/300";

    private static final String PIC_URL = "http://i.answers.microsoft.com/static/images/msheader.png";

    public PostEditionFragmentTest() {
        super(PostEditionActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        Intent i = new Intent();
        Post post = new Post("Hello", "Hello World", new GithubBlog("jojo"));
        Location location = new Location("jesaisoujesuis");
        location.setLatitude(23);
        post.addPlace(location);
        post.addFlickrPicture(new ParcelableFlickrPhoto("Some picture", PIC_URL));
        post.addPicture(new LocalPicture(Uri.parse(PIC_URI)));
        i.putExtra(Post.INTENT_EXTRA_KEY, post);
        setActivityIntent(i);
        editionActivity = getActivity();
        viewPager = (ViewPager) editionActivity.findViewById(R.id.pager);
    }

    public void testPreConditions() {
        assertNotNull("Pager does not exist", viewPager);
        EditText postContent = (EditText) editionActivity.findViewById(R.id.postContent);
        ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), postContent);
    }

    @MediumTest
    public void testTitleEdition() {
        EditText postTitle = (EditText) editionActivity.findViewById(R.id.postTitle);
        ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), postTitle);

        Espresso.onView(ViewMatchers.withId(R.id.postTitle)).perform(ViewActions.clearText());
        Espresso.onView(ViewMatchers.withId(R.id.postTitle)).perform(ViewActions.typeText("Bonjour"));
        Espresso.onView(ViewMatchers.withId(R.id.postContent)).perform(ViewActions.click());

        assertEquals("Bonjour", editionActivity.getCurrentPost().getTitle());
    }

    @MediumTest
    public void testPostEdition() {
        EditText postTitle = (EditText) editionActivity.findViewById(R.id.postTitle);
        ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), postTitle);

        Espresso.onView(ViewMatchers.withId(R.id.postContent)).perform(ViewActions.typeText("\n I feel good"));
        Espresso.onView(ViewMatchers.withId(R.id.postTitle)).perform(ViewActions.click());

        assertTrue(editionActivity.getCurrentPost().getContent().contains("Hello World\n I feel good"));
    }

    @MediumTest
    public void testSwipeAndBack() {
        EditText postTitle = (EditText) editionActivity.findViewById(R.id.postTitle);
        ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), postTitle);

        Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeRight());
        Espresso.onView(ViewMatchers.withId(R.id.pager)).perform(ViewActions.swipeLeft());
        ViewAsserts.assertOnScreen(editionActivity.getWindow().getDecorView(), postTitle);

        assertEquals("Hello", editionActivity.getCurrentPost().getTitle());
        assertEquals("Hello World", editionActivity.getCurrentPost().getContent());
    }

    @MediumTest
    public void testInsertPlace() {
        Espresso.onView(ViewMatchers.withId(R.id.postContent)).perform(ViewActions.click());

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        Espresso.onView(ViewMatchers.withText(R.string.action_insert)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(R.string.action_insert_location)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("Lat. 23.0, Lon. 0.0")).perform(ViewActions.click());

        assertTrue(editionActivity.getCurrentPost().getContent().contains("23.0"));
        assertTrue(editionActivity.getCurrentPost().getContent().contains("0.0"));

        Espresso.onView(ViewMatchers.withId(R.id.postContent)).
            check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("23.0"))));
        Espresso.onView(ViewMatchers.withId(R.id.postContent)).
            check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("0.0"))));
    }

    @MediumTest
    public void testInsertFlickrPicture() {
        Espresso.onView(ViewMatchers.withId(R.id.postContent)).perform(ViewActions.click());

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        Espresso.onView(ViewMatchers.withText(R.string.action_insert)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(R.string.action_insert_picture)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("Some picture")).perform(ViewActions.click());

        assertTrue(editionActivity.getCurrentPost().getContent().contains("Some picture"));
        assertTrue(editionActivity.getCurrentPost().getContent().contains(PIC_URL));

        Espresso.onView(ViewMatchers.withId(R.id.postContent)).
            check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("Some picture"))));
        Espresso.onView(ViewMatchers.withId(R.id.postContent)).
            check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString(PIC_URL))));
    }

    @MediumTest
    public void testInsertGalleryPicture() {
        Espresso.onView(ViewMatchers.withId(R.id.postContent)).perform(ViewActions.click());

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        Espresso.onView(ViewMatchers.withText(R.string.action_insert)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(R.string.action_insert_picture)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withText("untitled")).perform(ViewActions.click());

        assertTrue(editionActivity.getCurrentPost().getContent().contains("untitled"));
        assertTrue(editionActivity.getCurrentPost().getContent().contains(PIC_URI));

        Espresso.onView(ViewMatchers.withId(R.id.postContent)).
            check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("untitled"))));
        Espresso.onView(ViewMatchers.withId(R.id.postContent)).
            check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString(PIC_URI))));
    }


}

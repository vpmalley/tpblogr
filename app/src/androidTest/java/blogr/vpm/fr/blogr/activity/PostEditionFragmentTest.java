package blogr.vpm.fr.blogr.activity;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.EditText;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.activity.PostEditionActivity;
import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 06/04/15.
 *
 * Stub to test edition
 */
public class PostEditionFragmentTest extends ActivityInstrumentationTestCase2<PostEditionActivity> {

    private PostEditionActivity editionActivity;

    private ViewPager viewPager;

    public PostEditionFragmentTest() {
        super(PostEditionActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        Intent i = new Intent();
        i.putExtra(Post.INTENT_EXTRA_KEY, new Post("Hello", "Hello World", new GithubBlog("jojo")));
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




}

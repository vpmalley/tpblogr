package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.widget.EditText;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 06/04/15.
 *
 * Stub to test edition
 */
public class PostEditionActivityTest extends ActivityInstrumentationTestCase2<PostEditionActivity> {

    private Activity testActivity;

    private ViewPager viewPager;

    public PostEditionActivityTest() {
        super(PostEditionActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        Intent i = new Intent();
        i.putExtra(Post.INTENT_EXTRA_KEY, new Post("Hello", "Hello World", new GithubBlog("jojo")));
        setActivityIntent(i);
        testActivity = getActivity();
        viewPager = (ViewPager) testActivity.findViewById(R.id.pager);
    }

    public void testEditionView() {
        assertNotNull("Pager does not exist", viewPager);
        EditText postContent = (EditText) testActivity.findViewById(R.id.postContent);
        ViewAsserts.assertOnScreen(testActivity.getWindow().getDecorView(), postContent);
    }
}

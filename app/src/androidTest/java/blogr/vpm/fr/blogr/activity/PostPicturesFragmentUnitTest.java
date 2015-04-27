package blogr.vpm.fr.blogr.activity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;

import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;
import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 06/04/15.
 * <p/>
 * Stub to test edition
 */
public class PostPicturesFragmentUnitTest extends ActivityUnitTestCase<PostEditionActivity> {

  private static final String pic_url = "http://i.answers.microsoft.com/static/images/msheader.png";

  private PostEditionActivity editionActivity;

  public PostPicturesFragmentUnitTest() {
    super(PostEditionActivity.class);
  }

  public void setUp() throws Exception {
    super.setUp();
  }

  @MediumTest
  @UiThreadTest
  public void testInsertGalleryPicture() {
    Intent intent = new Intent(getInstrumentation().getTargetContext(),
        PostEditionActivity.class);
    Post post = new Post("Hello", "Hello World", new GithubBlog("jojo"));
    post.addFlickrPicture(new ParcelableFlickrPhoto("Some picture", pic_url));
    intent.putExtra(Post.INTENT_EXTRA_KEY, post);
    startActivity(intent, null, null);
    editionActivity = getActivity();

    // find a way to swipe to pictures fragment and simulate a click on the "pick gallery picture" button
    Intent launchedIntent = getStartedActivityIntent();
    assertEquals(Intent.ACTION_VIEW, launchedIntent.getAction());
  }

}

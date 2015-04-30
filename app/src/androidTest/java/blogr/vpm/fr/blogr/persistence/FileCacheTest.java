package blogr.vpm.fr.blogr.persistence;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;

import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by vince on 30/04/15.
 */
public class FileCacheTest extends AndroidTestCase {

  private static final String pic_url = "http://i.answers.microsoft.com/static/images/msheader.png";

  FileCache cache;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    cache = new FileCache();
    cache.clear();
    assertEquals(0, cache.size());
  }

  public void testGetEmptyCache() {
    assertNull(cache.get("non_existing_key"));
  }

  public void testSetThenGetCache() {
    Bitmap cachedBitmap = createBitmap();
    cache.set("some_key", cachedBitmap);
    assertEquals(1, cache.size());
    assertNotNull(cache.get("some_key"));
    assertEquals(cachedBitmap.getHeight(), cache.get("some_key").getHeight());
    assertEquals(cachedBitmap.getWidth(), cache.get("some_key").getWidth());
  }

  public void testSetThenClearThenGetCache() {
    Bitmap cachedBitmap = createBitmap();
    cache.set("some_key", cachedBitmap);
    assertEquals(1, cache.size());
    cache.clear();
    assertEquals(0, cache.size());
    assertNull(cache.get("some_key"));
  }


  public void testSetThenClearThenGetCacheEntry() {
    Bitmap cachedBitmap = createBitmap();
    cache.set("some_key", cachedBitmap);
    Bitmap anotherBitmap = createBitmap();
    cache.set("another_key", anotherBitmap);
    assertEquals(2, cache.size());
    cache.clearKeyUri("some_key");
    assertEquals(1, cache.size());
    assertNull(cache.get("some_key"));
    assertNotNull(cache.get("another_key"));
    assertEquals(anotherBitmap.getHeight(), cache.get("another_key").getHeight());
    assertEquals(anotherBitmap.getWidth(), cache.get("another_key").getWidth());
  }

  private Bitmap createBitmap() {
    Bitmap b = null;
    try {
      b = Picasso.with(getContext()).load(pic_url).get();
    } catch (IOException e) {
      fail("could not get bitmap from online pic");
    }
    return b;
  }

}

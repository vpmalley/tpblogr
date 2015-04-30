package blogr.vpm.fr.blogr.persistence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by vince on 30/04/15.
 * <p/>
 * Cache for Square Picasso that stores pictures as files in the external storage.
 */
public class FileCache implements Cache {

  public static final String PIC_CACHE_DIR = "cachedPics";
  public static final String APP_DIR = "BlogR";
  public static final int MAX_ITEM_SIZE = 50;

  private final String appDirName;
  private final String cacheDirName;
  private final int maxItems;
  private File cacheDir;

  /**
   * Constructs the cache with specific configuration
   *
   * @param appDir   the directory at the root of the device external storage, expected to reflect the app name
   * @param cacheDir the directory inside the app directory, used for caching the pictures
   * @param maxItems the maximum number of items to cache
   */
  public FileCache(String appDir, String cacheDir, int maxItems) {
    this.appDirName = appDir;
    this.cacheDirName = cacheDir;
    initCacheDir();
    this.maxItems = maxItems;
  }

  /**
   * Default constructor, sets default configurations:
   * The cache directory will be called "cachedPics" inside an app directory, at the root of storage.
   * The cache will be able to store 50 items.
   */
  public FileCache() {
    this.appDirName = APP_DIR;
    this.cacheDirName = PIC_CACHE_DIR;
    initCacheDir();
    maxItems = MAX_ITEM_SIZE;
  }

  @Override
  public Bitmap get(String key) {
    Bitmap b = null;
    if (isExternalStorageReadable()) {
      File picFile = new File(cacheDir, key);
      if (picFile.exists()) {
        try {
          FileInputStream picStream = new FileInputStream(picFile);
          b = BitmapFactory.decodeStream(picStream);
        } catch (FileNotFoundException e) {
          Log.w("fileCache", "cannot retrieve picture");
        }
      }
    }
    return b;
  }

  @Override
  public void set(String key, Bitmap bitmap) {
    writeBitmapToFile(key, bitmap);
  }

  @Override
  public int size() {
    int nbEntries = 0;
    File[] cacheFileNames = cacheDir.listFiles();
    if (cacheFileNames != null) {
      nbEntries = cacheFileNames.length;
    }
    return nbEntries;
  }

  @Override
  public int maxSize() {
    return maxItems;
  }

  @Override
  public void clear() {
    if (isExternalStorageWritable()) {
      for (File f : cacheDir.listFiles()) {
        f.delete();
      }
    }
  }

  @Override
  public void clearKeyUri(String keyPrefix) {
    if (isExternalStorageWritable()) {
      File picFile = new File(cacheDir, keyPrefix);
      if (picFile.exists()) {
        picFile.delete();
      }
    }

  }

  private void initCacheDir() {
    this.cacheDir = new File(Environment.getExternalStoragePublicDirectory(appDirName), cacheDirName);
    if (!this.cacheDir.exists()) {
      this.cacheDir.mkdirs();
    }
  }

  private void writeBitmapToFile(String cacheKey, Bitmap picToCache) {
    if (isExternalStorageWritable()) {
      File picFile = new File(cacheDir, cacheKey);
      if (!picFile.exists()) {
        try {
          picFile.createNewFile();
        } catch (IOException e) {
          Log.w("fileCache", "Cannot cache picture");
        }
      }
      FileOutputStream picStream = null;
      try {
        picStream = new FileOutputStream(picFile);
        picToCache.compress(Bitmap.CompressFormat.PNG, 80, picStream);
      } catch (FileNotFoundException e) {
        Log.w("fileCache", "Cannot cache picture");
      } finally {
        if (picStream != null) {
          try {
            picStream.close();
          } catch (IOException e) {
            Log.w("fileCache", "May not cache picture");
          }
        }
      }
    }
  }

  /**
   * Finds out whether the external storage is writable
   *
   * @return whether it is writable
   */
  private boolean isExternalStorageWritable() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }

  /**
   * Finds out whether the external storage is readable
   *
   * @return whether it is readable
   */
  private boolean isExternalStorageReadable() {
    return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()));
  }
}

package blogr.vpm.fr.blogr.persistence;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.insertion.DefaultInserter;
import blogr.vpm.fr.blogr.picture.Picture;
import blogr.vpm.fr.blogr.picture.PictureDeserializer;
import blogr.vpm.fr.blogr.picture.PictureSerializer;

/**
 * Created by vincent on 06/10/14.
 */
public class FilePostSaver implements PostSaver {

  public static final String BLOGS_DIR = "blogs";
  public static final String APP_DIR = "BlogR";
  private final Context context;

  public FilePostSaver(Context context) {
    this.context = context;
  }

  @Override
  public boolean exists(Post post) {
    File postFile = new FileManager().getFileForPost(context, post);
    return postFile.exists();
  }

  @Override
  public boolean persist(Post post) {
    post = new Post(post);
    boolean saved = false;
    if (isExternalStorageWritable()) {
      File postFile = new FileManager().getFileForPost(context, post);
      if (!postFile.exists()) {
        try {
          postFile.createNewFile();
        } catch (IOException e) {
          Toast.makeText(context, context.getResources().getString(R.string.cannotsavepost), Toast.LENGTH_SHORT).show();
        }
      }
      if (post.getBlog().hasMetadataProvider()) {
        new DefaultInserter(context).prepend(post, post.getBlog().getMetadataProvider(post));
      }
      saved = fillFile(postFile, post.getContent());
      saved &= persistPlaces(post);
    }
    return saved;
  }

  private boolean persistPlaces(Post post) {
    File postFile = new FileManager().getDataFileForPost(context, post);
    if (!postFile.exists()) {
      try {
        postFile.createNewFile();
      } catch (IOException e) {
        Toast.makeText(context, context.getResources().getString(R.string.cannotsavepost), Toast.LENGTH_SHORT).show();
      }
    }
    Gson gson = new GsonBuilder().registerTypeAdapter(Picture.class, new PictureSerializer()).
        registerTypeAdapter(Picture.class, new PictureDeserializer()).excludeFieldsWithoutExposeAnnotation().create();
    Log.d("pre-serializing", "pictures: " + post.getAllPictures().size());
    String serializedPost = gson.toJson(post);
    Log.d("postserializing", serializedPost);
    return fillFile(postFile, serializedPost);
  }

  private boolean fillFile(File postFile, String serializedPlaces) {
    boolean saved = false;
    FileOutputStream postFileOut = null;
    try {
      postFileOut = new FileOutputStream(postFile);
      IOUtils.copy(new StringReader(serializedPlaces), postFileOut, "UTF-8");
      saved = true;
    } catch (IOException e) {
      Toast.makeText(context, context.getResources().getString(R.string.cannotsavepost), Toast.LENGTH_SHORT).show();
    } finally {
      if (postFileOut != null) {
        try {
          postFileOut.close();
        } catch (IOException e) {
          Toast.makeText(context, context.getResources().getString(R.string.mightnotsavepost), Toast.LENGTH_SHORT).show();
        }
      }
    }
    return saved;
  }

  @Override
  public boolean delete(Post post) {
    boolean deleted = false;
    if (isExternalStorageWritable()) {
      File postFile = new FileManager().getFileForPost(context, post);
      deleted = postFile.delete();
    }
    return deleted;
  }

  /**
   * Finds out whether the external storage is writable
   *
   * @return whether it is writable
   */
  private boolean isExternalStorageWritable() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }
}

package blogr.vpm.fr.blogr.persistence;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 06/10/14.
 */
public class FilePostSaver implements PostSaver {

    private final Context context;

    public FilePostSaver(Context context) {
        this.context = context;
    }

    @Override
    public boolean persist(Post post) {
        boolean saved = false;
        String postTitle = post.getTitle();
        if (postTitle.isEmpty()) {
            postTitle = "New_Post";
        }
        if (isExternalStorageWritable()) {
            // create directory if non existent
            File dir = new File(Environment.getExternalStoragePublicDirectory("BlogR"), "posts");
            dir.mkdirs();
            // create file if non existent
            File postFile = new File(dir, postTitle.replace(' ', '_') + ".txt");
            try {
                postFile.createNewFile();
            } catch (IOException e) {
                Toast.makeText(context, "Could not save post", Toast.LENGTH_SHORT).show();
            }
            // fill file
            FileOutputStream postFileOut = null;
            try {
                postFileOut = new FileOutputStream(postFile);
                IOUtils.copy(new StringReader(post.getContent()), postFileOut, "UTF-8");
                saved = true;
            } catch (IOException e) {
                Toast.makeText(context, "Could not save post", Toast.LENGTH_SHORT).show();
            } finally {
                if (postFileOut != null) {
                    try {
                        postFileOut.close();
                    } catch (IOException e) {
                        Toast.makeText(context, "Might not save post", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        return saved;
    }

    private boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}

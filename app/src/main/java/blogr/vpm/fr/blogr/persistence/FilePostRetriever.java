package blogr.vpm.fr.blogr.persistence;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 08/10/14.
 */
public class FilePostRetriever implements PostRetriever{

    private final Context context;

    public FilePostRetriever(Context context) {
        this.context = context;
    }

    @Override
    public List<Post> retrieveAll() {
        List<Post> posts = new ArrayList<Post>();
        if (isExternalStorageReadable()) {
            File postDir = new File(Environment.getExternalStoragePublicDirectory("BlogR"), "posts");
            if (postDir.exists() && postDir.isDirectory()) {
                for (File postFile : postDir.listFiles()) {
                    StringWriter postWriter = new StringWriter();
                    FileInputStream postFileIn = null;
                    try {
                        postFileIn = new FileInputStream(postFile);
                        IOUtils.copy(postFileIn, postWriter, "UTF-8");
                    } catch (IOException e) {
                        Toast.makeText(context, "Could not retrieve post", Toast.LENGTH_SHORT).show();
                    } finally {
                        if (postFileIn != null) {
                            try {
                                postFileIn.close();
                            } catch (IOException e) {
                                Toast.makeText(context, "Might not retrieve post", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    posts.add(new Post(postFile.getName().replace(".txt", "").replace('_', ' '), postWriter.toString()));
                }
            }
        }
        return posts;
    }

    private boolean isExternalStorageReadable() {
        String storageState = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(storageState) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(storageState));
    }


}

package blogr.vpm.fr.blogr.persistence;

import android.content.Context;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vincent on 06/10/14.
 */
public class FilePostSaver implements PostSaver{

    private final Context context;

    public FilePostSaver(Context context) {
        this.context = context;
    }

    @Override
    public boolean persist(Post post) {
        boolean saved = false;
        File postFile = new File(context.getExternalFilesDir("posts"), post.getTitle().replace(' ', '_'));
        FileOutputStream postFileOut = null;
        try {
            postFileOut = new FileOutputStream(postFile);
            IOUtils.copy(new StringReader(post.getContent()), postFileOut, "UTF-8");
            saved = true;
        } catch (IOException e) {
            Toast.makeText(context, "Could not save post", Toast.LENGTH_SHORT);
        } finally {
            if (postFileOut != null) {
                try {
                    postFileOut.close();
                } catch (IOException e) {
                    Toast.makeText(context, "Might not save post", Toast.LENGTH_SHORT);
                }
            }
        }
        return saved;
    }

    @Override
    public List<Post> retrieveAll() {
        List<Post> posts = new ArrayList<Post>();
        File postDir = new File(context.getExternalFilesDir(null), "posts");
        if (postDir.isDirectory()){
            for (File postFile : postDir.listFiles()){
                StringWriter postWriter = new StringWriter();
                FileInputStream postFileIn = null;
                try {
                    postFileIn = new FileInputStream(postFile);
                    IOUtils.copy(postFileIn, postWriter, "UTF-8");
                } catch (IOException e) {
                    Toast.makeText(context, "Could not retrieve post", Toast.LENGTH_SHORT);
                } finally {
                    if (postFileIn != null){
                        try {
                            postFileIn.close();
                        } catch (IOException e) {
                            Toast.makeText(context, "Might not retrieve post", Toast.LENGTH_SHORT);
                        }
                    }
                }
             posts.add(new Post(postFile.getName(), postWriter.toString()));
            }
        }
        return null;
    }
}

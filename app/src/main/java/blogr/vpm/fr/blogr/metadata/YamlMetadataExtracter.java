package blogr.vpm.fr.blogr.metadata;

import android.util.Log;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.StringReader;
import java.util.Map;

import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.bean.PostMetadata;
import blogr.vpm.fr.blogr.insertion.Extracter;

/**
 * Created by vince on 12/03/15.
 */
public class YamlMetadataExtracter implements Extracter {

  public static final String YAML_SEP = "---\n";

  @Override
  public void extract(Blog blog, Post post) {
    int firstIndex = post.getContent().indexOf(YAML_SEP) + YAML_SEP.length();
    int secondIndex = post.getContent().indexOf(YAML_SEP, firstIndex);

    if ((firstIndex > 3) && (secondIndex > firstIndex)) {
      String metadata = post.getContent().substring(firstIndex, secondIndex);
      post.setContent(post.getContent().substring(secondIndex + YAML_SEP.length()));

      YamlReader reader = new YamlReader(new StringReader(metadata));
      Map md = null;
      try {
        md = (Map) reader.read();
      } catch (YamlException e) {
        Log.w("yaml", e.toString());
      }
      post.setMd(new PostMetadata(md));
    }
  }
}

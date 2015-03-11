package blogr.vpm.fr.blogr.metadata;

import android.util.Log;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlWriter;

import java.io.StringWriter;

import blogr.vpm.fr.blogr.bean.PostMetadata;
import blogr.vpm.fr.blogr.insertion.SingleTagProvider;

/**
 * Created by vince on 11/03/15.
 */
public class YamlMetadataProvider implements SingleTagProvider {

  public static final String YAML_SEP = "---\n";
  private final PostMetadata postMD;

  public YamlMetadataProvider(PostMetadata postMD) {
    this.postMD = postMD;
  }

  @Override
  public String getTag() {

    StringWriter yamlMDWriter = new StringWriter();
    yamlMDWriter.append(YAML_SEP);
    YamlWriter yamlWriter = new YamlWriter(yamlMDWriter);
    yamlWriter.getConfig().setPropertyElementType(PostMetadata.class, "tags", String.class);
    yamlWriter.getConfig().setClassTag("gen'd by BlogR app", PostMetadata.class);
    try {
      yamlWriter.write(postMD);
      yamlWriter.close();
    } catch (YamlException e) {
      Log.w("yaml", e.toString());
    }
    yamlMDWriter.append(YAML_SEP);
    return yamlMDWriter.toString();
  }
}

package blogr.vpm.fr.blogr.publish;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;
import blogr.vpm.fr.blogr.publish.PostPublisher;


/**
 * Created by vincent on 29/08/14.
 */
public class TPPostPublisher extends StdEmailPostPublisher {

  public TPPostPublisher(Context context) {
    super(context);
    setFormatter(new IdentityFormatter());
  }

  @Override
  protected void putEmailContent(Intent intent, String content) {
    intent.putExtra(Intent.EXTRA_TEXT, content);
  }
}

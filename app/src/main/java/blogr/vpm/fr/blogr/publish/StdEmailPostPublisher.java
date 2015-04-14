package blogr.vpm.fr.blogr.publish;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;

import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.EmailBlog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 16/10/14.
 */
public class StdEmailPostPublisher implements PostPublisher {

  private final Context context;

  private Formatter formatter;

  public StdEmailPostPublisher(Context context) {
    this.context = context;
    this.formatter = new HtmlFormatter();
  }

  @Override
  public void publish(Blog blog, Post post) {
    Intent intent;
    if (!(blog instanceof EmailBlog)) {
      Log.e("publisher", "Cannot publish a non-email blog as an email");
      return;
    }
    EmailBlog emailBlog = (EmailBlog) blog;
    if (!post.getPicturesAsFiles(context).isEmpty()) {
      intent = emailIntentWithAttachments(emailBlog, post);
    } else {
      intent = emailIntentWithoutAttachments(emailBlog, post);
    }
    context.startActivity(intent);
  }

  @Override
  public void setFormatter(Formatter formatter) {
    this.formatter = formatter;
  }

  protected void putEmailContent(Intent intent, String content) {
    intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content));
  }

  private Intent emailIntentWithAttachments(EmailBlog blog, Post post) {
    Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{blog.getEmailAddress()});
    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, post.getPicturesAsFiles(context));

    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
    String content = this.formatter.format(post.getContent());
    putEmailContent(intent, content);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    return intent;
  }

  private Intent emailIntentWithoutAttachments(EmailBlog blog, Post post) {
    Intent intent = new Intent(Intent.ACTION_SENDTO);

    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
    String content = this.formatter.format(post.getContent());
    putEmailContent(intent, content);

    intent.setData(Uri.parse("mailto:" + blog.getEmailAddress()));

    putEmailContent(intent, content);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    return intent;
  }
}

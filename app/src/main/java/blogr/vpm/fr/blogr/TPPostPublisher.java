package blogr.vpm.fr.blogr;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


/**
 * Created by vincent on 29/08/14.
 */
public class TPPostPublisher implements PostPublisher {

    private Context context;

    public TPPostPublisher(Context context) {
        this.context = context;
    }

    @Override
    public void publish(Blog blog, Post post) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, post.getContent());
        intent.setData(Uri.parse("mailto:" + blog.getEmailAddress()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


/*
  @Override
  public void publish(Blog blog, Post post) throws MessagingException {
   Message postMessage = generateEmail(post);
   //postMessage.setRecipient(blog.getRecipient());
   Transport.send(postMessage);
  }

  private Message generateEmail(Post post) throws MessagingException {
    Message message = new MimeMessage();
    message.setSubject(post.getTitle());
    message.setDataHandler(new DataHandler());
    return message;
  }

  */
}

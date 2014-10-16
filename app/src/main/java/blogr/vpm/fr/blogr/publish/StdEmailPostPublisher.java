package blogr.vpm.fr.blogr.publish;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.Post;

/**
 * Created by vince on 16/10/14.
 */
public class StdEmailPostPublisher implements PostPublisher {

    private final Context context;

    public StdEmailPostPublisher(Context context) {
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
}

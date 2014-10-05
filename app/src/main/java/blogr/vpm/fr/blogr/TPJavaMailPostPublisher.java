package blogr.vpm.fr.blogr;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * Created by vincent on 04/10/14.
 */
public class TPJavaMailPostPublisher implements PostPublisher {

    @Override
    public void publish(Blog blog, Post post) {
        try {
            Message postMessage = generateEmail(post);
            //postMessage.setRecipient(blog.getRecipient());
            Transport.send(postMessage);
        } catch (MessagingException e){
            // TODO display error
        }
    }

    private Message generateEmail(Post post) throws MessagingException {
        Message message = new MimeMessage(Session.getDefaultInstance(new Properties()));
        message.setSubject(post.getTitle());
        //message.setDataHandler(new DataHandler);
        return message;
    }

}

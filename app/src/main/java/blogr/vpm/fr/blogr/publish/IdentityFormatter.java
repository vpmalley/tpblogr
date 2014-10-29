package blogr.vpm.fr.blogr.publish;

/**
 * Created by vince on 24/10/14.
 * <p/>
 * The identity formatter does not apply any transformation to the content, it remains identical.
 */
public class IdentityFormatter implements Formatter {

  @Override
  public String format(String content) {
    return content;
  }
}

package blogr.vpm.fr.blogr.bean;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Created by vincent on 29/08/14.
 */
public class GithubBlog implements Blog {

  public static final String REPO_SUFFIX = ".github.io";
  public static final String GITHUB_DOMAIN = "https://github.com/";
  private String username;

  public GithubBlog(String username) {
    this.username = username;
  }

  @Override
  public String getTitle() { return username + REPO_SUFFIX;}

  public String getRepositoryUrl() {
    return GITHUB_DOMAIN + username + "/" + username + REPO_SUFFIX + ".git";
  }
}

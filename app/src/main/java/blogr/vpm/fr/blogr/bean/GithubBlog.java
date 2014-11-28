package blogr.vpm.fr.blogr.bean;

import android.content.Context;

import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;
import blogr.vpm.fr.blogr.picture.PictureMdTagsProvider;
import blogr.vpm.fr.blogr.publish.PostPublisher;

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

  @Override
  public String getPostsFolder() {
    return "_posts";
  }

  @Override
  public PostPublisher getPublisherService(Context context) {
    return null; // TODO: add a Publisher for Github
  }

  @Override
  public SurroundingTagsProvider getPictureTagsProvider(Context context, String pictureUrl) {
    return new PictureMdTagsProvider(pictureUrl);
  }

  public String getRepositoryUrl() {
    return GITHUB_DOMAIN + username + "/" + username + REPO_SUFFIX + ".git";
  }
}

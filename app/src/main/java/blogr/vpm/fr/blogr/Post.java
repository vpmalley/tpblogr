package blogr.vpm.fr.blogr;

import java.util.Date;

/**
 * Created by vincent on 29/08/14.
 */
public class Post {

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    private String title;

  private String content;

  private Date date;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getDate() {
    return date;
  }
}

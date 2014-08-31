package blogr.vpm.fr.blogr;

import java.util.Date;

/**
 * Created by vincent on 29/08/14.
 */
public class Post {

  String title;

  String content;

  Date date;

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

  public void setDate(Date date) {
    this.date = date;
  }
}

package blogr.vpm.fr.blogr.insertion;

/**
 * Created by vince on 13/05/15.
 */
public class WikipediaLinkTagsProvider implements SurroundingTagsProvider {

  private final String articleName;

  public WikipediaLinkTagsProvider(String articleName) {
    this.articleName = articleName;
  }

  @Override
  public String getStartTag() {
    return "[";
  }

  @Override
  public String getEndTag() {
    return "](https://en.wikipedia.org/wiki/" + articleName + ")";
  }
}

package blogr.vpm.fr.blogr.picture;

import blogr.vpm.fr.blogr.insertion.SurroundingTagsProvider;

/**
 * Created by vincent on 19/10/14.
 */
public class PictureMdTagsProvider implements SurroundingTagsProvider {

    private final String pictureUrl;

    public PictureMdTagsProvider(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Override
    public String getStartTag() {
        return "![";
    }

    @Override
    public String getEndTag() {
        StringBuilder endTagBuilder = new StringBuilder();
        endTagBuilder.append("](");
        endTagBuilder.append(pictureUrl);
        endTagBuilder.append(")");
        return endTagBuilder.toString();
    }
}

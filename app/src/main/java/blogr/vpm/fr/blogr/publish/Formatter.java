package blogr.vpm.fr.blogr.publish;

/**
 * Created by vincent on 19/10/14.
 *
 * Format the markdown content to any output format defined by the implementation
 */
public interface Formatter {

    /**
     * Formats markdown content to another format
     *
     * @param content the content to format
     * @return the formatted content
     */
    String format(String content);
}

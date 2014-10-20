package blogr.vpm.fr.blogr.publish;

import org.markdownj.MarkdownProcessor;

/**
 * Created by vincent on 19/10/14.
 *
 * Formats content to HTML, thanks to MarkdownJ by @myabc
 */
public class HtmlFormatter implements Formatter {
    @Override
    public String format(String content) {
        MarkdownProcessor mdProc = new MarkdownProcessor();
        return mdProc.markdown(content);
    }
}

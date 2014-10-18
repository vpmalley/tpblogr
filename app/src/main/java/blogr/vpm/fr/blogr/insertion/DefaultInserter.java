package blogr.vpm.fr.blogr.insertion;

import android.content.Context;
import android.widget.EditText;

import blogr.vpm.fr.blogr.R;

/**
 * Created by vincent on 08/10/14.
 */
public class DefaultInserter implements Inserter {

    Context context;

    public DefaultInserter(Context context) {
        this.context = context;
    }

    @Override
    public String insert(EditText contentField, SingleTagProvider tagProvider) {
        int position = contentField.getSelectionEnd();
        String content = contentField.getText().toString();
        String contentBeforeTag = content.substring(0, position);
        String contentAfterTag = content.substring(position);

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(contentBeforeTag);
        contentBuilder.append(tagProvider.getTag());
        contentBuilder.append(contentAfterTag);

        contentField.setText(contentBuilder.toString());
        contentField.setSelection(position + tagProvider.getTag().length());
        return contentBuilder.toString();
    }

    @Override
    public String insert(EditText contentField, SurroundingTagsProvider tagProvider) {
        int startPosition = contentField.getSelectionStart();
        int endPosition = contentField.getSelectionEnd();
        String content = contentField.getText().toString();
        String contentBeforeTags = content.substring(0, startPosition);
        String contentBetweenTags = content.substring(startPosition, endPosition);
        String contentAfterTags = content.substring(endPosition);

        if (startPosition == endPosition){
            contentBetweenTags = context.getResources().getString(R.string.customtext);
        }

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(contentBeforeTags);
        contentBuilder.append(tagProvider.getStartTag());
        contentBuilder.append(contentBetweenTags);
        contentBuilder.append(tagProvider.getEndTag());
        contentBuilder.append(contentAfterTags);

        contentField.setText(contentBuilder.toString());
        contentField.setSelection(startPosition, startPosition + tagProvider.getStartTag().length() +
                contentBetweenTags.length() + tagProvider.getEndTag().length());
        return contentBuilder.toString();
    }
}

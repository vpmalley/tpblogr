package blogr.vpm.fr.blogr.activity;

import blogr.vpm.fr.blogr.bean.Blog;

/**
 * Created by vince on 01/12/14.
 */
public interface DialogDismissedListener {

  public void onDialogDismissed(Blog blog, int requestCode);

}

package blogr.vpm.fr.blogr.activity;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.format.AlignCenterTagsProvider;
import blogr.vpm.fr.blogr.format.AlignLeftTagsProvider;
import blogr.vpm.fr.blogr.format.AlignRightTagsProvider;
import blogr.vpm.fr.blogr.insertion.Inserter;

/**
 * Created by vince on 11/05/15.
 */
public class AlignCallback implements ActionMode.Callback {

  private final Inserter inserter;

  private final EditText editText;

  public AlignCallback(Inserter inserter, EditText editText) {
    this.inserter = inserter;
    this.editText = editText;
  }

  @Override
  public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
    MenuInflater inflater = actionMode.getMenuInflater();
    inflater.inflate(R.menu.posteditionalign, menu);
    return true;
  }

  @Override
  public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
    return false;
  }

  @Override
  public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case R.id.action_align_left:
        inserter.insert(editText, new AlignLeftTagsProvider());
        return true;
      case R.id.action_align_center:
        inserter.insert(editText, new AlignCenterTagsProvider());
        return true;
      case R.id.action_align_right:
        inserter.insert(editText, new AlignRightTagsProvider());
        return true;
    }
    return false;
  }

  @Override
  public void onDestroyActionMode(ActionMode actionMode) {

  }
}

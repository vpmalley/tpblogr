package blogr.vpm.fr.blogr.activity;

/**
 * Created by vince on 17/10/14.
 * <p/>
 * When the model on which views are based is invalidated, this listener should be triggered
 */
public interface InvalidatedModelListener {

  /**
   * When the data model for a view is invalidated, this should be triggered.
   */
  void onInvalidatedModel();

}

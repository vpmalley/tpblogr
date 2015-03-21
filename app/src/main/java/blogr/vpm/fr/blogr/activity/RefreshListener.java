package blogr.vpm.fr.blogr.activity;

/**
 * Created by vince on 21/03/15.
 */
public interface RefreshListener {

  /**
   * For an activity/fragment that displays views based on a Post, refreshes the views to display that Post
   */
  void refreshViewFromPost();
}

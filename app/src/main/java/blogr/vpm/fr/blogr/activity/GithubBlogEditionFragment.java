package blogr.vpm.fr.blogr.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.GithubBlog;
import blogr.vpm.fr.blogr.persistence.FileBlogManager;

/**
 * Created by vincent on 07/10/14.
 */
public class GithubBlogEditionFragment extends Fragment {

  private GithubBlog currentBlog;

  private EditText usernameField;

  private EditText mdKeysField;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    Bundle args = getArguments();
    if (args.containsKey(BlogActivity.BLOG_KEY)) {
      currentBlog = args.getParcelable(BlogActivity.BLOG_KEY);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_blog, container, false);
    usernameField = (EditText) v.findViewById(R.id.main);
    usernameField.setHint(getActivity().getString(R.string.hint_github_username));
    usernameField.setText(currentBlog.getTitle().replace(GithubBlog.REPO_SUFFIX, ""));

    mdKeysField = (EditText) v.findViewById(R.id.md_keys);
    mdKeysField.setVisibility(View.VISIBLE);
    String keysAsString = currentBlog.getKeysAsString();
    if (!keysAsString.isEmpty()) {
      mdKeysField.setText(keysAsString);
    }
    return v;
  }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.blogedition, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_save:
        // save blog information
        String username = usernameField.getText().toString();
        String keys = mdKeysField.getText().toString();
        GithubBlog newBlog = new GithubBlog(username);
        newBlog.addMdKeys(keys);
        try {
          if (!new FileBlogManager().exists(currentBlog)) {
            newBlog.cloneRepository(getActivity());
          }
        } catch (IOException e) {
          Log.w("file", e.toString());
        }
        try {
          new FileBlogManager().update(currentBlog, newBlog);
        } catch (IOException e) {
          Toast.makeText(getActivity(), getActivity().getString(R.string.cannotsaveblog), Toast.LENGTH_SHORT).show();
        }
        getActivity().finish();
        return true;
      case R.id.action_settings:
        startActivity(new Intent(getActivity(), AllPreferencesActivity.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

}

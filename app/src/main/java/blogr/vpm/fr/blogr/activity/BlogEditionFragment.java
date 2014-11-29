package blogr.vpm.fr.blogr.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.bean.EmailBlog;
import blogr.vpm.fr.blogr.bean.GithubBlog;

/**
 * Created by vincent on 07/10/14.
 */
public class BlogEditionFragment extends Fragment {

  public static final String BLOG_KEY = "blog";

  private Blog currentBlog;

  private EditText emailField;

  private EditText mainField;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    Bundle args = getArguments();
    if (args.containsKey(BLOG_KEY)) {
      currentBlog = args.getParcelable(BLOG_KEY);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_blog, container, false);
    mainField = (EditText) v.findViewById(R.id.main);
    if (currentBlog instanceof EmailBlog) {
      emailField = (EditText) v.findViewById(R.id.email);
      mainField.setHint(getActivity().getString(R.string.hint_blog_name));
    } else if (currentBlog instanceof GithubBlog) {
      mainField.setHint(getActivity().getString(R.string.hint_github_username));
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
        String title = mainField.getText().toString();
        if (currentBlog instanceof GithubBlog) {
          title += GithubBlog.REPO_SUFFIX;
        } else if (currentBlog instanceof EmailBlog) {
          String email = emailField.getText().toString();
          ((EmailBlog) currentBlog).setRecipientEmail(email);
        }
        currentBlog.setTitle(title);
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

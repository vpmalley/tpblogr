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
import android.widget.Toast;

import java.io.IOException;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.EmailBlog;
import blogr.vpm.fr.blogr.persistence.FileBlogManager;

/**
 * Created by vincent on 07/10/14.
 */
public class EmailBlogEditionFragment extends Fragment {

  private EmailBlog currentBlog;

  private EditText emailField;

  private EditText titleField;

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
    titleField = (EditText) v.findViewById(R.id.main);
    titleField.setHint(getActivity().getString(R.string.hint_blog_name));
    emailField = (EditText) v.findViewById(R.id.email);
    emailField.setVisibility(View.VISIBLE);
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
        String title = titleField.getText().toString();
        String email = emailField.getText().toString();
        currentBlog.setRecipientEmail(email);
        currentBlog.setTitle(title);
        try {
          new FileBlogManager().persist(currentBlog);
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

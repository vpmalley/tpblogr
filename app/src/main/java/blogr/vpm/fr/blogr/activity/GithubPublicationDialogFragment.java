package blogr.vpm.fr.blogr.activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.publish.GithubPublisher;

/**
 * Created by vincent on 20/10/14.
 */
public class GithubPublicationDialogFragment extends DialogFragment {

  private static final String ARG_USER = "username";

  private GithubPublisher publisher;

  public static GithubPublicationDialogFragment newInstance(GithubPublisher publisher, String username) {
    Bundle bundle = new Bundle();
    bundle.putString(ARG_USER, username);
    GithubPublicationDialogFragment fragment = new GithubPublicationDialogFragment();
    fragment.setArguments(bundle);
    fragment.setPublisher(publisher);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.github_dialog, container, false);
    final TextView description = (TextView) v.findViewById(R.id.description);
    final EditText usernameField = (EditText) v.findViewById(R.id.username);
    final EditText passwordField = (EditText) v.findViewById(R.id.password);

    description.setText(getString(R.string.git_credentials));

    final String username = getArguments().getString(ARG_USER);
    usernameField.setText(username);

    Button button = (Button)v.findViewById(R.id.publish);
    button.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        publisher.publish(usernameField.getText().toString(), passwordField.getText().toString());
        dismiss();
      }
    });

    Button cancelB = (Button)v.findViewById(R.id.cancel);
    cancelB.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        dismiss();
      }
    });

    return v;
  }

  public void setPublisher(GithubPublisher publisher) {
    this.publisher = publisher;
  }
}

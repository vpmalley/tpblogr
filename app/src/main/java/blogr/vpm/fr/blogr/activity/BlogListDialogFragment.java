package blogr.vpm.fr.blogr.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.bean.Blog;
import blogr.vpm.fr.blogr.persistence.FileBlogManager;

/**
 * Created by vincent on 20/10/14.
 */
public class BlogListDialogFragment extends DialogFragment {

  public static final String ARG_PICS = "flickrPics";

  public BlogListDialogFragment(){
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    List<Blog> allBlogs = new ArrayList<Blog>();
    try {
      allBlogs = new FileBlogManager().retrieveAll();
    } catch (IOException e) {
      Toast.makeText(getActivity(), getActivity().getString(R.string.cannotgetblog), Toast.LENGTH_SHORT).show();
      dismiss();
    }

    ArrayAdapter<Blog> blogsAdapter = new ArrayAdapter<Blog>(getActivity(), R.layout.post_item, allBlogs);

    final List<Blog> finalAllBlogs = allBlogs;
    return new AlertDialog.Builder(getActivity())
        .setTitle(R.string.pick_blog)
        .setAdapter(blogsAdapter, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int position) {
            Intent i = new Intent(getActivity(), BlogActivity.class);
            i.putExtra(BlogActivity.BLOG_KEY, finalAllBlogs.get(position));
            startActivity(i);
          }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            // do nothing - just close the dialog
          }
        })
        .create();
  }
}

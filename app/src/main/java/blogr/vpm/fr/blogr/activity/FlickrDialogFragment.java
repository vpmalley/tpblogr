package blogr.vpm.fr.blogr.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;
import blogr.vpm.fr.blogr.picture.Picture;
import blogr.vpm.fr.blogr.picture.PicturePickedListener;

/**
 * Created by vincent on 20/10/14.
 */
public class FlickrDialogFragment extends DialogFragment {

  public static final String ARG_PICS = "flickrPics";

  private PicturePickedListener listener;

  public FlickrDialogFragment(){
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Picture[] pPics = (Picture[]) getArguments().getParcelableArray(ARG_PICS);
    /*
    for (Picture picture : pPics) {
      new AsyncPictureLoader(picture).execute(picture.getUrl());
    }
    */

    ArrayAdapter<Picture> picsAdapter = new ArrayAdapter<Picture>(getActivity(), R.layout.flickr_pic_item, pPics) {
      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder itemHolder;
        if (convertView == null) {
          LayoutInflater vi = getActivity().getLayoutInflater();
          convertView = vi.inflate(R.layout.flickr_pic_item, parent, false);

          ImageView picImage = (ImageView) convertView.findViewById(R.id.picImage);
          TextView picTitle = (TextView) convertView.findViewById(R.id.picTitle);

          itemHolder = new ViewHolder(picTitle, picImage);
          convertView.setTag(itemHolder);
        } else {
          itemHolder = (ViewHolder) convertView.getTag();
        }
        itemHolder.titleView.setText(pPics[position].getTitle());
        /*
        if (pPics[position].getSmallBitmap() != null) {
          itemHolder.pictureView.setImageBitmap(pPics[position].getSmallBitmap());
        } else {
          itemHolder.pictureView.setImageResource(R.drawable.ic_action_picture);
        }
        */
        pPics[position].displayPicture(itemHolder.pictureView);
        return convertView;
      }
    };

    return new AlertDialog.Builder(getActivity())
        .setTitle(R.string.flickr_pick_picture)
        .setAdapter(picsAdapter, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int position) {
            //listener.onPicturePicked(pPics[position]);
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

  public void openPicturePicker(Activity activity, ParcelableFlickrPhoto[] pPics, PicturePickedListener listener) {
    this.listener = listener;
    Bundle args = new Bundle();
    args.putParcelableArray(FlickrDialogFragment.ARG_PICS, pPics);
    setArguments(args);
    show(activity.getFragmentManager(), "flickrPicker");
  }

  /**
   * Keeps a reference to the views associated with a item. Only views should be stored there,
   * i.e. NO DATA should be linked to that object.
   */
  public class ViewHolder {

    private final TextView titleView;

    private final ImageView pictureView;

    public ViewHolder(TextView titleView, ImageView pictureView) {
      this.titleView = titleView;
      this.pictureView = pictureView;
    }
  }
}

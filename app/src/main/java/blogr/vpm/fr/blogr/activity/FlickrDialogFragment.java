package blogr.vpm.fr.blogr.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;
import blogr.vpm.fr.blogr.picture.PicturePickedListener;

/**
 * Created by vincent on 20/10/14.
 */
public class FlickrDialogFragment extends DialogFragment {

    public static final String ARG_PICS = "flickrPics";

    private final PicturePickedListener picturePickedListener;

    public FlickrDialogFragment(PicturePickedListener picturePickedListener) {
        this.picturePickedListener = picturePickedListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ParcelableFlickrPhoto[] pPics = (ParcelableFlickrPhoto[]) getArguments().getParcelableArray(ARG_PICS);

        ArrayAdapter<ParcelableFlickrPhoto> picsAdapter = new ArrayAdapter<ParcelableFlickrPhoto>(getActivity(), R.layout.post_item, pPics);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.flickr_pick_picture)
                .setAdapter(picsAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        picturePickedListener.onPicturePicked(pPics[position].getPicUrl());
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

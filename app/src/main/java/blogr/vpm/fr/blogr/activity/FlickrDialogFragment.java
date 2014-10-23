package blogr.vpm.fr.blogr.activity;

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
import blogr.vpm.fr.blogr.apis.flickr.AsyncPictureLoader;
import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;
import blogr.vpm.fr.blogr.picture.PicturePickedListener;

/**
 * Created by vincent on 20/10/14.
 */
public class FlickrDialogFragment extends DialogFragment {

    public static final String ARG_PICS = "flickrPics";

    private final PicturePickedListener picturePickedListener;

    // TODO try to keep a no-argument constructor
    public FlickrDialogFragment(PicturePickedListener picturePickedListener) {
        this.picturePickedListener = picturePickedListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ParcelableFlickrPhoto[] pPics = (ParcelableFlickrPhoto[]) getArguments().getParcelableArray(ARG_PICS);

        ArrayAdapter<ParcelableFlickrPhoto> picsAdapter = new ArrayAdapter<ParcelableFlickrPhoto>(getActivity(), R.layout.flickr_pic_item, pPics){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null){
                    LayoutInflater vi = getActivity().getLayoutInflater();
                    convertView = vi.inflate(R.layout.flickr_pic_item, parent, false);
                }
                ImageView picImage = (ImageView) convertView.findViewById(R.id.picImage);
                TextView picTitle = (TextView) convertView.findViewById(R.id.picTitle);
                picImage.setImageResource(R.drawable.ic_action_picture);
                picTitle.setText(pPics[position].getTitle());
                String[] picUrlAsArray = {pPics[position].getThumbnailSizeUrl()};
                new AsyncPictureLoader(picImage).execute(picUrlAsArray);
                return convertView;
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.flickr_pick_picture)
                .setAdapter(picsAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        picturePickedListener.onPicturePicked(pPics[position].getThumbnailSizeUrl());
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

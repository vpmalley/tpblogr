package blogr.vpm.fr.blogr.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import blogr.vpm.fr.blogr.R;
import blogr.vpm.fr.blogr.picture.AsyncPictureLoader;
import blogr.vpm.fr.blogr.apis.flickr.ParcelableFlickrPhoto;
import blogr.vpm.fr.blogr.picture.ImageViewLoader;
import blogr.vpm.fr.blogr.picture.PictureLoadedListener;
import blogr.vpm.fr.blogr.picture.PicturePickedListener;

/**
 * Created by vincent on 20/10/14.
 */
public class FlickrDialogFragment extends DialogFragment {

    public static final String ARG_PICS = "flickrPics";

    private final PicturePickedListener picturePickedListener;

    private final List<Bitmap> bitmaps;

    // TODO try to keep a no-argument constructor
    public FlickrDialogFragment(PicturePickedListener picturePickedListener) {
        this.picturePickedListener = picturePickedListener;
        bitmaps = new ArrayList<Bitmap>();
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

                boolean cached = (bitmaps.size() > position) && (bitmaps.get(position) != null);
                if (cached){
                    picImage.setImageBitmap(bitmaps.get(position));
                } else {
                    asyncLoadPicture(position, picImage, pPics);
                }
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

    private void asyncLoadPicture(int position, ImageView picImage, ParcelableFlickrPhoto[] pPics) {
        String[] picUrlAsArray = {pPics[position].getSmallSizeUrl()};
        PictureLoadedListener pictureLoadedListener = new ImageViewLoader(bitmaps, position, picImage);
        Log.d("images", position + "-" + picUrlAsArray[0]);
        new AsyncPictureLoader(pictureLoadedListener).execute(picUrlAsArray);
    }
}

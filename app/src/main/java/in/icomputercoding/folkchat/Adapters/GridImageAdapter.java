package in.icomputercoding.folkchat.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import in.icomputercoding.folkchat.R;
import in.icomputercoding.folkchat.Utils.SquareImageView;


public class GridImageAdapter extends ArrayAdapter<String>{

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final int layoutResource;
    private final String mAppend;

    public GridImageAdapter(Context context, int layoutResource, String append, ArrayList<String> imgURLs) {
        super(context, layoutResource, imgURLs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        this.layoutResource = layoutResource;
        mAppend = append;
    }

    private static class ViewHolder{
        SquareImageView image;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ProgressDialog dialog = ProgressDialog.show(getContext(), "",
                "Loading...", true);

        final ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.gridImageView);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        String imgURL = getItem(position);

        ImageLoader imageLoader = ImageLoader.getInstance();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));

        imageLoader.displayImage(mAppend + imgURL, holder.image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                dialog.dismiss();

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                dialog.dismiss();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    dialog.dismiss();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                    dialog.dismiss();
            }
        });

        return convertView;
    }
}



















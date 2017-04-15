package com.appmatic.baseapp.gallery.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.api.models.GalleryGroup;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by grender on 14/04/17.
 */

public class ImagePreviewAdapter extends PagerAdapter {
    private final Context context;
    private ArrayList<GalleryGroup.Image> images;

    public ImagePreviewAdapter(@NonNull Context context, ArrayList<GalleryGroup.Image> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images != null ? images.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object instanceof ViewHolder
                && view.equals(((ViewHolder) object).photoView);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = LayoutInflater.from(context)
                .inflate(R.layout.page_image_item_preview, container, false);
        ViewHolder viewHolder = new ViewHolder(view);
        Glide.with(context)
                .load(images.get(position).getUrl())
                .dontAnimate()
                .into(viewHolder.photoView);
        container.addView(viewHolder.photoView);
        return viewHolder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Glide.clear(((ViewHolder) object).photoView);
        container.removeView(((ViewHolder) object).photoView);
    }

    static class ViewHolder {
        @BindView(R.id.item_image_preview) PhotoView photoView;

        ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}

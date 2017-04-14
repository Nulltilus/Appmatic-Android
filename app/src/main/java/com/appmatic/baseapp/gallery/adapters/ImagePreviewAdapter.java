package com.appmatic.baseapp.gallery.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
                && view.equals(((ViewHolder) object).itemView);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = LayoutInflater.from(context)
                .inflate(R.layout.page_image_item_preview, container, false);
        ViewHolder viewHolder = new ViewHolder(view);
        Glide.with(context)
                .load(images.get(position).getUrl())
                .into(viewHolder.photoView);
        container.addView(viewHolder.itemView);
        return viewHolder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((ViewHolder) object).itemView);
    }

    static class ViewHolder {
        final View itemView;
        @BindView(R.id.item_image_preview) PhotoView photoView;

        ViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}

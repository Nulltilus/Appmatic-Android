package com.appmatic.baseapp.gallery.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.api.models.GalleryGroup;
import com.appmatic.baseapp.gallery.callbacks.GallerySharedElementCallback;
import com.appmatic.baseapp.gallery.preview.ImagePreviewActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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
    private int initialPosition;
    private int currentPosition = RecyclerView.NO_POSITION;
    private GallerySharedElementCallback sharedElementCallback;

    public ImagePreviewAdapter(@NonNull Context context, ArrayList<GalleryGroup.Image> images,
                               GallerySharedElementCallback sharedElementCallback) {
        this.context = context;
        this.images = images;
        this.sharedElementCallback = sharedElementCallback;
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
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currentPosition = position;
        sharedElementCallback.setSharedElementViews(((ViewHolder) object).photoView);
    }

    public void setInitialPosition(int position) {
        this.initialPosition = position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View view = LayoutInflater.from(context)
                .inflate(R.layout.page_image_item_preview, container, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        ViewCompat.setTransitionName(viewHolder.photoView, images.get(position).getUrl());
        Glide.with(context)
                .load(images.get(position).getUrl())
                .dontTransform()
                .dontAnimate()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        viewHolder.photoView.setImageDrawable(resource);
                        startPostponedEnterTransition(position);
                    }
                });

        container.addView(viewHolder.photoView);
        return viewHolder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((ViewHolder) object).photoView);
    }

    private void startPostponedEnterTransition(int position) {
        if (position == initialPosition) {
            ((ImagePreviewActivity) context).supportStartPostponedEnterTransition();
        }
    }

    static class ViewHolder {
        @BindView(R.id.item_image)
        PhotoView photoView;

        ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
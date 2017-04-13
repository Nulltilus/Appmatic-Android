package com.appmatic.baseapp.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by grender on 13/04/17.
 */

class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_GROUP = 0;
    private static final int VIEW_TYPE_IMAGE = 1;

    interface GalleryCallbacks {
        void onImageClick(String url);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public GroupViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}

package com.appmatic.baseapp.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.api.models.GalleryGroup;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by grender on 13/04/17.
 */

class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_GROUP = 0;
    private static final int VIEW_TYPE_IMAGE = 1;

    private GalleryCallbacks galleryCallbacks;
    private ArrayList<GalleryGroup> groups;
    private int selectedGroupPosition = -1;

    GalleryAdapter(ArrayList<GalleryGroup> groups, GalleryCallbacks galleryCallbacks) {
        this.groups = groups;
        this.galleryCallbacks = galleryCallbacks;
    }

    interface GalleryCallbacks {
        void onImageClick(String url);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_GROUP)
            return new GroupViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gallery_group_item, parent, false));
        else if (viewType == VIEW_TYPE_IMAGE)
            return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gallery_image_item, parent, false));
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GroupViewHolder) {
            final GalleryGroup galleryGroup = groups.get(position);
            Glide.with(((GroupViewHolder) holder).groupImage.getContext())
                    .load(galleryGroup.getImages().get(0).getUrl())
                    .into(((GroupViewHolder) holder).groupImage);
            ((GroupViewHolder) holder).groupTitle.setText(galleryGroup.getTitle());
        } else if (holder instanceof ImageViewHolder) {
            final GalleryGroup.Image image = groups.get(selectedGroupPosition).getImages().get(position);
            Glide.with(((ImageViewHolder) holder).itemImage.getContext())
                    .load(image.getUrl())
                    .into(((ImageViewHolder) holder).itemImage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return selectedGroupPosition == -1 ? VIEW_TYPE_GROUP : VIEW_TYPE_IMAGE;
    }

    @Override
    public int getItemCount() {
        return groups != null ? groups.size() : 0;
    }

    public void backToGroups() {
        this.selectedGroupPosition = -1;
        notifyDataSetChanged();
    }

    public void removeAll() {
        groups.clear();
        notifyDataSetChanged();
    }

    private class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.group_image) ImageView groupImage;
        @BindView(R.id.group_title) TextView groupTitle;

        public GroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            groupImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            selectedGroupPosition = position;
            notifyDataSetChanged();
        }
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_image) ImageView itemImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            galleryCallbacks.onImageClick(groups.get(selectedGroupPosition).getImages().get(position).getUrl());
        }
    }
}

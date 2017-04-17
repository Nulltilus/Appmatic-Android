package com.appmatic.baseapp.gallery.adapters;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.api.models.GalleryGroup;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Appmatic
 * Copyright (C) 2016 - Nulltilus
 * <p>
 * This file is part of Appmatic.
 * <p>
 * Appmatic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * <p>
 * Appmatic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Appmatic.  If not, see <http://www.gnu.org/licenses/>.
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_GROUP = 0;
    private static final int VIEW_TYPE_IMAGE = 1;

    private GalleryCallbacks galleryCallbacks;
    private ArrayList<GalleryGroup> groups;
    private int selectedGroupPosition;

    public GalleryAdapter(ArrayList<GalleryGroup> groups, GalleryCallbacks galleryCallbacks, int selectedGroupPosition) {
        this.groups = groups;
        this.galleryCallbacks = galleryCallbacks;
        this.selectedGroupPosition = selectedGroupPosition;
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
            ViewCompat.setTransitionName(((ImageViewHolder) holder).itemImage, image.getUrl());
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
        if (selectedGroupPosition == -1)
            return groups != null ? groups.size() : 0;
        else
            return groups != null ? groups.get(selectedGroupPosition).getImages().size() : 0;
    }

    public void backToGroups() {
        selectedGroupPosition = -1;
        notifyDataSetChanged();
    }

    public void removeAll() {
        groups.clear();
        notifyDataSetChanged();
    }

    public interface GalleryCallbacks {
        void onImageClick(ArrayList<GalleryGroup.Image> images, int position);

        void onGroupClick(String title, int selectedGroup);
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.group_image_container)
        FrameLayout groupImageContainer;
        @BindView(R.id.group_image)
        ImageView groupImage;
        @BindView(R.id.group_title)
        TextView groupTitle;

        GroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            groupImageContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectedGroupPosition = getAdapterPosition();
            galleryCallbacks.onGroupClick(groups.get(selectedGroupPosition).getTitle(), selectedGroupPosition);
            notifyDataSetChanged();
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public
        @BindView(R.id.item_image)
        ImageView itemImage;
        @BindView(R.id.item_image_container)
        FrameLayout itemImageContainer;

        ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemImageContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            galleryCallbacks.onImageClick(groups.get(selectedGroupPosition).getImages(), position);
        }
    }
}

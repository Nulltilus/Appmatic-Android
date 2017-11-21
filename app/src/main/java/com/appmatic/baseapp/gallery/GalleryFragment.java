package com.appmatic.baseapp.gallery;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appmatic.baseapp.R;
import com.appmatic.baseapp.api.models.GalleryGroup;
import com.appmatic.baseapp.fragments.BaseFragment;
import com.appmatic.baseapp.gallery.adapters.GalleryAdapter;
import com.appmatic.baseapp.gallery.callbacks.GallerySharedElementCallback;
import com.appmatic.baseapp.gallery.preview.ImagePreviewActivity;
import com.appmatic.baseapp.main.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Appmatic
 * Copyright (C) 2016 - Nulltilus
 *
 * This file is part of Appmatic.
 *
 * Appmatic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Appmatic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Appmatic.  If not, see <http://www.gnu.org/licenses/>.
 */

public class GalleryFragment extends BaseFragment implements GalleryView, GalleryAdapter.GalleryCallbacks {
    private static final String SELECTED_GROUP_EXTRA = "SELECTED_GROUP_EXTRA";
    private static final String LAST_TITLE_EXTRA = "LAST_TITLE_EXTRA";
    @BindView(R.id.images_recycler_view) RecyclerView imagesRecyclerView;
    private GalleryPresenter galleryPresenter;
    private int selectedGroup;
    private GallerySharedElementCallback sharedElementCallback;
    private boolean imageAlreadyClicked = false;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        galleryPresenter = new GalleryPresenterImpl(this);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SELECTED_GROUP_EXTRA)) {
                selectedGroup = savedInstanceState.getInt(SELECTED_GROUP_EXTRA);
                getActivity().setTitle(savedInstanceState.getString(LAST_TITLE_EXTRA));
            }
        } else {
            selectedGroup = -1;
            getActivity().setTitle(getString(R.string.gallery));
        }
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_gallery);
    }

    @Override
    protected void setupViews() {
        ((MainActivity) getActivity()).showProgress(null, getString(R.string.loading_gallery_msg));
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getBoolean(R.bool.tablet_mode) ? 3 : 2));
        imagesRecyclerView.setHasFixedSize(true);
        imReady();
        galleryPresenter.getImages();
    }

    @Override
    protected void setListeners() {
        // ignored
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_GROUP_EXTRA, selectedGroup);
        outState.putString(LAST_TITLE_EXTRA, getActivity().getTitle().toString());
    }

    @Override
    public void onImageClick(ArrayList<GalleryGroup.Image> images, int position) {
        Intent previewActivityIntent = new Intent(getActivity(), ImagePreviewActivity.class);
        previewActivityIntent.putParcelableArrayListExtra(ImagePreviewActivity.PREVIEW_IMAGES_EXTRA, images);
        previewActivityIntent.putExtra(ImagePreviewActivity.INITIAL_POSITION_EXTRA, position);
        if (!imageAlreadyClicked) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                GalleryAdapter.ImageViewHolder imageViewHolder =
                        (GalleryAdapter.ImageViewHolder) imagesRecyclerView.findViewHolderForAdapterPosition(position);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(getActivity(), imageViewHolder.itemImage, images.get(position).getUrl());
                ActivityCompat.startActivity(getActivity(), previewActivityIntent, activityOptionsCompat.toBundle());
            } else {
                startActivity(previewActivityIntent);
            }
            imageAlreadyClicked = true;
        }
    }

    public void onActivityReenter(int resultCode, Intent data) {
        imageAlreadyClicked = false;
        if (imagesRecyclerView == null && getActivity() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getActivity().getWindow().getSharedElementExitTransition().addListener(null);
            getActivity().setExitSharedElementCallback((SharedElementCallback) null);
            return;
        }

        int position = NO_POSITION;
        if (resultCode == Activity.RESULT_OK && data != null && data.hasExtra(ImagePreviewActivity.LAST_POSITION_EXTRA))
            position = data.getIntExtra(ImagePreviewActivity.LAST_POSITION_EXTRA, NO_POSITION);

        if (position != NO_POSITION)
            imagesRecyclerView.scrollToPosition(position);

        sharedElementCallback = new GallerySharedElementCallback();
        getActivity().setExitSharedElementCallback(sharedElementCallback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Listener to reset shared element exit transition callbacks.
            getActivity().getWindow().getSharedElementExitTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                    // ignored
                }

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onTransitionEnd(Transition transition) {
                    if (getActivity() != null) {
                        getActivity().getWindow().getSharedElementExitTransition().removeListener(this);
                        getActivity().setExitSharedElementCallback((SharedElementCallback) null);
                    }
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // ignored
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // ignored
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // ignored
                }
            });
        }

        //noinspection ConstantConditions
        getActivity().supportPostponeEnterTransition();
        final int finalPosition = position;
        imagesRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imagesRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);

                RecyclerView.ViewHolder imageViewHolder = imagesRecyclerView.findViewHolderForAdapterPosition(finalPosition);
                if (imageViewHolder instanceof GalleryAdapter.ImageViewHolder)
                    sharedElementCallback.setSharedElementViews(((GalleryAdapter.ImageViewHolder) imageViewHolder).itemImage);

                getActivity().supportStartPostponedEnterTransition();

                return true;
            }
        });
    }

    @Override
    public void onGroupClick(String title, int selectedGroup) {
        getActivity().setTitle(title);
        this.selectedGroup = selectedGroup;
    }

    @Override
    public void setupGroups(ArrayList<GalleryGroup> groups) {
        ((MainActivity) getActivity()).hideProgress();
        imagesRecyclerView.setAdapter(new GalleryAdapter(groups, this, selectedGroup));
    }

    @Override
    public void handleInternetError() {
        ((MainActivity) getActivity()).handleInternetError(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                ((MainActivity) getActivity()).showProgress(null, getString(R.string.loading_gallery_msg));
                galleryPresenter.getImages();
            }
        });
    }

    public int getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(int selectedGroup) {
        this.selectedGroup = selectedGroup;
        if (selectedGroup == -1) {
            ((GalleryAdapter) imagesRecyclerView.getAdapter()).backToGroups();
            getActivity().setTitle(R.string.gallery);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        galleryPresenter.onDestroy();
    }

}

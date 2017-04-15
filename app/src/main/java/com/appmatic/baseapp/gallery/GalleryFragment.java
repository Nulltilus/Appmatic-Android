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

public class GalleryFragment extends BaseFragment implements GalleryView, GalleryAdapter.GalleryCallbacks {
    private static final String SELECTED_GROUP_EXTRA = "SELECTED_GROUP_EXTRA";
    @BindView(R.id.images_recycler_view)
    RecyclerView imagesRecyclerView;
    private GalleryPresenter galleryPresenter;
    private int selectedGroup;
    private GallerySharedElementCallback sharedElementCallback;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        galleryPresenter = new GalleryPresenterImpl(this);
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_GROUP_EXTRA))
            selectedGroup = savedInstanceState.getInt(SELECTED_GROUP_EXTRA);
        else
            selectedGroup = -1;
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_gallery);
    }

    @Override
    protected void setupViews() {
        ((MainActivity) getActivity()).showProgress(getString(R.string.loading), getString(R.string.loading_gallery_msg));
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        imagesRecyclerView.setHasFixedSize(true);
        galleryPresenter.getImages();
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_GROUP_EXTRA, selectedGroup);
    }

    @Override
    public void onImageClick(ArrayList<GalleryGroup.Image> images, int position) {
        Intent previewActivityIntent = new Intent(getActivity(), ImagePreviewActivity.class);
        previewActivityIntent.putParcelableArrayListExtra(ImagePreviewActivity.PREVIEW_IMAGES_EXTRA, images);
        previewActivityIntent.putExtra(ImagePreviewActivity.INITIAL_POSITION_EXTRA, position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            GalleryAdapter.ImageViewHolder imageViewHolder =
                    (GalleryAdapter.ImageViewHolder) imagesRecyclerView.findViewHolderForAdapterPosition(position);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(getActivity(), imageViewHolder.itemImage, images.get(position).getUrl());
            ActivityCompat.startActivity(getActivity(), previewActivityIntent, activityOptionsCompat.toBundle());
        } else {
            startActivity(previewActivityIntent);
        }
    }

    public void onActivityReenter(int resultCode, Intent data) {
        int position = NO_POSITION;
        if (resultCode == Activity.RESULT_OK && data != null && data.hasExtra(ImagePreviewActivity.LAST_POSITION_EXTRA))
            position = data.getIntExtra(ImagePreviewActivity.LAST_POSITION_EXTRA, NO_POSITION);

        if (position != RecyclerView.NO_POSITION)
            imagesRecyclerView.scrollToPosition(position);

        sharedElementCallback = new GallerySharedElementCallback();
        getActivity().setExitSharedElementCallback(sharedElementCallback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Listener to reset shared element exit transition callbacks.
            getActivity().getWindow().getSharedElementExitTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

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

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

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
    public void setImageError() {
        ((MainActivity) getActivity()).hideProgress();
        new MaterialDialog.Builder(getActivity())
                .title(getString(R.string.connection_error))
                .content(getString(R.string.connection_error_msg))
                .cancelable(false)
                .positiveText(getString(R.string.retry))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        galleryPresenter.getImages();
                    }
                })
                .negativeText(getString(R.string.exit))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        System.exit(0);
                    }
                })
                .show();
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

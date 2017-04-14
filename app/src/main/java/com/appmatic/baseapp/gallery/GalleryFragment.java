package com.appmatic.baseapp.gallery;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appmatic.baseapp.R;
import com.appmatic.baseapp.api.models.GalleryGroup;
import com.appmatic.baseapp.fragments.BaseFragment;
import com.appmatic.baseapp.gallery.adapters.GalleryAdapter;
import com.appmatic.baseapp.gallery.preview.ImagePreviewActivity;
import com.appmatic.baseapp.main.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class GalleryFragment extends BaseFragment implements GalleryView, GalleryAdapter.GalleryCallbacks {
    @BindView(R.id.images_recycler_view) RecyclerView imagesRecyclerView;
    private GalleryPresenter galleryPresenter;
    private boolean isInGroupView;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        galleryPresenter = new GalleryPresenterImpl(this);
        isInGroupView = true;
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_gallery);
    }

    @Override
    protected void setupViews() {
        ((MainActivity) getActivity()).showProgress(getString(R.string.loading), getString(R.string.loading_gallery_msg));
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        galleryPresenter.getImages();
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void onImageClick(ArrayList<GalleryGroup.Image> images, int position) {
        Intent previewActivityIntent = new Intent(getActivity(), ImagePreviewActivity.class);
        previewActivityIntent.putParcelableArrayListExtra(ImagePreviewActivity.PREVIEW_IMAGES_EXTRA, images);
        previewActivityIntent.putExtra(ImagePreviewActivity.INITIAL_POSITION_EXTRA, position);
        startActivity(previewActivityIntent);
    }

    @Override
    public void onGroupClick(String title) {
        getActivity().setTitle(title);
        isInGroupView = false;
    }

    @Override
    public void setupGroups(ArrayList<GalleryGroup> groups) {
        ((MainActivity) getActivity()).hideProgress();
        imagesRecyclerView.setAdapter(new GalleryAdapter(groups, this));
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

    public boolean isInGroupView() {
        return isInGroupView;
    }

    public void setInGroupView(boolean inGroupView) {
        isInGroupView = inGroupView;
        if (inGroupView) {
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

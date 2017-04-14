package com.appmatic.baseapp.gallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.api.models.GalleryGroup;
import com.appmatic.baseapp.fragments.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class GalleryFragment extends BaseFragment implements GalleryView, GalleryAdapter.GalleryCallbacks {
    @BindView(R.id.images_recycler_view) RecyclerView imagesRecyclerView;
    private GalleryPresenter galleryPresenter;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        galleryPresenter = new GalleryPresenterImpl(this);
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_gallery);
    }

    @Override
    protected void setupViews() {
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        galleryPresenter.getImages();
    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void onImageClick(String url) {

    }

    @Override
    public void setupGroups(ArrayList<GalleryGroup> groups) {
        imagesRecyclerView.setAdapter(new GalleryAdapter(groups, this));
    }

    @Override
    public void setImageError() {

    }
}

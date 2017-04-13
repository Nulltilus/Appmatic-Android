package com.appmatic.baseapp.gallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.api.models.GalleryGroup;
import com.appmatic.baseapp.fragments.BaseFragment;

import java.util.ArrayList;

public class GalleryFragment extends BaseFragment implements GalleryView, GalleryAdapter.GalleryCallbacks {
    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_gallery);
    }

    @Override
    protected void setupViews() {

    }

    @Override
    protected void setListeners() {

    }

    @Override
    public void onImageClick(String url) {

    }

    @Override
    public void setupGroups(ArrayList<GalleryGroup> groups) {

    }

    @Override
    public void setupImages(ArrayList<GalleryGroup.Image> images) {

    }
}

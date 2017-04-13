package com.appmatic.baseapp.gallery;

import com.appmatic.baseapp.api.models.GalleryGroup;

import java.util.ArrayList;

/**
 * Created by grender on 13/04/17.
 */

class GalleryPresenterImpl implements GalleryPresenter, GalleryInteractor.OnImagesReceivedListener {
    private GalleryView galleryView;
    private GalleryInteractor galleryInteractor;

    GalleryPresenterImpl(GalleryView galleryView) {
        this.galleryView = galleryView;
        this.galleryInteractor = new GalleryInteractorImpl();
    }

    @Override
    public void getImages() {
        galleryInteractor.retrieveImages(((GalleryFragment) galleryView).getActivity(), this);
    }

    @Override
    public void onDestroy() {
        this.galleryView = null;
    }

    @Override
    public void onImagesReceived(ArrayList<GalleryGroup> groups) {
        galleryView.setupGroups(groups);
    }

    @Override
    public void onImagesError() {
        galleryView.setImageError();
    }
}

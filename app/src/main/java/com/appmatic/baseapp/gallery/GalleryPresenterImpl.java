package com.appmatic.baseapp.gallery;

import com.appmatic.baseapp.api.models.GalleryGroup;

import java.util.ArrayList;

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

class GalleryPresenterImpl implements GalleryPresenter, GalleryInteractor.OnImagesReceivedListener {
    private GalleryView galleryView;
    private GalleryInteractor galleryInteractor;

    GalleryPresenterImpl(GalleryView galleryView) {
        this.galleryView = galleryView;
        galleryInteractor = new GalleryInteractorImpl();
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

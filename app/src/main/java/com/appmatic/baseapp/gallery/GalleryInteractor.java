package com.appmatic.baseapp.gallery;

import com.appmatic.baseapp.api.models.GalleryGroup;

import java.util.ArrayList;

/**
 * Created by grender on 13/04/17.
 */

interface GalleryInteractor {
    void retrieveImages(OnImagesReceivedListener onImagesReceivedListener);

    interface OnImagesReceivedListener {
        void onImagesReceived(ArrayList<GalleryGroup> groups);
        void onImagesError();
    }
}

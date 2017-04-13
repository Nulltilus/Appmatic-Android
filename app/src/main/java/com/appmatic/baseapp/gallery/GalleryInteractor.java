package com.appmatic.baseapp.gallery;

import android.content.Context;
import android.support.annotation.NonNull;

import com.appmatic.baseapp.api.models.GalleryGroup;

import java.util.ArrayList;

/**
 * Created by grender on 13/04/17.
 */

interface GalleryInteractor {
    void retrieveImages(@NonNull Context context, OnImagesReceivedListener onImagesReceivedListener);

    interface OnImagesReceivedListener {
        void onImagesReceived(ArrayList<GalleryGroup> groups);
        void onImagesError();
    }
}

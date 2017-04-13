package com.appmatic.baseapp.gallery;

import com.appmatic.baseapp.api.models.GalleryGroup;

import java.util.ArrayList;

/**
 * Created by grender on 13/04/17.
 */

interface GalleryView {
    void setupGroups(ArrayList<GalleryGroup> groups);
    void setupImages(ArrayList<GalleryGroup.Image> images);
}

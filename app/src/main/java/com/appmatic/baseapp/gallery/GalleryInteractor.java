package com.appmatic.baseapp.gallery;

import android.content.Context;
import android.support.annotation.NonNull;

import com.appmatic.baseapp.api.models.GalleryGroup;

import java.util.ArrayList;

/**
 * Appmatic
 * Copyright (C) 2016 - Nulltilus
 * <p>
 * This file is part of Appmatic.
 * <p>
 * Appmatic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * <p>
 * Appmatic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Appmatic.  If not, see <http://www.gnu.org/licenses/>.
 */

interface GalleryInteractor {
    void retrieveImages(@NonNull Context context, OnImagesReceivedListener onImagesReceivedListener);

    interface OnImagesReceivedListener {
        void onImagesReceived(ArrayList<GalleryGroup> groups);
        void onImagesError();
    }
}

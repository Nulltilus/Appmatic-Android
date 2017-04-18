package com.appmatic.baseapp.gallery;

import android.content.Context;
import android.support.annotation.NonNull;

import com.appmatic.baseapp.api.WebService;
import com.appmatic.baseapp.api.models.GalleryGroup;
import com.appmatic.baseapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

class GalleryInteractorImpl implements GalleryInteractor {
    @Override
    public void retrieveImages(@NonNull Context context, final OnImagesReceivedListener onImagesReceivedListener) {
        new WebService(context).getApiInterface().getGallery(Constants.APP_ID).enqueue(new Callback<ArrayList<GalleryGroup>>() {
            @Override
            public void onResponse(Call<ArrayList<GalleryGroup>> call, Response<ArrayList<GalleryGroup>> response) {
                onImagesReceivedListener.onImagesReceived(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<GalleryGroup>> call, Throwable t) {
                onImagesReceivedListener.onImagesError();
            }
        });
    }
}

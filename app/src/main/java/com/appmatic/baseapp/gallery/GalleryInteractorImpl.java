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
 * Created by grender on 13/04/17.
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

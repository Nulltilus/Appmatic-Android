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
        /*ArrayList<GalleryGroup> groups = new ArrayList<>();
        GalleryGroup g1 = new GalleryGroup();
        ArrayList<GalleryGroup.Image> images = new ArrayList<>();
        images.add(new GalleryGroup.Image("https://c1.staticflickr.com/3/2883/9292210470_fee10e8c02_b.jpg"));
        images.add(new GalleryGroup.Image("https://www.ocu.org/-/media/ocu/images/mobile/cervezas_500x281.jpg?h=-1&w=-1&la=es-ES&hash=587D8D128A143F24BC02C1F287987111F615D8DA"));
        g1.setImages(images);
        g1.setTitle("Cerveza");
        GalleryGroup g2 = new GalleryGroup();
        ArrayList<GalleryGroup.Image> images2 = new ArrayList<>();
        images2.add(new GalleryGroup.Image("http://reseblavapies.net/wp-content/uploads/2016/06/hidromiel-1-580x250.jpg"));
        g2.setImages(images2);
        g2.setTitle("Hidromiel");
        groups.add(g1);
        groups.add(g2);
        onImagesReceivedListener.onImagesReceived(groups);*/
    }
}

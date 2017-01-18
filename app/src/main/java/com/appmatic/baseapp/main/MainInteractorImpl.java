package com.appmatic.baseapp.main;

import com.appmatic.baseapp.models.api.WebService;
import com.appmatic.baseapp.models.api_models.AppContent;
import com.appmatic.baseapp.models.api_models.ExtraInfo;
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
 * (at your option) any later version.
 *
 * Appmatic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Appmatic.  If not, see <http://www.gnu.org/licenses/>.
 */

public class MainInteractorImpl implements MainInteractor {

    @Override
    public void getData(final OnDataRetrievedListener dataListener) {
        //dataListener.onDataReceived(new ArrayList<>(Arrays.asList(new Gson().fromJson(appDataJson, AppContent[].class))));
        new WebService().getApiInterface().getAppData(Constants.APP_ID).enqueue(new Callback<ArrayList<AppContent>>() {
            @Override
            public void onResponse(Call<ArrayList<AppContent>> call, Response<ArrayList<AppContent>> response) {
                dataListener.onDataReceived(response.body());

            }

            @Override
            public void onFailure(Call<ArrayList<AppContent>> call, Throwable t) {
                dataListener.onDataReceivedError();
            }
        });
    }

    @Override
    public void getExtraInfo(final OnExtraInfoListener extraItemsListener) {
        new WebService().getApiInterface().getExtraItems(Constants.APP_ID).enqueue(new Callback<ExtraInfo>() {
            @Override
            public void onResponse(Call<ExtraInfo> call, Response<ExtraInfo> response) {
                extraItemsListener.onExtraInfoReceived(response.body());
            }

            @Override
            public void onFailure(Call<ExtraInfo> call, Throwable t) {
                extraItemsListener.onExtraInfoError();
            }
        });
    }

}

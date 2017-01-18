package com.appmatic.baseapp.main;

import com.appmatic.baseapp.BaseApplication;
import com.appmatic.baseapp.R;
import com.appmatic.baseapp.models.api_models.AppContent;
import com.appmatic.baseapp.models.api_models.ExtraInfo;

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

public class MainPresenterImpl implements MainPresenter, MainInteractorImpl.OnDataRetrievedListener,
        MainInteractor.OnExtraInfoListener {

    private MainView mainView;
    private MainInteractor mainInteractor;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
        this.mainInteractor = new MainInteractorImpl();
    }

    @Override
    public void setUpDataFromServer() {
        this.mainInteractor.getData(this);
    }

    @Override
    public void onDestroy() {
        this.mainView = null;
    }

    @Override
    public void populateApp() {
        if (this.mainView != null) {
            this.mainView.showProgress(BaseApplication.getAppContext().getResources().getString(R.string.loading),
                    BaseApplication.getAppContext().getResources().getString(R.string.loading_msg));
            setUpDataFromServer();
        }
    }

    @Override
    public void getExtraItems() {
        this.mainInteractor.getExtraInfo(this);
    }

    @Override
    public void onDataReceived(ArrayList<AppContent> appContents) {
        if (this.mainView != null) {
            this.mainView.updateAllContent(appContents);
        }
    }

    @Override
    public void onDataReceivedError() {
        if (this.mainView != null) {
            this.mainView.handleInternetError(null);
        }
    }

    @Override
    public void onExtraInfoReceived(ExtraInfo extraInfo) {
        if (this.mainView != null) {
            this.mainView.setUpRemainingContent(extraInfo);
        }
    }

    @Override
    public void onExtraInfoError() {
        if (this.mainView != null) {
            this.mainView.handleInternetError(null);
        }
    }

}

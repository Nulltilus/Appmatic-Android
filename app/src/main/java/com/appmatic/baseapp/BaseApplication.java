package com.appmatic.baseapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.google.android.gms.maps.MapView;

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

public class BaseApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Application appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApplication.appInstance = this;
        preLoadGoogleMaps();
    }

    public static Context getAppContext() {
        return BaseApplication.appInstance.getApplicationContext();
    }

    // As seen in http://stackoverflow.com/a/29246677
    public void preLoadGoogleMaps() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(BaseApplication.getAppContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                } catch (Exception ignored) {}
            }
        }).start();
    }
}

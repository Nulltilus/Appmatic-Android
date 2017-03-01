package com.appmatic.baseapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import static android.content.Context.CONNECTIVITY_SERVICE;

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

public class InternetUtils {
    public static boolean isInternetAvailable(@NonNull Context context) {
        NetworkInfo ni = ((ConnectivityManager) context.
                getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return ni != null && ni.isConnected() && ni.isAvailable();
    }
}

package com.appmatic.baseapp.fcm;

import android.content.Context;

import com.appmatic.baseapp.utils.Constants;
import com.google.firebase.messaging.FirebaseMessaging;

import es.dmoral.prefs.Prefs;

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

public class FCMHelper {
    public static void subscribeToFCMTopic(Context context) {
        if (Prefs.with(context).readBoolean(Constants.PREF_FIRST_BOOT, true)) {
            FirebaseMessaging.getInstance().subscribeToTopic(Constants.FCM_TOPIC);
            Prefs.with(context).writeBoolean(Constants.PREF_FIRST_BOOT, false);
        }
    }

}

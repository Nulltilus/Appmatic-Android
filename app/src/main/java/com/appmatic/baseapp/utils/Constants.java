package com.appmatic.baseapp.utils;

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

public class Constants {

    public static final String APP_ID = "0";
    public static final String FCM_TOPIC = "TOPIC_" + APP_ID;

    public static final String BASE_URL = "https://cpanel.appmatic.nulltilus.com/";
    public static final String API_URL = BASE_URL + "api/";

    public static final String APP_DATA_URL = "get_app_content";
    public static final String APP_EXTRA_INFO = "get_extra_info";
    public static final String APP_EXTRA_CONTACT = "get_contact";

    public static final String PREF_FIRST_BOOT = "preferences.first_boot";

    // 0 to 127 (reserved for predefined views)
    public static final int MENU_CONTACT_ID = 0;
    public static final String MENU_CONTACT_ICON = "ic_account_circle_black_48dp";

}

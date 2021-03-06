package com.appmatic.baseapp.api;

import com.appmatic.baseapp.api.models.AppContent;
import com.appmatic.baseapp.api.models.Contact;
import com.appmatic.baseapp.api.models.ExtraInfo;
import com.appmatic.baseapp.api.models.GalleryGroup;
import com.appmatic.baseapp.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

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

public interface ApiInterface {
    @GET(Constants.APP_DATA_ENDPOINT + "?")
    Call<ArrayList<AppContent>> getAppData(@Query("app_id") String app_id);

    @GET(Constants.APP_EXTRA_CONTACT_ENDPOINT + "?")
    Call<Contact> getAppContact(@Query("app_id") String app_id);

    @GET(Constants.APP_EXTRA_INFO_ENDPOINT + "?")
    Call<ExtraInfo> getExtraItems(@Query("app_id") String app_id);

    @GET(Constants.APP_GALLERY_ENDPOINT + "?")
    Call<ArrayList<GalleryGroup>> getGallery(@Query("app_id") String app_id);
}

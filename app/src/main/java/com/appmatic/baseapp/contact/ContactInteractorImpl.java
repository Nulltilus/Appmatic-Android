package com.appmatic.baseapp.contact;

import android.content.Context;
import android.support.annotation.NonNull;

import com.appmatic.baseapp.api.WebService;
import com.appmatic.baseapp.api.models.Contact;
import com.appmatic.baseapp.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

class ContactInteractorImpl implements ContactInteractor {
    @Override
    public void retrieveContactData(@NonNull Context context, final OnContactDataReceivedListener contactDataReceivedListener) {
        new WebService(context).getApiInterface().getAppContact(Constants.APP_ID).enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                contactDataReceivedListener.onContactDataReceived(response.body());
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                contactDataReceivedListener.onContactDataError();
            }
        });
    }
}

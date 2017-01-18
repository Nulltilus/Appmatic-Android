package com.appmatic.baseapp.contact;

import android.os.Bundle;

import com.appmatic.baseapp.models.api_models.Contact;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.CameraPosition;

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

public interface ContactView {

    void showFullscreenMap();

    void performPhoneCall();

    void sendEmail();

    void openWebsite();

    void populateContent(Contact contact);

    void setListeners();

    void setUpViews(Bundle savedInstanceState);

    void setBottomSheetState(int orientation);

    boolean collapseBottomSheet();

    CameraPosition getCameraPositionAtLocation();

    void showErrorDialog();
}

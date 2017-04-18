package com.appmatic.baseapp.contact;

import com.appmatic.baseapp.api.models.Contact;
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

interface ContactView {
    void showFullscreenMap();

    void performPhoneCall();

    void sendEmail();

    void openWebsite();

    void populateContent(Contact contact);

    void setBottomSheetState(int orientation);

    boolean collapseBottomSheet();

    CameraPosition getCameraPositionAtLocation();

    void showErrorDialog();
}

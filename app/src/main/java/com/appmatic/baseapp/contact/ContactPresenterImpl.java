package com.appmatic.baseapp.contact;

import com.appmatic.baseapp.api.models.Contact;

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

class ContactPresenterImpl implements ContactInteractorImpl.OnContactDataReceivedListener,
        ContactPresenter {
    private ContactView contactView;
    private ContactInteractor contactInteractor;

    ContactPresenterImpl(ContactView contactView) {
        this.contactView = contactView;
        contactInteractor = new ContactInteractorImpl();
    }

    @Override
    public void setUpData() {
        contactInteractor.retrieveContactData(((ContactFragment) contactView).getActivity(), this);
    }

    @Override
    public void onDestroy() {
        contactView = null;
    }

    @Override
    public void onContactDataReceived(Contact contact) {
        if (contactView != null)
            contactView.populateContent(contact);
    }

    @Override
    public void onContactDataError() {
        if (contactView != null)
            contactView.showErrorDialog();
    }
}

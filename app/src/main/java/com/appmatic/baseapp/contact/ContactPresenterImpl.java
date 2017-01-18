package com.appmatic.baseapp.contact;

import com.appmatic.baseapp.models.api_models.Contact;

/**
 * Appmatic
 * Copyright (C) 2016 - Nulltilus
 *
 * This file is part of Appmatic.
 * <p>
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

public class ContactPresenterImpl implements ContactInteractorImpl.OnContactDataReceivedListener,
        ContactPresenter {

    private ContactView contactView;
    private ContactInteractor contactInteractor;

    public ContactPresenterImpl(ContactView contactView) {
        this.contactView = contactView;
        this.contactInteractor = new ContactInteractorImpl();
    }

    @Override
    public void setUpData() {
        this.contactInteractor.retrieveContactData(this);
    }

    @Override
    public void onDestroy() {
        this.contactView = null;
    }

    @Override
    public void onContactDataReceived(Contact contact) {
        if (this.contactView != null)
            this.contactView.populateContent(contact);
    }

    @Override
    public void onContactDataError() {
        if (this.contactView != null)
            this.contactView.showErrorDialog();
    }
}

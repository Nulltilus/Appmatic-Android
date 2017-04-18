package com.appmatic.baseapp.api.models;

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

public class Contact {
    private String contact_email;
    private float latitude;
    private float longitude;
    private String contact_address;
    private String contact_name;
    private String contact_phone;
    private String contact_website;

    public Contact(String contact_name, String contact_email, String contact_phone, float latitude, float longitude, String contact_address, String contact_website) {
        this.contact_name = contact_name;
        this.contact_email = contact_email;
        this.contact_phone = contact_phone;
        this.contact_address = contact_address;
        this.contact_website = contact_website;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getContact_phone() {
        return this.contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_email() {
        return this.contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getName() {
        return this.contact_name;
    }

    public void setName(String name) {
        this.contact_name = name;
    }

    public String getContact_website() {
        return contact_website;
    }

    public void setContact_website(String contact_website) {
        this.contact_website = contact_website;
    }

    public String getContact_address() {
        return contact_address;
    }

    public void setContact_address(String contact_address) {
        this.contact_address = contact_address;
    }
}

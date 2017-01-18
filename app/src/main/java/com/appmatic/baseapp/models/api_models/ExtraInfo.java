package com.appmatic.baseapp.models.api_models;

import com.appmatic.baseapp.contact.ContactFragment;

import java.util.ArrayList;

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


public class ExtraInfo {

    public static final String TYPE_CONTACT_ITEM = "TYPE_CONTACT_ITEM";

    private boolean navigation_bar_colored;
    private String android_drawer_header_color;
    private String android_drawer_header_main_text;
    private String android_drawer_header_sub_text;
    private ArrayList<String> extra_items;

    public ExtraInfo() {
    }

    public ExtraInfo(boolean navigation_bar_colored, String android_drawer_header_color,
                     String android_drawer_header_main_text, String android_drawer_header_sub_text, ArrayList<String> extra_items) {
        this.navigation_bar_colored = navigation_bar_colored;
        this.android_drawer_header_color = android_drawer_header_color;
        this.android_drawer_header_main_text = android_drawer_header_main_text;
        this.android_drawer_header_sub_text = android_drawer_header_sub_text;
        this.extra_items = extra_items;
    }

    public static String getTagByItemName(String itemName) {
        switch (itemName) {
            case TYPE_CONTACT_ITEM:
                return ContactFragment.class.toString();
            default:
                return "";
        }
    }

    public ArrayList<String> getExtra_items() {
        return extra_items;
    }

    public void setExtra_items(ArrayList<String> extra_items) {
        this.extra_items = extra_items;
    }

    public boolean isNavigation_bar_colored() {
        return navigation_bar_colored;
    }

    public void setNavigation_bar_colored(boolean navigation_bar_colored) {
        this.navigation_bar_colored = navigation_bar_colored;
    }

    public String getAndroid_drawer_header_color() {
        return android_drawer_header_color;
    }

    public void setAndroid_drawer_header_color(String android_drawer_header_color) {
        this.android_drawer_header_color = android_drawer_header_color;
    }

    public String getAndroid_drawer_header_main_text() {
        return android_drawer_header_main_text;
    }

    public void setAndroid_drawer_header_main_text(String android_drawer_header_main_text) {
        this.android_drawer_header_main_text = android_drawer_header_main_text;
    }

    public String getAndroid_drawer_header_sub_text() {
        return android_drawer_header_sub_text;
    }

    public void setAndroid_drawer_header_sub_text(String android_drawer_header_sub_text) {
        this.android_drawer_header_sub_text = android_drawer_header_sub_text;
    }
}

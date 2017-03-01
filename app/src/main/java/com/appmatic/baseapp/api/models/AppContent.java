package com.appmatic.baseapp.api.models;

import java.util.ArrayList;

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

public class AppContent {
    private int content_id;
    private ArrayList<Content> contents;
    private String icon_id;
    private String name;
    private int position;

    public AppContent(int content_id) {
        this.content_id = content_id;
    }

    public AppContent(int content_id, int position, String name, String icon_id, ArrayList<Content> contents) {
        this.content_id = content_id;
        this.position = position;
        this.name = name;
        this.icon_id = icon_id;
        this.contents = contents;
    }

    public int getContent_id() {
        return this.content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon_id() {
        return this.icon_id;
    }

    public void setIcon_id(String icon_id) {
        this.icon_id = icon_id;
    }

    public ArrayList<Content> getContents() {
        return this.contents;
    }

    public void setContents(ArrayList<Content> contents) {
        this.contents = contents;
    }

    public boolean equals(Object o) {
        return !(o == null || !(o instanceof AppContent)) && ((AppContent) o).getContent_id() == getContent_id();
    }
}

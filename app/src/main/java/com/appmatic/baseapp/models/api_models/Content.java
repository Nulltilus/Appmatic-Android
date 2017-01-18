package com.appmatic.baseapp.models.api_models;

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

public class Content {
    public static final String TABLE_COLUMN_DIVIDER = ":::";
    public static final String TABLE_ROW_DIVIDER = ";;;";

    public static final String TYPE_SEPARATOR = "TYPE_SEPARATOR";
    public static final String TYPE_IMAGE = "TYPE_IMAGE";
    public static final String TYPE_TABLE = "TYPE_TABLE";
    public static final String TYPE_TEXT = "TYPE_TEXT";
    public static final String TYPE_TITLE = "TYPE_TITLE";
    public static final String TYPE_YOUTUBE = "TYPE_YOUTUBE";

    public static final String EXTRA_DELIMITER = "ǁ";
    public static final String EXTRA_HAS_STRIPES = "HAS_STRIPES";
    public static final String EXTRA_HAS_HEADER = "HAS_HEADER";

    private String content;
    private String extras;
    private String type;

    public Content(String type, String extras, String content) {
        this.type = type;
        this.extras = extras;
        this.content = content;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtras() {
        return this.extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

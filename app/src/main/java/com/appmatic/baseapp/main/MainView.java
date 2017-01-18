package com.appmatic.baseapp.main;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.appmatic.baseapp.models.api_models.AppContent;
import com.appmatic.baseapp.models.api_models.ExtraInfo;

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

public interface MainView {

    void showProgress(String title, String message);

    void hideProgress();

    void handleInternetError(@Nullable final Fragment from);

    void updateAllContent(ArrayList<AppContent> appContents);

    void setListeners();

    void setUpViews();

    void handleFirstContentState(ArrayList<String> extraItems);

    void setUpRemainingContent(ExtraInfo extraInfo);

    void addFragment(Fragment fragmentToAdd);

    void closeDrawer();
}

package com.appmatic.baseapp.content_container;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.api.models.AppContent;
import com.appmatic.baseapp.api.models.Content;
import com.appmatic.baseapp.fragments.BaseFragment;
import com.appmatic.baseapp.utils.AppmaticUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;

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

public class ContentContainerFragment extends BaseFragment implements ContentContainerView {
    @BindView(R.id.content_container_layout)
    LinearLayout contentContainer;
    @BindView(R.id.content_scrollview)
    ScrollView contentScrollView;

    public static ContentContainerFragment newInstance() {
        return new ContentContainerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_content_container);
    }

    @Override
    public void updateFragmentContents(AppContent fragmentContents) {
        clearFragmentContents();
        onFragmentReadyListener.fragmentReady();
        for (int i = 0; i < fragmentContents.getContents().size(); i++) {
            Content content = fragmentContents.getContents().get(i);
            View newView = AppmaticUtils
                    .parseContent(getActivity(), content, i == 0, i == fragmentContents.getContents().size() - 1);

            if (newView instanceof ImageView)
                Glide.with(getActivity()).load(content.getContent()).into(((ImageView) newView));
            this.contentContainer.addView(newView);
        }
    }

    public int getChildCount() {
        return this.contentContainer.getChildCount();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setupViews() {
        // ignored
    }

    @Override
    protected void setListeners() {
        // ignored
    }

    @Override
    public void clearFragmentContents() {
        this.contentContainer.removeAllViews();
        this.contentScrollView.scrollTo(0, 0);
    }

}

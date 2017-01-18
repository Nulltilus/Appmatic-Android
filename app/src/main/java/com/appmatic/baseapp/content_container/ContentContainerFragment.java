package com.appmatic.baseapp.content_container;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.fragment.BaseFragment;
import com.appmatic.baseapp.models.api_models.AppContent;
import com.appmatic.baseapp.models.api_models.Content;
import com.appmatic.baseapp.utils.AppmaticUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

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

public class ContentContainerFragment extends BaseFragment implements ContentContainerView {

    @BindView(R.id.content_container_layout)
    LinearLayout contentContainer;
    @BindView(R.id.content_scrollview)
    ScrollView contentScrollView;

    public static ContentContainerFragment newInstance() {
        return new ContentContainerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_container, container, false);
        ButterKnife.bind(this, view);
        return view;
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
                Glide.with(this).load(content.getContent()).into(((ImageView) newView));
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
    public void clearFragmentContents() {
        this.contentContainer.removeAllViews();
        this.contentScrollView.scrollTo(0, 0);
    }

}

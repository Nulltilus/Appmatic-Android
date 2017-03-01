package com.appmatic.baseapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

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

public abstract class BaseFragment extends Fragment {
    protected OnFragmentReadyListener onFragmentReadyListener;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState, @LayoutRes int contentViewId) {
        View view = inflater.inflate(contentViewId, container, false);
        ButterKnife.bind(this, view);

        setupViews();
        setListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onFragmentReadyListener.fragmentReady();
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof OnFragmentReadyListener)
            this.onFragmentReadyListener = (OnFragmentReadyListener) context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        this.onFragmentReadyListener = null;
        super.onDetach();
    }

    protected abstract void setupViews();

    protected abstract void setListeners();

    public interface OnFragmentReadyListener {
        void fragmentReady();
    }
}

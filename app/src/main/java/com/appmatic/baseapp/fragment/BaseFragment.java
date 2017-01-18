package com.appmatic.baseapp.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

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

public class BaseFragment extends Fragment {

    protected OnFragmentReadyListener onFragmentReadyListener;

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

    public interface OnFragmentReadyListener {
        void fragmentReady();
    }
}

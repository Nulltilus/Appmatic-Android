package com.appmatic.baseapp.contact.trianglify;

import com.appmatic.baseapp.BaseApplication;
import com.appmatic.baseapp.R;
import com.manolovn.trianglify.generator.color.ColorGenerator;

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

public class ContactColorGenerator implements ColorGenerator {

    private static int[][] colors;
    private int randomIndex;
    private int index;

    static {
        colors = new int[][]{
            BaseApplication.getAppContext().getResources().getIntArray(R.array.ambers),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.blue_grays),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.blues),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.browns),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.cyans),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.deep_oranges),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.deep_purples),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.greens),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.greys),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.indigos),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.light_blues),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.light_greens),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.limes),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.oranges),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.pinks),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.purples),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.reds),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.teals),
            BaseApplication.getAppContext().getResources().getIntArray(R.array.yellows)
        };
    }

    public ContactColorGenerator() {
        this.index = 0;
        this.randomIndex = (int) (Math.random() * colors.length);
    }

    @Override
    public int nextColor() {
        if (index == colors[randomIndex].length)
            index = 0;
        return colors[randomIndex][index++];
    }

    @Override
    public void setCount(int count) {
        this.index = 0;
    }
}

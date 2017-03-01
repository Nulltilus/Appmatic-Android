package com.appmatic.baseapp.contact.trianglify;

import android.content.Context;
import android.support.annotation.NonNull;

import com.appmatic.baseapp.R;
import com.manolovn.trianglify.generator.color.ColorGenerator;

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

public class ContactColorGenerator implements ColorGenerator {
    private static int[][] colors;
    private int randomIndex;
    private int index;

    public ContactColorGenerator() {
        this.index = 0;
        this.randomIndex = (int) (Math.random() * colors.length);
    }

    public static void initColors(@NonNull Context context) {
        if (colors == null) {
            colors = new int[][]{
                    context.getResources().getIntArray(R.array.ambers),
                    context.getResources().getIntArray(R.array.blue_grays),
                    context.getResources().getIntArray(R.array.blues),
                    context.getResources().getIntArray(R.array.browns),
                    context.getResources().getIntArray(R.array.cyans),
                    context.getResources().getIntArray(R.array.deep_oranges),
                    context.getResources().getIntArray(R.array.deep_purples),
                    context.getResources().getIntArray(R.array.greens),
                    context.getResources().getIntArray(R.array.greys),
                    context.getResources().getIntArray(R.array.indigos),
                    context.getResources().getIntArray(R.array.light_blues),
                    context.getResources().getIntArray(R.array.light_greens),
                    context.getResources().getIntArray(R.array.limes),
                    context.getResources().getIntArray(R.array.oranges),
                    context.getResources().getIntArray(R.array.pinks),
                    context.getResources().getIntArray(R.array.purples),
                    context.getResources().getIntArray(R.array.reds),
                    context.getResources().getIntArray(R.array.teals),
                    context.getResources().getIntArray(R.array.yellows)
            };
        }
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

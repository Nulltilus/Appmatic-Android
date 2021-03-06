package com.appmatic.baseapp.gallery.preview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.activities.BaseActivity;
import com.appmatic.baseapp.api.models.GalleryGroup;
import com.appmatic.baseapp.gallery.adapters.ImagePreviewAdapter;
import com.appmatic.baseapp.gallery.callbacks.GallerySharedElementCallback;
import com.appmatic.baseapp.gallery.custom.DepthPageTransformer;
import com.appmatic.baseapp.gallery.custom.ExceptionFreeViewPager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Appmatic
 * Copyright (C) 2016 - Nulltilus
 *
 * This file is part of Appmatic.
 *
 * Appmatic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Appmatic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Appmatic.  If not, see <http://www.gnu.org/licenses/>.
 */

public class ImagePreviewActivity extends BaseActivity {
    public static final String PREVIEW_IMAGES_EXTRA = "PREVIEW_IMAGES_EXTRA";
    public static final String INITIAL_POSITION_EXTRA = "INITIAL_POSITION_EXTRA";
    public static final String LAST_POSITION_EXTRA = "LAST_POSITION_EXTRA";
    @BindView(R.id.preview_images_viewpager) ExceptionFreeViewPager previewImagesViewpager;
    private ArrayList<GalleryGroup.Image> images;
    private int initialPosition;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        images = getIntent().getParcelableArrayListExtra(PREVIEW_IMAGES_EXTRA);
        initialPosition = getIntent().getIntExtra(INITIAL_POSITION_EXTRA, 0);
        super.onCreate(savedInstanceState, R.layout.activity_image_preview);
    }

    @Override
    protected void setupViews() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        supportPostponeEnterTransition();
        GallerySharedElementCallback sharedElementCallback = new GallerySharedElementCallback();
        setEnterSharedElementCallback(sharedElementCallback);
        setTitle(getString(R.string.image_viewer_indicator, initialPosition + 1, images.size()));
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        previewImagesViewpager.setAdapter(new ImagePreviewAdapter(this, images, sharedElementCallback));
        previewImagesViewpager.setPageTransformer(true, new DepthPageTransformer());
        ((ImagePreviewAdapter) previewImagesViewpager.getAdapter()).setInitialPosition(initialPosition);
        previewImagesViewpager.setCurrentItem(initialPosition, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return false;
        }
    }


    @Override
    public void finish() {
        setResult();
        super.finish();
    }

    @Override
    public void finishAfterTransition() {
        setResult();
        super.finishAfterTransition();
    }

    private void setResult() {
        int position = previewImagesViewpager.getCurrentItem();

        Intent data = new Intent();
        data.putExtra(LAST_POSITION_EXTRA, position);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void setListeners() {
        previewImagesViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // ignored
            }

            @Override
            public void onPageSelected(int position) {
                setTitle(getString(R.string.image_viewer_indicator, position + 1, images.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // ignored
            }
        });
    }
}

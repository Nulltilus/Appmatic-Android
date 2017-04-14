package com.appmatic.baseapp.gallery.preview;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.activities.BaseActivity;
import com.appmatic.baseapp.api.models.GalleryGroup;
import com.appmatic.baseapp.gallery.adapters.ImagePreviewAdapter;

import java.util.ArrayList;

import butterknife.BindView;

public class ImagePreviewActivity extends BaseActivity {
    @BindView(R.id.preview_images_viewpager) ViewPager previewImagesViewpager;

    public static final String PREVIEW_IMAGES_EXTRA = "PREVIEW_IMAGES_EXTRA";
    public static final String INITIAL_POSITION_EXTRA = "INITIAL_POSITION_EXTRA";
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
        previewImagesViewpager.setAdapter(new ImagePreviewAdapter(this, images));
        previewImagesViewpager.setCurrentItem(initialPosition, false);
    }

    @Override
    protected void setListeners() {

    }
}

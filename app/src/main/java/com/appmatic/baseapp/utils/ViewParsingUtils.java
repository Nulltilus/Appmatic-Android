package com.appmatic.baseapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.api.models.Content;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.pixplicity.htmlcompat.HtmlCompat;

import java.util.Arrays;

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

public class ViewParsingUtils {
    private static int halfMargin = -1;
    private static int defaultMargin = -1;

    public static View parseContent(@NonNull final Context context, final Content content, boolean isFirstView,
                                    boolean isLastView) {
        String[] extras = content.getExtras().split(Content.EXTRA_DELIMITER);

        if (halfMargin == -1 || defaultMargin == -1) {
            halfMargin = (int) context.getResources().getDimension(R.dimen.half_default_margin);
            defaultMargin = (int) context.getResources().getDimension(R.dimen.default_margin);
        }

        switch (content.getType()) {
            case Content.TYPE_SEPARATOR:
                return inflateSeparator(context, isFirstView, isLastView);
            case Content.TYPE_TEXT:
                return inflateText(context, content.getContent(), isFirstView, isLastView);
            case Content.TYPE_IMAGE:
                return inflateImageView(context, content.getContent(), isFirstView, isLastView);
            case Content.TYPE_TABLE:
                return inflateTable(context, content.getContent(), extras, isFirstView, isLastView);
            case Content.TYPE_TITLE:
                return inflateTitle(context, content.getContent(), isFirstView, isLastView);
            case Content.TYPE_YOUTUBE:
                return inflateYouTubeVideo(context, content.getContent(), isFirstView, isLastView);
            default:
                return new View(context);
        }
    }

    private static View inflateSeparator(Context context, boolean isFirstView, boolean isLastView) {
        View separator = new View(context);
        separator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        final TypedArray typedArray = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        DeprecationUtils.setBackgroundDrawable(separator, typedArray.getDrawable(0));
        typedArray.recycle();

        if (isFirstView)
            setLinearViewMargins(separator, 0, defaultMargin, 0, halfMargin);
        else if (isLastView)
            setLinearViewMargins(separator, 0, halfMargin, 0, defaultMargin);
        else
            setLinearViewMargins(separator, 0, halfMargin, 0, halfMargin);

        return separator;
    }

    private static TextView inflateText(Context context, String text, boolean isFirstView, boolean isLastView) {
        TextView textView = new TextView(context);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(HtmlCompat.fromHtml(context,
                text.replace("\n", "<br><br>").replace("</p>", "").replace("<p>", ""), 0));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
        textView.setTextColor(ContextCompat.getColor(context, R.color.mainTextColor));
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        if (isFirstView)
            setLinearViewMargins(textView, defaultMargin, defaultMargin, defaultMargin, halfMargin);
        else if (isLastView)
            setLinearViewMargins(textView, defaultMargin, halfMargin, defaultMargin, defaultMargin);
        else
            setLinearViewMargins(textView, defaultMargin, halfMargin, defaultMargin, halfMargin);

        return textView;
    }

    private static ImageView inflateImageView(Context context, String url, boolean isFirstView, boolean isLastView) {
        ImageView image = new ImageView(context);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setAdjustViewBounds(true); // As seen in http://stackoverflow.com/a/23550010
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        image.setLayoutParams(params);

        Glide.with(context)
                .load(url)
                .into(image);

        if (isFirstView && isLastView)
            setLinearViewMargins(image, 0, 0, 0, 0);
        if (isFirstView)
            setLinearViewMargins(image, 0, 0, 0, halfMargin);
        else if (isLastView)
            setLinearViewMargins(image, 0, halfMargin, 0, 0);
        else
            setLinearViewMargins(image, 0, halfMargin, 0, halfMargin);

        return image;
    }

    private static HorizontalScrollView inflateTable(Context context, String tableContent, String[] extras,
                                                     boolean isFirstView, boolean isLastView) {
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        TableLayout tableLayout = new TableLayout(context);
        String[] rows = tableContent.split(Content.TABLE_ROW_DIVIDER);
        int primaryColor = ContextCompat.getColor(context, R.color.colorPrimary);
        int stripColor = Color.argb(35, Color.red(primaryColor), Color.green(primaryColor), Color.blue(primaryColor));
        HorizontalScrollView.LayoutParams horizontalScrollViewParams = new HorizontalScrollView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        horizontalScrollView.setLayoutParams(horizontalScrollViewParams);
        tableLayout.setLayoutParams(tableParams);
        tableLayout.setStretchAllColumns(true);
        for (int i = 0; i < rows.length; i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(rowParams);
            if (Arrays.asList(extras).contains(Content.EXTRA_HAS_STRIPES) && i % 2 != 0)
                tableRow.setBackgroundColor(stripColor);
            if (Arrays.asList(extras).contains(Content.EXTRA_HAS_HEADER) && i == 0)
                tableRow.setBackgroundResource(R.drawable.bottom_tablerow_border);
            String[] rowCells = rows[i].split(Content.TABLE_COLUMN_DIVIDER);
            for (int j = 0; j < rowCells.length; j++) {
                TextView tvCell = new TextView(context);
                tvCell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
                tvCell.setText(HtmlCompat.fromHtml(context, rowCells[j], 0));
                tvCell.setTextColor(ContextCompat.getColor(context, R.color.mainTextColor));
                int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                        context.getResources().getDisplayMetrics());
                tvCell.setPadding(padding, i == 0 ? 0 : padding, padding, padding);
                if (Arrays.asList(extras).contains(Content.EXTRA_HAS_HEADER) && i == 0) {
                    tableRow.setBackgroundResource(R.drawable.bottom_tablerow_border);
                    tvCell.setTypeface(null, Typeface.BOLD);
                }
                if (j == rowCells.length - 1 && rowCells.length > 1)
                    tvCell.setGravity(GravityCompat.END);
                tableRow.addView(tvCell);
            }
            tableLayout.addView(tableRow);
        }

        if (isFirstView && isLastView)
            setFrameViewMargins(horizontalScrollView, 0, defaultMargin, 0, 0);
        else if (isFirstView)
            setFrameViewMargins(horizontalScrollView, 0, defaultMargin, 0, halfMargin);
        else if (isLastView)
            setFrameViewMargins(horizontalScrollView, 0, halfMargin, 0, 0);
        else
            setFrameViewMargins(horizontalScrollView, 0, halfMargin, 0, halfMargin);

        horizontalScrollView.addView(tableLayout);
        return horizontalScrollView;
    }

    private static TextView inflateTitle(Context context, String title, boolean isFirstView, boolean isLastView) {
        TextView titleTextView = new TextView(context);
        titleTextView.setText(title);
        titleTextView.setTypeface(null, Typeface.BOLD);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f);
        titleTextView.setTextColor(ContextCompat.getColor(context, R.color.mainTextColor));
        titleTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        if (isFirstView)
            setLinearViewMargins(titleTextView, defaultMargin, defaultMargin, defaultMargin, halfMargin);
        else if (isLastView)
            setLinearViewMargins(titleTextView, defaultMargin, halfMargin, defaultMargin, defaultMargin);
        else
            setLinearViewMargins(titleTextView, defaultMargin, halfMargin, defaultMargin, halfMargin);

        return titleTextView;
    }

    private static FrameLayout inflateYouTubeVideo(final Context context, final String videoId, boolean isFirstView, boolean isLastView) {
        final FrameLayout videoLayout = new FrameLayout(context);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        videoLayout.setForeground(ContextCompat.getDrawable(context, outValue.resourceId));

        final ImageView youTubeThumbnailView = new ImageView(context);
        final ImageView playLogo = new ImageView(context);
        playLogo.setVisibility(View.GONE);
        playLogo.setImageResource(R.drawable.youtube_play_logo);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context,
                        context.getString(R.string.google_dev_api_key), videoId, 0, true, true);
                if (intent.resolveActivity(context.getPackageManager()) != null)
                    context.startActivity(intent);
                else
                    AppmaticUtils.openPlayStore(context, "com.google.android.youtube");

            }
        });
        Glide.with(context)
                .load("https://img.youtube.com/vi/" + videoId + "/0.jpg")
                .centerCrop()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        youTubeThumbnailView.setImageDrawable(resource);
                        playLogo.setVisibility(View.VISIBLE);
                    }
                });

        youTubeThumbnailView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout.LayoutParams thumbnailParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int) context.getResources().getDimension(R.dimen.youtube_thumbnail));
        thumbnailParams.gravity = Gravity.CENTER_HORIZONTAL;
        youTubeThumbnailView.setLayoutParams(thumbnailParams);

        FrameLayout.LayoutParams playLogoParams = new FrameLayout.LayoutParams(
                (int) TypedValue.applyDimension
                        (TypedValue.COMPLEX_UNIT_DIP, 96, context.getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension
                        (TypedValue.COMPLEX_UNIT_DIP, 96, context.getResources().getDisplayMetrics()));
        playLogoParams.gravity = Gravity.CENTER;
        playLogo.setLayoutParams(playLogoParams);

        LinearLayout.LayoutParams frameLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        videoLayout.addView(youTubeThumbnailView);
        videoLayout.addView(playLogo);

        if (isFirstView && isLastView)
            videoLayout.setLayoutParams(injectFrameLayoutMargins(frameLayoutParams, 0, 0, 0, 0));
        else if (isFirstView)
            videoLayout.setLayoutParams(injectFrameLayoutMargins(frameLayoutParams, 0, 0, 0, halfMargin));
        else if (isLastView)
            videoLayout.setLayoutParams(injectFrameLayoutMargins(frameLayoutParams, 0, halfMargin, 0, 0));
        else
            videoLayout.setLayoutParams(injectFrameLayoutMargins(frameLayoutParams, 0, halfMargin, 0, halfMargin));

        return videoLayout;
    }

    private static void setLinearViewMargins(View view, int start, int top, int end, int bottom) {
        ((LinearLayout.LayoutParams) view.getLayoutParams()).setMargins(start, top, end, bottom);
    }

    private static void setFrameViewMargins(View view, int start, int top, int end, int bottom) {
        ((FrameLayout.LayoutParams) view.getLayoutParams()).setMargins(start, top, end, bottom);
    }

    private static LinearLayout.LayoutParams injectFrameLayoutMargins(LinearLayout.LayoutParams layoutParams,
                                                                      int start, int top, int end, int bottom) {
        layoutParams.setMargins(start, top, end, bottom);
        return layoutParams;
    }
}

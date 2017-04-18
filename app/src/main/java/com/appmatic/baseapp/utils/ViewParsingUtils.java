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
    public static View parseContent(@NonNull final Context context, final Content content, boolean isFirstView, boolean isLastView) {
        View newView = null;
        String[] extras = content.getExtras().split(Content.EXTRA_DELIMITER);

        int halfMargin = (int) context.getResources().getDimension(R.dimen.half_default_margin);
        int defaultMargin = (int) context.getResources().getDimension(R.dimen.default_margin);

        switch (content.getType()) {
            case Content.TYPE_SEPARATOR:
                newView = new View(context);
                newView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                final TypedArray typedArray = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
                DeprecationUtils.setBackgroundDrawable(newView, typedArray.getDrawable(0));
                typedArray.recycle();

                if (isFirstView)
                    setViewMargins(newView, 0, defaultMargin, 0, halfMargin);
                else if (isLastView)
                    setViewMargins(newView, 0, halfMargin, 0, defaultMargin);
                else
                    setViewMargins(newView, 0, halfMargin, 0, halfMargin);

                break;
            case Content.TYPE_TEXT:
                newView = new TextView(context);
                ((TextView) newView).setMovementMethod(LinkMovementMethod.getInstance());
                ((TextView) newView).setText(HtmlCompat.fromHtml(context, content.getContent().replace("\n", "<br><br>").replace("</p>", "").replace("<p>", ""), 0));
                ((TextView) newView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
                ((TextView) newView).setTextColor(ContextCompat.getColor(context, R.color.mainTextColor));
                newView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                if (isFirstView)
                    setViewMargins(newView, defaultMargin, defaultMargin, defaultMargin, halfMargin);
                else if (isLastView)
                    setViewMargins(newView, defaultMargin, halfMargin, defaultMargin, defaultMargin);
                else
                    setViewMargins(newView, defaultMargin, halfMargin, defaultMargin, halfMargin);

                break;
            case Content.TYPE_IMAGE:
                newView = new ImageView(context);
                ((ImageView) newView).setScaleType(ImageView.ScaleType.CENTER_CROP);
                ((ImageView) newView).setAdjustViewBounds(true); // As seen in http://stackoverflow.com/a/23550010
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                newView.setLayoutParams(params);

                if (isFirstView && isLastView)
                    setViewMargins(newView, 0, 0, 0, 0);
                if (isFirstView)
                    setViewMargins(newView, 0, 0, 0, halfMargin);
                else if (isLastView)
                    setViewMargins(newView, 0, halfMargin, 0, 0);
                else
                    setViewMargins(newView, 0, halfMargin, 0, halfMargin);

                break;
            case Content.TYPE_TABLE:
                String[] rows = content.getContent().split(Content.TABLE_ROW_DIVIDER);
                int primaryColor = ContextCompat.getColor(context, R.color.colorPrimary);
                int stripColor = Color.argb(35, Color.red(primaryColor), Color.green(primaryColor), Color.blue(primaryColor));
                TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TableRow.LayoutParams rowParams = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                newView = new TableLayout(context);
                newView.setLayoutParams(tableParams);
                ((TableLayout) newView).setStretchAllColumns(true);
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
                        tvCell.setText(rowCells[j]);
                        tvCell.setTextColor(ContextCompat.getColor(context, R.color.mainTextColor));
                        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                                context.getResources().getDisplayMetrics());
                        tvCell.setPadding(padding, i == 0 ? 0 : padding, padding, padding);
                        if (Arrays.asList(extras).contains(Content.EXTRA_HAS_HEADER) && i == 0) {
                            tableRow.setBackgroundResource(R.drawable.bottom_tablerow_border);
                            tvCell.setTypeface(null, Typeface.BOLD);
                        }
                        if (j == rowCells.length - 1)
                            tvCell.setGravity(GravityCompat.END);
                        tableRow.addView(tvCell);
                    }
                    ((TableLayout) newView).addView(tableRow);
                }

                if (isFirstView && isLastView)
                    setViewMargins(newView, 0, defaultMargin, 0, 0);
                else if (isFirstView)
                    setViewMargins(newView, 0, defaultMargin, 0, halfMargin);
                else if (isLastView)
                    setViewMargins(newView, 0, halfMargin, 0, 0);
                else
                    setViewMargins(newView, 0, halfMargin, 0, halfMargin);

                break;
            case Content.TYPE_TITLE:
                newView = new TextView(context);
                ((TextView) newView).setText(content.getContent());
                ((TextView) newView).setTypeface(null, Typeface.BOLD);
                ((TextView) newView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f);
                ((TextView) newView).setTextColor(ContextCompat.getColor(context, R.color.mainTextColor));
                newView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                if (isFirstView)
                    setViewMargins(newView, defaultMargin, defaultMargin, defaultMargin, halfMargin);
                else if (isLastView)
                    setViewMargins(newView, defaultMargin, halfMargin, defaultMargin, defaultMargin);
                else
                    setViewMargins(newView, defaultMargin, halfMargin, defaultMargin, halfMargin);

                break;
            case Content.TYPE_YOUTUBE:
                newView = new FrameLayout(context);
                TypedValue outValue = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                // Casting is required, see http://stackoverflow.com/a/33386364/4208583
                ((FrameLayout) newView).setForeground(ContextCompat.getDrawable(context, outValue.resourceId));

                final ImageView youTubeThumbnailView = new ImageView(context);
                final ImageView playLogo = new ImageView(context);
                playLogo.setVisibility(View.GONE);
                playLogo.setImageResource(R.drawable.youtube_play_logo);

                final String videoId = content.getContent();
                newView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, context.getString(R.string.google_dev_api_key), videoId, 0, true, true);
                        if (intent.resolveActivity(context.getPackageManager()) != null)
                            context.startActivity(intent);
                        else
                            AppmaticUtils.openPlayStore(context, "com.google.android.youtube");

                    }
                });
                Glide.with(context).load("https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg").into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        youTubeThumbnailView.setImageDrawable(resource);
                        playLogo.setVisibility(View.VISIBLE);
                    }
                });

                youTubeThumbnailView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                youTubeThumbnailView.setAdjustViewBounds(true); // As seen in http://stackoverflow.com/a/23550010

                LinearLayout.LayoutParams thumbnailParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                thumbnailParams.gravity = Gravity.CENTER_HORIZONTAL;
                youTubeThumbnailView.setLayoutParams(thumbnailParams);

                FrameLayout.LayoutParams playLogoParams = new FrameLayout.LayoutParams(
                        (int) TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 96, context.getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension
                                (TypedValue.COMPLEX_UNIT_DIP, 96, context.getResources().getDisplayMetrics()));
                playLogoParams.gravity = Gravity.CENTER;
                playLogo.setLayoutParams(playLogoParams);

                LinearLayout.LayoutParams frameLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                ((FrameLayout) newView).addView(youTubeThumbnailView);
                ((FrameLayout) newView).addView(playLogo);

                if (isFirstView && isLastView)
                    newView.setLayoutParams(injectFrameLayoutMargins(frameLayoutParams, 0, 0, 0, 0));
                else if (isFirstView)
                    newView.setLayoutParams(injectFrameLayoutMargins(frameLayoutParams, 0, 0, 0, halfMargin));
                else if (isLastView)
                    newView.setLayoutParams(injectFrameLayoutMargins(frameLayoutParams, 0, halfMargin, 0, 0));
                else
                    newView.setLayoutParams(injectFrameLayoutMargins(frameLayoutParams, 0, halfMargin, 0, halfMargin));
                break;
        }

        return newView;
    }

    private static void setViewMargins(View view, int start, int top, int end, int bottom) {
        ((LinearLayout.LayoutParams) view.getLayoutParams()).setMargins(start, top, end, bottom);
    }

    private static LinearLayout.LayoutParams injectFrameLayoutMargins(LinearLayout.LayoutParams layoutParams, int start, int top, int end, int bottom) {
        layoutParams.setMargins(start, top, end, bottom);
        return layoutParams;
    }
}

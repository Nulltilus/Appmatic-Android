package com.appmatic.baseapp.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appmatic.baseapp.R;
import com.appmatic.baseapp.activities.BaseActivity;
import com.appmatic.baseapp.api.models.AppContent;
import com.appmatic.baseapp.api.models.ExtraInfo;
import com.appmatic.baseapp.contact.ContactFragment;
import com.appmatic.baseapp.content_container.ContentContainerFragment;
import com.appmatic.baseapp.fcm.FCMHelper;
import com.appmatic.baseapp.fragments.BaseFragment;
import com.appmatic.baseapp.gallery.GalleryFragment;
import com.appmatic.baseapp.utils.AppmaticUtils;
import com.appmatic.baseapp.utils.Constants;
import com.appmatic.baseapp.utils.FragmentUtils;
import com.appmatic.baseapp.utils.InternetUtils;
import com.jakewharton.processphoenix.ProcessPhoenix;

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

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView, BaseFragment.OnFragmentReadyListener {
    private static final String CURRENT_FRAGMENT_TAG_EXTRA = "CURRENT_FRAGMENT_TAG_EXTRA";
    private static final String CURRENT_ITEM_POSITION_EXTRA = "CURRENT_ITEM_POSITION_EXTRA";
    private static final String LAST_SELECTED_MENU_ID = "LAST_SELECTED_MENU_ID";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private View headerView;
    private MainPresenter mainPresenter;
    private MaterialDialog progressDialog;
    private ArrayList<AppContent> items;
    private Menu currentNavigationViewMenu;
    private int lastSelectedMenuId;
    private AppContent currentItem;
    private int currentItemPosition;
    private String currentFragmentTag;
    private boolean shouldHandleState;
    private SparseIntArray menuIdPosition = new SparseIntArray();

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState, R.layout.activity_main);

        showProgress(null, getString(R.string.loading_msg));

        if (savedInstanceState != null) {
            shouldHandleState = true;
            if (savedInstanceState.containsKey(CURRENT_FRAGMENT_TAG_EXTRA))
                currentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_TAG_EXTRA);
            if (savedInstanceState.containsKey(CURRENT_ITEM_POSITION_EXTRA))
                currentItemPosition = savedInstanceState.getInt(CURRENT_ITEM_POSITION_EXTRA);
            if (savedInstanceState.containsKey(LAST_SELECTED_MENU_ID))
                lastSelectedMenuId = savedInstanceState.getInt(LAST_SELECTED_MENU_ID);
        } else {
            currentFragmentTag = "";
            lastSelectedMenuId = -1;
            shouldHandleState = false;
        }

        FCMHelper.subscribeToFCMTopic(this);

        mainPresenter.populateApp();

        headerView = navigationView.getHeaderView(0);
    }

    @Override
    protected void setupViews() {
        mainPresenter = new MainPresenterImpl(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the hamburger to arrow animation
            }

        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        currentNavigationViewMenu = navigationView.getMenu();
    }

    @Override
    protected void setListeners() {
        this.navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (currentFragmentTag != null && currentFragmentTag.equals(ContactFragment.class.toString())) {
            if (!((ContactFragment) getSupportFragmentManager().findFragmentByTag(currentFragmentTag)).collapseBottomSheet())
                super.onBackPressed();
            return;
        } else if (currentFragmentTag != null && currentFragmentTag.equals(GalleryFragment.class.toString())) {
            if (((GalleryFragment) getSupportFragmentManager().findFragmentByTag(currentFragmentTag)).getSelectedGroup() == -1)
                super.onBackPressed();
            else
                ((GalleryFragment) getSupportFragmentManager().findFragmentByTag(currentFragmentTag)).setSelectedGroup(-1);
            return;

        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        currentItemPosition = menuIdPosition.get(item.getItemId());
        if (currentFragmentTag != null && FragmentUtils.getTagByMenuId(item.getItemId()).equals(currentFragmentTag)) {
            closeDrawer();
            if (!currentFragmentTag.equals(ContentContainerFragment.class.toString()))
                return true;
        } else {
            currentFragmentTag = FragmentUtils.getTagByMenuId(item.getItemId());
        }

        if (item.getItemId() == Constants.MENU_CONTACT_ID) {
            addFragment(ContactFragment.newInstance());
            setTitle(getString(R.string.contact));
        } else if (item.getItemId() == Constants.MENU_GALLERY_ID) {
            addFragment(GalleryFragment.newInstance());
            setTitle(getString(R.string.gallery));
        } else {
            if (getSupportFragmentManager().findFragmentByTag(ContentContainerFragment.class.toString()) == null)
                addFragment(ContentContainerFragment.newInstance());
            currentItem = items.get(items.indexOf(new AppContent(item.getItemId())));
            setTitle(currentItem.getName());
            ((ContentContainerFragment) getSupportFragmentManager().findFragmentByTag(currentFragmentTag)).updateFragmentContents(currentItem);
        }

        return true;
    }

    @Override
    public void showProgress(String title, String message) {
        hideProgress();
        progressDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .progress(true, 0)
                .show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void handleInternetError() {
        hideProgress();
        new MaterialDialog.Builder(this)
                .title(getString(R.string.connection_error))
                .content(getString(R.string.connection_error_msg))
                .cancelable(false)
                .positiveText(getString(R.string.retry))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showProgress(null, getString(R.string.loading_msg));
                        mainPresenter.populateApp();
                    }
                })
                .negativeText(getString(R.string.exit))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        System.exit(0);
                    }
                })
                .show();
    }

    @Override
    public void updateAllContent(ArrayList<AppContent> items) {
        if (currentNavigationViewMenu.size() > 0)
            currentNavigationViewMenu.clear();

        this.items = items;

        if (!InternetUtils.isInternetAvailable(this))
            Snackbar.make(findViewById(R.id.main_coordinator_layout), R.string.snackbar_cache_text,
                    Snackbar.LENGTH_LONG).show();

        if (items == null) {
            handleInternetError();
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            AppContent item = items.get(i);
            menuIdPosition.put(item.getContent_id(), i);
            if (AppmaticUtils.getIconRes(item.getIcon_id(), this) == -1)
                currentNavigationViewMenu.add(R.id.main_group_menu, item.getContent_id(), i, item.getName());
            else
                currentNavigationViewMenu.add(R.id.main_group_menu, item.getContent_id(), i,
                        item.getName()).setIcon(AppmaticUtils.getIconRes(item.getIcon_id(), this));
        }

        mainPresenter.getExtraItems();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_FRAGMENT_TAG_EXTRA, currentFragmentTag);
        outState.putInt(CURRENT_ITEM_POSITION_EXTRA, currentItemPosition);
        outState.putInt(LAST_SELECTED_MENU_ID,
                currentNavigationViewMenu.size() > currentItemPosition ? currentNavigationViewMenu.getItem(currentItemPosition).getItemId() : -1);
    }

    @Override
    public void setUpRemainingContent(ExtraInfo extraInfo) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                extraInfo.isNavigation_bar_colored())
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        ((TextView) headerView.findViewById(R.id.tv_nav_main)).setText(extraInfo.getAndroid_drawer_header_main_text());
        ((TextView) headerView.findViewById(R.id.tv_nav_sub)).setText(extraInfo.getAndroid_drawer_header_sub_text());
        headerView.findViewById(R.id.navigation_header_layout).setBackgroundColor(Color.parseColor(extraInfo.getAndroid_drawer_header_color()));

        if (!extraInfo.getExtra_items().contains(ExtraInfo.TYPE_GALLERY_ITEM)) {
            menuIdPosition.put(Constants.MENU_GALLERY_ID, currentNavigationViewMenu.size());
            currentNavigationViewMenu.add(R.id.main_group_menu, Constants.MENU_GALLERY_ID, currentNavigationViewMenu.size(),
                    getString(R.string.gallery)).setIcon(AppmaticUtils.getIconRes(Constants.MENU_GALLERY_ICON, this));
        }

        if (extraInfo.getExtra_items().contains(ExtraInfo.TYPE_CONTACT_ITEM)) {
            menuIdPosition.put(Constants.MENU_CONTACT_ID, currentNavigationViewMenu.size());
            currentNavigationViewMenu.add(R.id.main_group_menu, Constants.MENU_CONTACT_ID, currentNavigationViewMenu.size(),
                    getString(R.string.contact)).setIcon(AppmaticUtils.getIconRes(Constants.MENU_CONTACT_ICON, this));
        }

        currentNavigationViewMenu.setGroupCheckable(R.id.main_group_menu, true, true);

        handleFirstContentState(extraInfo.getExtra_items());

        hideProgress();
    }

    @Override
    public void handleFirstContentState(ArrayList<String> extraItems) {
        try {
            if (currentNavigationViewMenu.size() >= currentItemPosition &&
                    currentNavigationViewMenu.size() != 0) {
                // Menu has changed since last update, should restart.
                if (lastSelectedMenuId != -1 &&
                        lastSelectedMenuId != currentNavigationViewMenu.getItem(currentItemPosition).getItemId()) {
                    hideProgress();
                    shouldHandleState = false;
                    lastSelectedMenuId = -1;
                    showProgress(null, getString(R.string.loading_msg));
                    mainPresenter.populateApp();
                    return;
                } else {
                    currentNavigationViewMenu.getItem(currentItemPosition).setChecked(true);
                }
            }

            if (shouldHandleState) {
                restoreContent();
                shouldHandleState = false;
            } else {
                if (items.size() > 0) {
                    currentFragmentTag = ContentContainerFragment.class.toString();
                    currentItem = items.get(0);
                    currentItemPosition = 0;
                    setTitle(currentItem.getName());
                    addFragment(ContentContainerFragment.newInstance());
                    ((ContentContainerFragment) getSupportFragmentManager().findFragmentByTag(currentFragmentTag)).updateFragmentContents(currentItem);
                } else if (!extraItems.contains(ExtraInfo.TYPE_GALLERY_ITEM)) {
                    currentFragmentTag = GalleryFragment.class.toString();
                    addFragment(GalleryFragment.newInstance());
                } else if (extraItems.contains(ExtraInfo.TYPE_CONTACT_ITEM)) {
                    currentFragmentTag = ContentContainerFragment.class.toString();
                    addFragment(ContactFragment.newInstance());
                }
            }
        } catch (Exception genericException) {
            hideProgress();
            shouldHandleState = false;
            lastSelectedMenuId = -1;
            showProgress(null, getString(R.string.loading_msg));
            mainPresenter.populateApp();
        }
    }

    @Override
    public void restoreContent() {
        if (currentFragmentTag.equals(ContentContainerFragment.class.toString())) {
            currentItem = items.get(currentItemPosition);
            setTitle(currentItem.getName());
            ((ContentContainerFragment) getSupportFragmentManager().findFragmentByTag(currentFragmentTag)).updateFragmentContents(currentItem);
        }
    }

    @SuppressLint("CommitTransaction")
    @Override
    public void addFragment(final Fragment fragmentToAdd) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentToAdd, currentFragmentTag)
                .commitNowAllowingStateLoss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        ((ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.menu_share_app)))
                .setShareIntent(getShareIntent());

        return true;
    }

    private Intent getShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_text, getString(R.string.app_name), getPackageName()));
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_restart_app:
                ProcessPhoenix.triggerRebirth(this);
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();
    }

    @Override
    public void fragmentReady() {
        closeDrawer();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        if (currentFragmentTag != null && currentFragmentTag.equals(GalleryFragment.class.toString())) {
            ((GalleryFragment) getSupportFragmentManager().findFragmentByTag(currentFragmentTag)).onActivityReenter(resultCode, data);
        }
    }

    @Override
    public void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}

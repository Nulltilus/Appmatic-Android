package com.appmatic.baseapp.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.appmatic.baseapp.contact.trianglify.ContactColorGenerator;
import com.appmatic.baseapp.content_container.ContentContainerFragment;
import com.appmatic.baseapp.fcm.FCMHelper;
import com.appmatic.baseapp.fragments.BaseFragment;
import com.appmatic.baseapp.utils.AppmaticUtils;
import com.appmatic.baseapp.utils.Constants;
import com.appmatic.baseapp.utils.InternetUtils;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.ArrayList;

import butterknife.BindView;

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

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView, BaseFragment.OnFragmentReadyListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    private View headerView;

    private MainPresenter mainPresenter;

    private MaterialDialog progressDialog;
    private ContentContainerFragment contentContainerFragment;
    private ArrayList<AppContent> items;
    private Menu currentNavigationViewMenu;
    private ContactFragment contactFragment;

    private AppContent currentItem;
    private String currentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState, R.layout.activity_main);

        FCMHelper.subscribeToFCMTopic(this);

        this.mainPresenter.populateApp();

        this.headerView = navigationView.getHeaderView(0);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (this.currentFragmentTag != null && this.currentFragmentTag.equals(ContactFragment.class.toString())) {
            if (!this.contactFragment.collapseBottomSheet())
                super.onBackPressed();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == Constants.MENU_CONTACT_ID) {
            if (this.currentFragmentTag.equals(ContactFragment.class.toString()))
                closeDrawer();
            this.currentFragmentTag = ContactFragment.class.toString();
            addFragment(this.contactFragment);
            setTitle(getString(R.string.contact));
        } else {
            if (getSupportFragmentManager().findFragmentByTag(ContentContainerFragment.class.toString()) == null) {
                this.currentFragmentTag = ContentContainerFragment.class.toString();
                addFragment(this.contentContainerFragment);
            }
            this.currentItem = this.items.get(this.items.indexOf(new AppContent(item.getItemId())));
            setTitle(this.currentItem.getName());
            this.contentContainerFragment.updateFragmentContents(this.currentItem);
        }

        return true;
    }

    @Override
    public void showProgress(String title, String message) {
        hideProgress();
        this.progressDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .progress(true, 0)
                .show();
    }

    @Override
    public void hideProgress() {
        if (this.progressDialog != null)
            this.progressDialog.dismiss();
    }

    @Override
    public void handleInternetError(@Nullable final Fragment from) {
        hideProgress();
        new MaterialDialog.Builder(this)
                .title(getString(R.string.connection_error))
                .content(getString(R.string.connection_error_msg))
                .cancelable(false)
                .positiveText(getString(R.string.retry))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (from instanceof ContactFragment)
                            contactFragment.retrieveDataCall();
                        else if (from == null) //from MainActivity
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
        this.items = items;

        if (!InternetUtils.isInternetAvailable(this))
            Snackbar.make(findViewById(R.id.main_coordinator_layout), R.string.snackbar_cache_text,
                    Snackbar.LENGTH_LONG).show();

        if (items == null) {
            handleInternetError(null);
            return;
        }

        for (AppContent item : items) {
            if (AppmaticUtils.getIconRes(item.getIcon_id(), this) == -1)
                this.currentNavigationViewMenu.add(R.id.main_group_menu, item.getContent_id(), item.getPosition(), item.getName());
            else
                this.currentNavigationViewMenu.add(R.id.main_group_menu, item.getContent_id(), item.getPosition(),
                        item.getName()).setIcon(AppmaticUtils.getIconRes(item.getIcon_id(), this));
        }

        this.mainPresenter.getExtraItems();
    }

    @SuppressLint("CommitTransaction")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (this.currentFragmentTag == null) {
            super.onConfigurationChanged(newConfig);
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .detach(getSupportFragmentManager().findFragmentByTag(this.currentFragmentTag))
                .commitNowAllowingStateLoss();
        super.onConfigurationChanged(newConfig);
        getSupportFragmentManager().beginTransaction()
                .attach(getSupportFragmentManager().findFragmentByTag(this.currentFragmentTag))
                .commitNowAllowingStateLoss();

        if (this.currentFragmentTag.equals(ContentContainerFragment.class.toString())) {
            this.contentContainerFragment.updateFragmentContents(this.currentItem);
        } else if (this.currentFragmentTag.equals(ContactFragment.class.toString())) {
            this.contactFragment.setBottomSheetState(newConfig.orientation);
        }
    }

    @Override
    public void setUpRemainingContent(ExtraInfo extraInfo) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                extraInfo.isNavigation_bar_colored())
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        ((TextView) this.headerView.findViewById(R.id.tv_nav_main)).setText(extraInfo.getAndroid_drawer_header_main_text());
        ((TextView) this.headerView.findViewById(R.id.tv_nav_sub)).setText(extraInfo.getAndroid_drawer_header_sub_text());
        this.headerView.findViewById(R.id.navigation_header_layout).setBackgroundColor(Color.parseColor(extraInfo.getAndroid_drawer_header_color()));

        if (extraInfo.getExtra_items().contains(ExtraInfo.TYPE_CONTACT_ITEM)) {
            this.currentNavigationViewMenu.add(R.id.main_group_menu, Constants.MENU_CONTACT_ID, this.currentNavigationViewMenu.size(),
                    getString(R.string.contact)).setIcon(AppmaticUtils.getIconRes(Constants.MENU_CONTACT_ICON, this));
            this.contactFragment = ContactFragment.newInstance();
        }

        this.currentNavigationViewMenu.setGroupCheckable(R.id.main_group_menu, true, true);

        handleFirstContentState(extraInfo.getExtra_items());

        this.hideProgress();
    }

    @Override
    public void handleFirstContentState(ArrayList<String> extraItems) {
        try {
            if (this.currentNavigationViewMenu.size() > 0)
                this.currentNavigationViewMenu.getItem(0).setChecked(true);

            if (items.size() > 0) {
                this.currentFragmentTag = ContentContainerFragment.class.toString();
                this.currentItem = this.items.get(0);
                setTitle(this.currentItem.getName());
                addFragment(this.contentContainerFragment);
                this.contentContainerFragment.updateFragmentContents(this.currentItem);
            } else if (extraItems.contains(ExtraInfo.TYPE_CONTACT_ITEM)) {
                ContactColorGenerator.initColors(this);
                this.currentFragmentTag = ContentContainerFragment.class.toString();
                addFragment(this.contactFragment);
                showProgress(getString(R.string.loading), getString(R.string.loading_contact_msg));
                setTitle(getString(R.string.contact));
            }

        } catch (ArrayIndexOutOfBoundsException aiobe) {
            ProcessPhoenix.triggerRebirth(this);
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
        this.mainPresenter.onDestroy();
    }

    @Override
    public void fragmentReady() {
        closeDrawer();
    }

    @Override
    public void closeDrawer() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void setListeners() {
        this.navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void setupViews() {
        this.contentContainerFragment = ContentContainerFragment.newInstance();

        this.mainPresenter = new MainPresenterImpl(this);

        setSupportActionBar(this.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, this.drawer, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the hamburger to arrow animation
            }

        };

        this.drawer.addDrawerListener(toggle);
        toggle.syncState();
        this.currentNavigationViewMenu = this.navigationView.getMenu();
    }
}

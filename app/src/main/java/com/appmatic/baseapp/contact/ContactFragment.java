package com.appmatic.baseapp.contact;


import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appmatic.baseapp.R;
import com.appmatic.baseapp.api.models.Contact;
import com.appmatic.baseapp.fragments.BaseFragment;
import com.appmatic.baseapp.main.MainActivity;
import com.appmatic.baseapp.utils.AppmaticUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class ContactFragment extends BaseFragment implements OnMapReadyCallback, ContactView {
    private static final String MAP_VIEW_SAVE_STATE = "MAP_VIEW_SAVE_STATE";
    @BindView(R.id.contact_coordinator_layout) CoordinatorLayout contactCoordinatorLayout;
    @BindView(R.id.fab_fullscreen_map) FloatingActionButton fabFullscreenMap;
    @BindView(R.id.map_view) MapView mapView;
    @BindView(R.id.contact_placeholder_image) ImageView contactPlaceholderImage;
    @BindView(R.id.contact_name_layout) LinearLayout contactNameLayout;
    @BindView(R.id.contact_phone_layout) LinearLayout contactPhoneLayout;
    @BindView(R.id.contact_email_layout) LinearLayout contactEmailLayout;
    @BindView(R.id.contact_website_layout) LinearLayout contactWebsiteLayout;
    @BindView(R.id.contact_bottomsheet) ScrollView contactBottomSheet;
    @BindView(R.id.contact_name_text) TextView contactNameText;
    @BindView(R.id.contact_address_text) TextView contactAddressText;
    @BindView(R.id.contact_phone_text) TextView contactPhoneText;
    @BindView(R.id.contact_email_text) TextView contactEmailText;
    @BindView(R.id.contact_website_text) TextView contactWebsiteText;
    @BindView(R.id.name_phone_divider) View namePhoneDivider;
    @BindView(R.id.phone_email_divider) View phoneEmailDivider;
    @BindView(R.id.email_website_divider) View emailWebsiteDivider;
    @BindView(R.id.website_end_divider) View websiteEndDivider;
    private GoogleMap googleMap;
    private ContactPresenter contactPresenter;
    private Contact contact;

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_contact);
        getActivity().setTitle(getString(R.string.contact));
        final Bundle mapViewSavedInstanceState =
                savedInstanceState != null ? savedInstanceState.getBundle(MAP_VIEW_SAVE_STATE) : null;
        if (mapView != null)
            mapView.onCreate(mapViewSavedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).showProgress(null, getString(R.string.loading_contact_msg));
        if (mapView != null)
            mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        imReady();
        this.googleMap = googleMap;
        retrieveDataCall();
    }

    public void retrieveDataCall() {
        if (checkPlayServices()) {
            contactPresenter.setUpData();
        }
    }

    @Override
    public void showFullscreenMap() {
        String format = "geo:0,0?q=" + Double.toString(this.contact.getLatitude()) + "," +
                Double.toString(contact.getLongitude()) + "(" + this.contact.getName() + ")";
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

        if (intent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(Intent.createChooser(intent, getString(R.string.open_location_with)));
        else
            AppmaticUtils.openPlayStore(getActivity(), "com.google.android.apps.maps");
    }

    @Override
    public void performPhoneCall() {
        Uri call = Uri.parse("tel:" + contact.getContact_phone());
        Intent intent = new Intent(Intent.ACTION_DIAL, call);
        startActivity(Intent.createChooser(intent, getString(R.string.call_with)));
    }

    @Override
    public void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{contact.getContact_email()});
        if (intent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(Intent.createChooser(intent, getString(R.string.send_email_with)));
    }

    @Override
    public void openWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = contact.getContact_website();
        if (!url.startsWith("http"))
            url = ("http://" + contact.getContact_website());
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(Intent.createChooser(intent, getString(R.string.open_website_with)));
    }

    @Override
    public void populateContent(final Contact contact) {
        this.contact = contact;
        if (!contact.getContact_address().isEmpty()) {
            mapView.setVisibility(View.VISIBLE);
            fabFullscreenMap.setVisibility(View.VISIBLE);
            contactAddressText.setText(contact.getContact_address());

            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            MapsInitializer.initialize(getActivity());

            contactNameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionAtLocation()));
                }
            });

            googleMap.getUiSettings().setMapToolbarEnabled(false);

            final LatLng loc = new LatLng(contact.getLatitude(), contact.getLongitude());

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionAtLocation()), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    googleMap.addMarker(new MarkerOptions().position(loc).title(contact.getName()).snippet(contact.getContact_address()));
                    googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                        @Override
                        public void onCameraMove() {
                            collapseBottomSheet();
                        }
                    });
                }

                @Override
                public void onCancel() {
                    // ignored
                }
            });
        } else {
            contactPlaceholderImage.setVisibility(View.VISIBLE);
            contactNameLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
            contactNameLayout.setFocusable(false);
            contactNameLayout.setClickable(false);
        }

        contactNameLayout.setVisibility(View.VISIBLE);
        namePhoneDivider.setVisibility(View.VISIBLE);
        contactNameText.setText(contact.getName().isEmpty() ? getString(R.string.app_name) : contact.getName());

        if (!contact.getContact_phone().isEmpty()) {
            contactPhoneLayout.setVisibility(View.VISIBLE);
            phoneEmailDivider.setVisibility(View.VISIBLE);
            contactPhoneText.setText(contact.getContact_phone());
        }

        if (!contact.getContact_email().isEmpty()) {
            contactEmailLayout.setVisibility(View.VISIBLE);
            emailWebsiteDivider.setVisibility(View.VISIBLE);
            contactEmailText.setText(contact.getContact_email());
        }

        if (!contact.getContact_website().isEmpty()) {
            contactWebsiteLayout.setVisibility(View.VISIBLE);
            websiteEndDivider.setVisibility(View.VISIBLE);
            // There is a bug with "http(s)://" and ellipsize, we need to remove it.
            contactWebsiteText.setText(contact.getContact_website().replaceFirst("(http(s)?://)?(www.)?", ""));
        }

        ((MainActivity) getActivity()).hideProgress();
    }

    @Override
    protected void setListeners() {
        fabFullscreenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFullscreenMap();
            }
        });

        contactPhoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performPhoneCall();
            }
        });

        contactEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        contactWebsiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebsite();
            }
        });

        final BottomSheetBehavior behavior = BottomSheetBehavior.from(contactBottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    contactBottomSheet.smoothScrollTo(0, 0);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // ignored
            }
        });
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());

        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result))
                googleAPI.getErrorDialog(getActivity(), result, 0).show();
            return false;
        }
        return true;
    }

    // As seen in http://stackoverflow.com/a/39525123/4208583
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //This MUST be done before saving any of your own or your base class's variables
        final Bundle mapViewSaveState = new Bundle(outState);
        if (mapView != null)
            mapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle(MAP_VIEW_SAVE_STATE, mapViewSaveState);
        //Add any other variables here.
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void setupViews() {
        contactPresenter = new ContactPresenterImpl(this);
        setBottomSheetState(getResources().getConfiguration().orientation);
    }

    @Override
    public void setBottomSheetState(int orientation) {
        if (!getResources().getBoolean(R.bool.tablet_mode)) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
                BottomSheetBehavior.from(contactBottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            else
                BottomSheetBehavior.from(contactBottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public boolean collapseBottomSheet() {
        if (BottomSheetBehavior.from(contactBottomSheet).getState() == BottomSheetBehavior.STATE_EXPANDED) {
            BottomSheetBehavior.from(contactBottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
            return true;
        }
        return false;
    }

    @Override
    public CameraPosition getCameraPositionAtLocation() {
        final double cameraOffset;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            cameraOffset = 0.0032;
        else
            cameraOffset = 0;

        return new CameraPosition.Builder()
                .target(new LatLng(contact.getLatitude() - cameraOffset, contact.getLongitude()))
                .zoom(16f)
                .bearing(0)
                .tilt(45f)
                .build();
    }

    @Override
    public void handleInternetError() {
        ((MainActivity) getActivity()).handleInternetError(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                ((MainActivity) getActivity()).showProgress(null, getString(R.string.loading_contact_msg));
                retrieveDataCall();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contactPresenter.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null)
            this.mapView.onLowMemory();
    }
}
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.appmatic.baseapp.R;
import com.appmatic.baseapp.contact.trianglify.ContactColorGenerator;
import com.appmatic.baseapp.fragment.BaseFragment;
import com.appmatic.baseapp.main.MainActivity;
import com.appmatic.baseapp.models.api_models.Contact;
import com.appmatic.baseapp.utils.AppmaticUtils;
import com.appmatic.baseapp.utils.DeprecationUtils;
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
import com.manolovn.trianglify.TrianglifyView;
import com.manolovn.trianglify.generator.point.RegularPointGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;

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

public class ContactFragment extends BaseFragment implements OnMapReadyCallback, ContactView {

    @BindView(R.id.contact_coordinator_layout)
    CoordinatorLayout contactCoordinatorLayout;

    @BindView(R.id.fab_fullscreen_map)
    FloatingActionButton fabFullscreenMap;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.contact_trianglify_view)
    TrianglifyView contactTrianglifyView;
    @BindView(R.id.contact_name_layout)
    LinearLayout contactNameLayout;
    @BindView(R.id.contact_phone_layout)
    LinearLayout contactPhoneLayout;
    @BindView(R.id.contact_email_layout)
    LinearLayout contactEmailLayout;
    @BindView(R.id.contact_website_layout)
    LinearLayout contactWebsiteLayout;

    @BindView(R.id.contact_bottomsheet)
    ScrollView contactBottomSheet;

    @BindView(R.id.contact_name_text)
    TextView contactNameText;
    @BindView(R.id.contact_address_text)
    TextView contactAddressText;
    @BindView(R.id.contact_phone_text)
    TextView contactPhoneText;
    @BindView(R.id.contact_email_text)
    TextView contactEmailText;
    @BindView(R.id.contact_website_text)
    TextView contactWebsiteText;

    @BindView(R.id.name_phone_divider)
    View namePhoneDivider;
    @BindView(R.id.phone_email_divider)
    View phoneEmailDivider;
    @BindView(R.id.email_website_divider)
    View emailWebsiteDivider;
    @BindView(R.id.website_end_divider)
    View websiteEndDivider;

    private GoogleMap googleMap;
    private static final String MAP_VIEW_SAVE_STATE = "MAP_VIEW_SAVE_STATE";

    private ContactPresenter contactPresenter;
    private Contact contact;

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setUpViews(savedInstanceState);
        setListeners();
        this.mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        retrieveDataCall();
    }

    public void retrieveDataCall() {
        if (checkPlayServices()) {
            ((MainActivity) getActivity()).showProgress(getString(R.string.loading), getString(R.string.loading_contact_msg));
            this.contactPresenter.setUpData();
        }
    }

    @Override
    public void showFullscreenMap() {
        String format = "geo:0,0?q=" + Double.toString(this.contact.getLatitude()) + "," +
                Double.toString(this.contact.getLongitude()) + "(" + this.contact.getName() + ")";
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
        Uri call = Uri.parse("tel:" + this.contact.getContact_phone());
        Intent intent = new Intent(Intent.ACTION_DIAL, call);
        startActivity(Intent.createChooser(intent, getString(R.string.call_with)));
    }

    @Override
    public void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{this.contact.getContact_email()});
        if (intent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(Intent.createChooser(intent, getString(R.string.send_email_with)));
    }

    @Override
    public void openWebsite() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = this.contact.getContact_website();
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
            this.mapView.setVisibility(View.VISIBLE);
            this.fabFullscreenMap.setVisibility(View.VISIBLE);
            this.contactAddressText.setText(contact.getContact_address());

            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            MapsInitializer.initialize(getActivity());

            this.contactNameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionAtLocation()));
                }
            });

            this.googleMap.getUiSettings().setMapToolbarEnabled(false);

            final LatLng loc = new LatLng(contact.getLatitude(), contact.getLongitude());

            this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionAtLocation()), new GoogleMap.CancelableCallback() {
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

                }
            });
        } else {
            this.contactTrianglifyView.getDrawable().setColorGenerator(new ContactColorGenerator());
            this.contactTrianglifyView.getDrawable().setPointGenerator(new RegularPointGenerator());
            this.contactTrianglifyView.setVisibility(View.VISIBLE);
            this.contactNameLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
            this.contactNameLayout.setFocusable(false);
            this.contactNameLayout.setClickable(false);
            this.contactTrianglifyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    collapseBottomSheet();
                }
            });
        }

        contactNameLayout.setVisibility(View.VISIBLE);
        namePhoneDivider.setVisibility(View.VISIBLE);
        this.contactNameText.setText(contact.getName().isEmpty() ? getString(R.string.app_name) : contact.getName());

        if (!contact.getContact_phone().isEmpty()) {
            contactPhoneLayout.setVisibility(View.VISIBLE);
            phoneEmailDivider.setVisibility(View.VISIBLE);
            this.contactPhoneText.setText(contact.getContact_phone());
        }

        if (!contact.getContact_email().isEmpty()) {
            contactEmailLayout.setVisibility(View.VISIBLE);
            emailWebsiteDivider.setVisibility(View.VISIBLE);
            this.contactEmailText.setText(contact.getContact_email());
        }

        if (!contact.getContact_website().isEmpty()) {
            contactWebsiteLayout.setVisibility(View.VISIBLE);
            websiteEndDivider.setVisibility(View.VISIBLE);
            this.contactWebsiteText.setText(contact.getContact_website());
        }

        ((MainActivity) getActivity()).hideProgress();
    }

    @Override
    public void setListeners() {
        this.fabFullscreenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFullscreenMap();
            }
        });

        this.contactPhoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performPhoneCall();
            }
        });

        this.contactEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        this.contactWebsiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebsite();
            }
        });

        BottomSheetBehavior.from(this.contactBottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(this.contactBottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                    contactBottomSheet.smoothScrollTo(0, 0);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
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
        this.mapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle(MAP_VIEW_SAVE_STATE, mapViewSaveState);
        //Add any other variables here.
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setUpViews(Bundle savedInstanceState) {
        final Bundle mapViewSavedInstanceState =
                savedInstanceState != null ? savedInstanceState.getBundle(MAP_VIEW_SAVE_STATE) : null;
        this.mapView.onCreate(mapViewSavedInstanceState);
        this.contactPresenter = new ContactPresenterImpl(this);

        setBottomSheetState(getResources().getConfiguration().orientation);
    }

    @Override
    public void setBottomSheetState(int orientation) {
        if (!getResources().getBoolean(R.bool.tablet_mode)) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
                BottomSheetBehavior.from(this.contactBottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            else
                BottomSheetBehavior.from(this.contactBottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public boolean collapseBottomSheet() {
        if (BottomSheetBehavior.from(this.contactBottomSheet).getState() == BottomSheetBehavior.STATE_EXPANDED) {
            BottomSheetBehavior.from(this.contactBottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
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
    public void showErrorDialog() {
        ((MainActivity) getActivity()).handleInternetError(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.contactPresenter.onDestroy();
        this.mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.mapView.onLowMemory();
    }
}
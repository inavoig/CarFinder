package br.com.rocha.carfinder.cars.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import br.com.rocha.carfinder.base.BaseActivityListener;
import br.com.rocha.carfinder.R;
import br.com.rocha.carfinder.util.AnimUtils;
import br.com.rocha.carfinder.util.GPSUtil;

public class CarsMapFragment extends Fragment
        implements CarsMapContract.View, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    private static final int GPS_REQUEST_CODE = 1;

    private ProgressBar mLoading;
    private FrameLayout mMapFrame;

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;

    private CarsMapContract.Presenter mCarsMapPresenter;
    private BaseActivityListener mBaseActivityListener;

    public CarsMapFragment() {
    }

    public static CarsMapFragment newInstance() {
        return new CarsMapFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mBaseActivityListener = (BaseActivityListener) context;

        } catch (Exception e) {
            Log.e("LISTENER_ERROR", "BaseActivityListener must be implement", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_REQUEST_CODE) {
            loadMapOnScreen();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mCarsMapPresenter.start();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cars_map, container, false);

        if (mMapFragment == null) {
            mMapFragment = SupportMapFragment.newInstance();
        }
        getChildFragmentManager().beginTransaction().replace(R.id.map, mMapFragment).commit();

        mLoading = view.findViewById(R.id.loading);
        mMapFrame = view.findViewById(R.id.map);

        return view;
    }

    @Override
    public void setPresenter(CarsMapContract.Presenter presenter) {
        mCarsMapPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            AnimUtils.replaceView(mMapFrame, mLoading);
        } else {
            AnimUtils.replaceView(mLoading, mMapFrame);
        }
    }

    @Override
    public void loadMapOnScreen() {
        if (getContext() != null && GPSUtil.isGPSEnabled(getContext())) {
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(connectionResult ->
                                Snackbar.make(Objects.requireNonNull(getView()),
                                        R.string.failed_getting_updated_location,
                                        Snackbar.LENGTH_LONG).show())
                        .addApi(LocationServices.API)
                        .build();
            }
            mMapFragment.getMapAsync(this);

        } else {
            showGPSAlert();
        }
    }

    @Override
    public void showLoadingCarsError() {
        Snackbar.make(Objects.requireNonNull(getView()), R.string.error_loading_cars,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNoCarsFound() {
        Snackbar.make(Objects.requireNonNull(getView()), R.string.no_cars_found,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showGPSAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.gps_enable_question)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, GPS_REQUEST_CODE);
                })
                .setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel());

        builder.create().show();
    }

    @Override
    public void addMarkerToMap(MarkerOptions carMarker) {
        mCarsMapPresenter.addCarMarker(mMap.addMarker(carMarker));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            mCarsMapPresenter.loadCarsOnMap();

            mMap.setOnMarkerClickListener(marker -> {
                mCarsMapPresenter.updateMapMarkers(marker);
                return true;
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        setCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Snackbar.make(Objects.requireNonNull(getView()), R.string.map_not_responding,
                Snackbar.LENGTH_LONG).show();
    }

    @SuppressLint("MissingPermission")
    private void setCurrentLocation() {
        if (getActivity() != null) {
            mBaseActivityListener.checkPermissionAndRun(() ->
                    LocationServices.getFusedLocationProviderClient(getActivity())
                            .getLastLocation()
                            .addOnSuccessListener(getActivity(), location -> {
                                if (location == null) {
                                    handleGetCurrentLocationError();
                                } else {
                                    mCurrentLocation = location;
                                    setCurrentLocationOnMap();
                                }
                            })
                            .addOnFailureListener(getActivity(), e ->
                                    handleGetCurrentLocationError()), Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    private void setCurrentLocationOnMap() {
        LatLng device = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_user_pin);

        mMap.addMarker(new MarkerOptions()
                .icon(icon)
                .title(getString(R.string.current_position))
                .position(device));
        CameraPosition camPos = new CameraPosition.Builder()
                .target(device)
                .zoom(15)
                .bearing(mCurrentLocation.getBearing())
                .tilt(0)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);
    }

    private void handleGetCurrentLocationError() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            setCurrentLocation();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

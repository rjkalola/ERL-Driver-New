package com.app.erldriver.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.erldriver.R;
import com.app.erldriver.callback.LocationUpdateCallBack;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;


public class LocationHelper {
    private Activity mContext;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback mLocationCallback;
    private double latitude, longitude;
    private Location mLocation;
    private boolean isPermission;
    private GoogleApiClient googleApiClient;
    private LocationUpdateCallBack listener;
    private String[] EXTERNAL_STORAGE = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    public LocationHelper(Activity context) {
        this.mContext = context;
        getLocation();
    }

    //for get current location here
    public void getLocation() {
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(3000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {
                        mLocation = location;
                        if (listener != null) {
                            listener.locationUpdate(mLocation);
                        }
                    }
                }
            };
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //for stop location update
    public void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    //for start location update
    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }

    public double getLatitude() {
        if (mLocation != null) {
            latitude = mLocation.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (mLocation != null) {
            longitude = mLocation.getLongitude();
        }

        return longitude;
    }

    public String getFullAddress(double latitude, double longitude) {
        String currentLocation = "";
        try {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            currentLocation = addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentLocation;
    }

    public String getCountryCode(double latitude, double longitude) {
        String country = "";
        try {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            country = addresses.get(0).getCountryCode();
            Log.e("test", "country:" + country);
            Log.e("test", "country Code:" + addresses.get(0).getCountryCode());
            Log.e("test", "addresss:" + addresses.get(0).getAddressLine(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return country;
    }


    public Address getGeocoderAddress(double latitude, double longitude) {
        Address address = null;
        try {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    public boolean isLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            EasyPermissions.requestPermissions(
                    mContext,
                    mContext.getString(R.string.msg_location_permission),
                    AppConstant.IntentKey.RC_LOCATION_PERM,
                    EXTERNAL_STORAGE);
            isPermission = false;
        } else {
            isPermission = true;
        }
        return isPermission;
    }

    public boolean isGPSEnabled() {
        boolean isGpsOn = false;
        if (isLocationPermission()) {
            LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (getLocationMode(mContext) == AppConstant.LocationMode.LOCATION_MODE_HIGH_ACCURACY) {
                    isGpsOn = true;
                    startLocationUpdates();
                } else {
                    showSettingsAlert();
                    isGpsOn = false;
                }
            } else {
                isGpsOn = false;
                enableLoc(mContext);
            }
        }

        return isGpsOn;
    }

    public void enableLoc(Activity context) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        googleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(connectionResult -> {
                }).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(context, AppConstant.IntentKey.LOCATION_SETTING_STATUS);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });
    }

    public int getLocationMode(Context context) {
        int locationMode = 0;
        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return locationMode;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setMessage(mContext.getString(R.string.msg_gps_enable));
        alertDialog.setPositiveButton(mContext.getString(R.string.settings), (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(intent);
        });
        alertDialog.setNegativeButton(mContext.getString(R.string.lbl_cancel), (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    public void setLocationUpdateListener(LocationUpdateCallBack callBack) {
        listener = callBack;
    }
}
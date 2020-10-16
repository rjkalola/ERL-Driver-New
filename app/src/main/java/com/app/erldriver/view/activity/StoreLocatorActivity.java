package com.app.erldriver.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erldriver.R;
import com.app.erldriver.adapter.StoreMarkerInfoWindowAdapter;
import com.app.erldriver.databinding.ActivityStoreLocatorBinding;
import com.app.erldriver.model.entity.info.ModuleInfo;
import com.app.erldriver.model.entity.info.ModuleSelection;
import com.app.erldriver.model.entity.response.StoreLocatorResponse;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.PopupMenuHelper;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.DashBoardViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StoreLocatorActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {
    private ActivityStoreLocatorBinding binding;
    private Context mContext;
    private int action;
    private DashBoardViewModel dashBoardViewModel;
    private StoreLocatorResponse storeLocatorData;
    private GoogleMap mMap;
    private List<Marker> listMarkers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_store_locator);
        mContext = this;
        listMarkers = new ArrayList<>();

        dashBoardViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(DashBoardViewModel.class);
        dashBoardViewModel.createView(this);
        dashBoardViewModel.storeLocatorResponse()
                .observe(this, getStoreLocatorResponse());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        binding.imgBack.setOnClickListener(this);
        binding.routSelectShop.setOnClickListener(this);
        binding.edtSelectShop.setOnClickListener(this);
        dashBoardViewModel.getStoreLocatorRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.routSelectShop:
            case R.id.edtSelectShop:
                showSelectStoreDialog(getStoreLocatorData().getInfo(), AppConstant.DialogIdentifier.SELECT_SHOP, binding.routSelectShop);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(marker -> {
            try {
                int position = Integer.parseInt(marker.getTag().toString());
                if (getStoreLocatorResponse() != null && !getStoreLocatorData().getInfo().isEmpty()
                        && !StringHelper.isEmpty(getStoreLocatorData().getInfo().get(position).getLatitude())
                        && !StringHelper.isEmpty(getStoreLocatorData().getInfo().get(position).getLongitude())) {
                    String geoUri = "http://maps.google.com/maps?q=loc:" +getStoreLocatorData().getInfo().get(position).getLatitude() + "," + getStoreLocatorData().getInfo().get(position).getLongitude() + " (" + getStoreLocatorData().getInfo().get(position).getName() + ")";
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.parseDouble(getStoreLocatorData().getInfo().get(position).getLatitude()), Double.parseDouble(getStoreLocatorData().getInfo().get(position).getLongitude()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    mContext.startActivity(intent);
                }

//                Bundle bundle = new Bundle();
//                if (viewMapType == AppConstant.Type.SINGLE_USER) {
//                    bundle.putInt(AppConstant.IntentKey.USER_ID, getInfo().getId());
//                } else {
//                    bundle.putInt(AppConstant.IntentKey.USER_ID, getModuleResponse().getInfo().get(position).getId());
//                }
//                ((BaseActivity) mContext).moveActivityForResult(mContext, ViewUserProfileActivity.class, false, false, AppConstant.IntentKey.VIEW_USER, bundle);
            } catch (Exception e) {

            }
        });
    }

    public Observer getStoreLocatorResponse() {
        return (Observer<StoreLocatorResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setStoreLocatorData(response);
                    addMarkers(response.getInfo());
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public void showSelectStoreDialog(List<ModuleInfo> list, int dialogIdentifier, View v) {
        if (list != null && list.size() > 0)
            PopupMenuHelper.showPopupMenu(mContext, v, list, dialogIdentifier);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ModuleSelection moduleInfo) {
        if (moduleInfo != null) {
            if (moduleInfo.getType() == AppConstant.DialogIdentifier.SELECT_SHOP)
                binding.edtSelectShop.setText(moduleInfo.getInfo().getName());
            if (listMarkers.get(moduleInfo.getPosition()) != null) {
                ModuleInfo info = moduleInfo.getInfo();
                LatLng currentLocation = new LatLng(Double.parseDouble(info.getLatitude()), Double.parseDouble(info.getLongitude()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 9.0f));
                listMarkers.get(moduleInfo.getPosition()).showInfoWindow();
            }

        }
    }

    public void addMarkers(List<ModuleInfo> list) {
        if (mMap != null && list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ModuleInfo info = list.get(i);

                info.setTag(i);
                if (!StringHelper.isEmpty(info.getLatitude())
                        && !StringHelper.isEmpty(info.getLongitude())) {
                    LatLng currentLocation = new LatLng(Double.parseDouble(info.getLatitude()), Double.parseDouble(info.getLongitude()));
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(currentLocation)
                            .title(info.getName())
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    marker.setTag(i);
                    listMarkers.add(i, marker);
                }
            }

            if (!StringHelper.isEmpty(list.get(0).getLatitude())
                    && !StringHelper.isEmpty(list.get(0).getLongitude())) {
                LatLng currentLocation = new LatLng(Double.parseDouble(list.get(0).getLatitude()), Double.parseDouble(list.get(0).getLongitude()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 9.0f));
                setMarkerWindowInfoAdapter(list);
            }
        }
    }

    public void setMarkerWindowInfoAdapter(List<ModuleInfo> list) {
        StoreMarkerInfoWindowAdapter customInfoWindow = new StoreMarkerInfoWindowAdapter(StoreLocatorActivity.this, list);
        mMap.setInfoWindowAdapter(customInfoWindow);
    }

    public StoreLocatorResponse getStoreLocatorData() {
        return storeLocatorData;
    }

    public void setStoreLocatorData(StoreLocatorResponse storeLocatorData) {
        this.storeLocatorData = storeLocatorData;
    }
}

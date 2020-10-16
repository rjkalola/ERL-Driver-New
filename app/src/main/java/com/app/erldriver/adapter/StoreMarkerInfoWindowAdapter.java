package com.app.erldriver.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erldriver.R;
import com.app.erldriver.databinding.RowStoreMarkerWindowBinding;
import com.app.erldriver.model.entity.info.ModuleInfo;
import com.app.utilities.callbacks.ImageLoadingListener;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class StoreMarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity context;
    private RowStoreMarkerWindowBinding binding;
    private List<ModuleInfo> list;
    private boolean notFirstTimeShowingInfoWindow;

    public StoreMarkerInfoWindowAdapter(Activity context, List<ModuleInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.row_store_marker_window, null);
        binding = DataBindingUtil.bind(view);
        int position = Integer.parseInt(marker.getTag().toString());

        binding.txtName.setText(list.get(position).getName());
        binding.txtAddress.setText(list.get(position).getAddress());

        if (notFirstTimeShowingInfoWindow) {
            notFirstTimeShowingInfoWindow = false;
        } else {
            notFirstTimeShowingInfoWindow = true;
        }

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void setImage(ImageView imageView, String url) {
        if (!StringHelper.isEmpty(url)) {
            GlideUtil.loadImageUsingGlideTransformation(url, imageView, Constant.TransformationType.CIRCLECROP_TRANSFORM, null, null, Constant.ImageScaleType.CENTER_CROP, 0, 0, "", 0, null);
        }
    }

    private class InfoWindowRefresher implements ImageLoadingListener {
        private Marker markerToRefresh;

        private InfoWindowRefresher(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        @Override
        public void onLoaded(@Nullable Bitmap bitmap, @Nullable Drawable glideDrawable) {
            markerToRefresh.showInfoWindow();
        }

        @Override
        public void onFailed(Exception exception) {

        }
    }
}
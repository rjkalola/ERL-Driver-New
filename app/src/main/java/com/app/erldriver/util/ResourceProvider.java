package com.app.erldriver.util;

import android.content.res.Resources;
import androidx.annotation.StringRes;

public class ResourceProvider {
    private  Resources mResources;
    public ResourceProvider(Resources resource) {
        this.mResources = resource;
    }

    private String getString(@StringRes Integer stringResId){
        return mResources.getString(stringResId);
    }
}

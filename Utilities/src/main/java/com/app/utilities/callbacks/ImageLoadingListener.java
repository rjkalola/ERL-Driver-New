package com.app.utilities.callbacks;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;

/**
 * Created by pc on 19-09-2017.
 */

public interface ImageLoadingListener {

    void onLoaded(@Nullable Bitmap bitmap , @Nullable Drawable glideDrawable);

    void onFailed(Exception exception);
}

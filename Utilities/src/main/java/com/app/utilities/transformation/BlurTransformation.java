package com.app.utilities.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by jalpa on 9/25/2017.
 */

public class BlurTransformation extends BitmapTransformation {

    private RenderScript renderScript;
    private int BLUR_RADIUS;

    public BlurTransformation(Context context, int radius) {
        super();
        renderScript = RenderScript.create(context);
        BLUR_RADIUS = radius;;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap blurredBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true);

        // Allocate memory for Renderscript to work with
        Allocation input = Allocation.createFromBitmap(renderScript, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(renderScript, input.getType());

        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        script.setInput(input);

        // Set the blur radius
        script.setRadius(BLUR_RADIUS);

        // Start the ScriptIntrinisicBlur
        script.forEach(output);

        // Copy the output to the blurred bitmap
        output.copyTo(blurredBitmap);

        return blurredBitmap;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update("blur transformation".getBytes());
    }
}

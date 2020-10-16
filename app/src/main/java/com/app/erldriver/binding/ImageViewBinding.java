package com.app.erldriver.binding;

import androidx.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.erldriver.R;
import com.app.erldriver.model.entity.dashboard.DashboardModel;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

/**
 * Created by Dhaval on 29-06-2017.
 * <p>
 * TODO : create all methods fpr image loading using data binding
 */


public final class ImageViewBinding {

    /* Activity name or Adapter name */

    @BindingAdapter({"userUrl"})
    public static void setImage(ImageView imageview, String url) {
//        Drawable mPlaceholderDrawable =  ResourcesCompat.getDrawable(
//                imageview.getContext().getResources(), R.drawable.photo_icon, null);
        Drawable mPlaceholderDrawable = null;
        if (!StringHelper.isEmpty(url)) {
            // AppUtils.loadRoundedImage(imageview.getContext(), url, mPlaceholderDrawable, imageview);
            GlideUtil.loadImage(url, imageview, mPlaceholderDrawable, mPlaceholderDrawable, Constant.ImageScaleType.CENTER_CROP, null);
        } else {
            imageview.setImageDrawable(mPlaceholderDrawable);
        }
    }


    @BindingAdapter({"image"})
    public static void setGridImage(ImageView imageview, String url) {
        Drawable mPlaceholderDrawable = imageview.getContext().getResources().getDrawable(R.mipmap.ic_launcher);/*ResourcesCompat.getDrawable(
                imageview.getContext().getResources(), R.drawable.user_default_vector, null);*/
        if (!StringHelper.isEmpty(url)) {
            //AppUtils.loadImage(imageview.getContext(), url, mPlaceholderDrawable, imageview);
            GlideUtil.loadImage(url, imageview, mPlaceholderDrawable, mPlaceholderDrawable, Constant.ImageScaleType.CENTER_CROP, null);
        } else {
            imageview.setImageDrawable(mPlaceholderDrawable);
        }
    }

    /* End with Activity name or Adapter name*/

    // set offer image
    @BindingAdapter({"offerUrl"})
    public static void setOfferImage(ImageView imageview, String url) {
        Drawable mPlaceholderDrawable = imageview.getContext().getResources().getDrawable(R.mipmap.ic_launcher);
        if (!StringHelper.isEmpty(url)) {
            GlideUtil.loadImage(url, imageview, mPlaceholderDrawable, mPlaceholderDrawable, Constant.ImageScaleType.CENTER_CROP, null);
        } else {
            imageview.setImageDrawable(mPlaceholderDrawable);
        }
    }

    @BindingAdapter({"color"})
    public static void setFont(RelativeLayout rout, DashboardModel item) {
        rout.setBackgroundColor(ContextCompat.getColor(rout.getContext(), item.getColorCode()));
    }
}

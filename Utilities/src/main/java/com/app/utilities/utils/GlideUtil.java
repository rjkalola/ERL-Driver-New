package com.app.utilities.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.utilities.GlideApp;
import com.app.utilities.callbacks.ImageLoadingListener;
import com.app.utilities.transformation.BlurTransformation;
import com.app.utilities.transformation.ColorFilterTransformation;
import com.app.utilities.transformation.GrayscaleTransformation;
import com.app.utilities.transformation.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * @AutherBy Dhaval Jivani
 */

public class GlideUtil {

    private static RequestOptions options;


    /**
     * TODO : If you don't want to scale image then scaleType = 0;
     *
     * @param url
     * @param imageView
     * @param placeHolder
     * @param errorDrawable
     * @param scaleType                    ( Constant.ImageScaleType.CENTER_CROP , Constant.ImageScaleType.FIT_CENTER )
     * @param imageLoadingListener<String, Bitmap>( If you don't want to get success and error of image loading then pass null )
     */

    public static void loadImage(String url, ImageView imageView, final Drawable placeHolder,
                                 Drawable errorDrawable, int scaleType, final ImageLoadingListener imageLoadingListener) {

        SetScaleType(scaleType);  // set scaleType

        GlideApp
                .with(imageView.getContext())
                .asBitmap()
                .load(url)
                .placeholder(placeHolder)
                .apply(options)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        if (imageLoadingListener != null) imageLoadingListener.onFailed(e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (imageLoadingListener != null)
                            imageLoadingListener.onLoaded(resource, null);
                        return false;
                    }
                })
                .error(errorDrawable)
                .into(imageView);

    }


    /**
     * @param scaleType ( Constant.ImageScaleType.CENTER_CROP , Constant.ImageScaleType.FIT_CENTER )
     */
    private static void SetScaleType(int scaleType) {
        options = new RequestOptions();
        if (scaleType != 0) {
            if (scaleType == Constant.ImageScaleType.CENTER_CROP) {
                options.centerCrop();
            } else if (scaleType == Constant.ImageScaleType.FIT_CENTER) {
                options.fitCenter();
            }
        }
    }

    /**
     * TODO : If you don't want to scale image then scaleType = 0;
     *
     * @param url
     * @param imageView
     * @param errorDrawable
     * @param progressBar
     * @param scaleType            ( Constant.ImageScaleType.CENTER_CROP , Constant.ImageScaleType.FIT_CENTER )
     * @param imageLoadingListener ( If you don't want to get success and error of image loading then pass null )
     */

    public static void loadImageWithProgressBar(String url, final ImageView imageView, Drawable errorDrawable,
                                                final ProgressBar progressBar
            , int scaleType, @Nullable final ImageLoadingListener imageLoadingListener) {

        SetScaleType(scaleType);

        GlideApp
                .with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(options)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        if (imageLoadingListener != null) imageLoadingListener.onFailed(e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (imageLoadingListener != null)
                            imageLoadingListener.onLoaded(resource, null);
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(errorDrawable)
                .into(imageView);

    }


    /**
     * TODO : If you don't want to scale image then scaleType = 0;
     *
     * @param imageView
     * @param fileName
     * @param placeHolder
     * @param errorDrawable
     * @param scaleType            ( Constant.ImageScaleType.CENTER_CROP , Constant.ImageScaleType.FIT_CENTER )
     * @param imageLoadingListener ( If you don't want to get success and error of image loading then pass null )
     */
    public static void loadImageFromFile(ImageView imageView, File fileName, Drawable placeHolder,
                                         Drawable errorDrawable, int scaleType, final ImageLoadingListener imageLoadingListener) {

        SetScaleType(scaleType);

        GlideApp
                .with(imageView.getContext())
                .asBitmap()
                .load(Uri.fromFile(fileName))
                .placeholder(placeHolder)
                .error(errorDrawable)
                .apply(options)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        if (imageLoadingListener != null) imageLoadingListener.onFailed(e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (imageLoadingListener != null)
                            imageLoadingListener.onLoaded(resource, null);
                        return false;
                    }
                })
                .error(errorDrawable)
                .into(imageView);

    }

    public static void loadRoundedImageFromFile(ImageView view, File fileName, String transformation, Drawable placeHolder, final Drawable errorDrawable, int scaleType, int corner, int margin, String color, int border, final ImageLoadingListener imageLoadingListener) {
        SetScaleType(scaleType);
        switch (transformation) {
            case Constant.TransformationType.CENTERCROP_TRANSFORM:
                GlideApp.with(view.getContext())
                        .load(Uri.fromFile(fileName))
                        .apply(RequestOptions.centerCropTransform()) // center crop image
                        .placeholder(placeHolder)
                        .error(errorDrawable)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onFailed(e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (imageLoadingListener != null)
                                    imageLoadingListener.onLoaded(null, errorDrawable);
                                return false;
                            }
                        })
                        .into(view);
                break;

            case Constant.TransformationType.CIRCLECROP_TRANSFORM:
                GlideApp.with(view.getContext())
                        .load(Uri.fromFile(fileName))
                        .apply(RequestOptions.circleCropTransform()) // circle crop image
                        .placeholder(placeHolder)
                        .error(errorDrawable)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onFailed(e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (imageLoadingListener != null)
                                    imageLoadingListener.onLoaded(null, errorDrawable);
                                return false;
                            }
                        })
                        .into(view);
                break;
            default:
                break;
        }
    }

    public static void loadGifImageFromFile(ImageView imageView, int file, Drawable placeHolder,
                                         Drawable errorDrawable, int scaleType, final ImageLoadingListener imageLoadingListener) {
        SetScaleType(scaleType);
        GlideApp
                .with(imageView.getContext())
                .asGif()
                .load(file)
                .placeholder(placeHolder)
                .error(errorDrawable)
                .apply(options)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .error(errorDrawable)
                .into(imageView);
    }

    public static void loadCircularImageFromFile(ImageView imageView, File file) {
        Glide.with(imageView.getContext())
                .load(file)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }


    /**
     * TODO : If you don't want to scale image then scaleType = 0;
     *
     * @param url
     * @param view
     * @param transformation       ( Constant.TransformationType.CenterCropTransform , Constant.TransformationType.CircleCropTransform , Constant.TransformationType.BlurTransformation , Constant.TransformationType.RoundedCornersTransformation , Constant.TransformationType.ColorFilterTansformation, Constant.TransformationType.GrayscaleTransformation )
     * @param placeHolder
     * @param errorDrawable
     * @param scaleType            ( Constant.ImageScaleType.CENTER_CROP , Constant.ImageScaleType.FIT_CENTER )
     * @param corner
     * @param margin
     * @param color
     * @param border
     * @param imageLoadingListener ( If you don't want to get success and error of image loading then pass null )
     */
    public static void loadImageUsingGlideTransformation(String url, ImageView view, String transformation, Drawable placeHolder, final Drawable errorDrawable, int scaleType, int corner, int margin, String color, int border, final ImageLoadingListener imageLoadingListener) {
        SetScaleType(scaleType);
        switch (transformation) {
            case Constant.TransformationType.CENTERCROP_TRANSFORM:
                GlideApp.with(view.getContext())
                        .asBitmap()
                        .load(url)
                        .apply(RequestOptions.centerCropTransform()) // center crop image
                        .placeholder(placeHolder)
                        .error(errorDrawable)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onFailed(e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                if (imageLoadingListener != null)
                                    imageLoadingListener.onLoaded(resource, null);
                                return false;
                            }
                        })
                        .into(view);
                break;

            case Constant.TransformationType.CIRCLECROP_TRANSFORM:
                GlideApp.with(view.getContext())
                        .load(url)
                        .apply(RequestOptions.circleCropTransform()) // circle crop image
                        .placeholder(placeHolder)
                        .error(errorDrawable)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onFailed(e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onLoaded(null , errorDrawable);
                                return false;
                            }
                        })
                        .into(view);
                break;

            case Constant.TransformationType.BLUR_TRANSFORMATION:
                GlideApp.with(view.getContext())
                        .load(url)
                        .transform(new BlurTransformation(view.getContext(), border)) // for blur image
                        .placeholder(placeHolder)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onFailed(e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onLoaded(null , errorDrawable);
                                return false;
                            }
                        })
                        .error(errorDrawable)

                        .into(view);
                break;

            case Constant.TransformationType.ROUNDED_CORNERS_TRANSFORMATION:
                GlideApp.with(view.getContext())
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(view.getContext(), corner, margin, color, border))) //for the round Corner Image
                        .placeholder(placeHolder)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onFailed(e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onLoaded(null , errorDrawable);
                                return false;
                            }
                        })
                        .error(errorDrawable)

                        .into(view);
                break;

            case Constant.TransformationType.COLOR_FILTER_TRANSFORMATION:
                GlideApp.with(view.getContext())
                        .load(url)
                        .transform(new ColorFilterTransformation(Color.parseColor(color))) // for color image
                        .placeholder(placeHolder)
                        .error(errorDrawable)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onFailed(e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onLoaded(null , errorDrawable);
                                return false;
                            }
                        })
                        .into(view);
                break;

            case Constant.TransformationType.GRAYSCALE_TRANSFORMATION:
                GlideApp.with(view.getContext())
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(new GrayscaleTransformation(view.getContext())))
                        .placeholder(placeHolder)
                        .error(errorDrawable)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onFailed(e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (imageLoadingListener != null) imageLoadingListener.onLoaded(null , errorDrawable);
                                return false;
                            }
                        })
                        .into(view);
                break;

            default:
                break;
        }
    }
}

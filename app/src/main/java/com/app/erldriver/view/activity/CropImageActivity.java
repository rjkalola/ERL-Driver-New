package com.app.erldriver.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.erldriver.R;
import com.app.erldriver.databinding.ActivityCropImageBinding;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.utilities.utils.FileUtils;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;

import java.io.File;


public class CropImageActivity extends BaseActivity implements View.OnClickListener {
    private ActivityCropImageBinding binding;
    private Context mContext;
    private Uri sourceUri;
    private int cropX, cropY;
    private String fileExtension;
    private static final String KEY_FRAME_RECT = "FrameRect";
    private Bitmap.CompressFormat mCompressFormat;
    private RectF mFrameRect = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        mContext = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crop_image);

        if (savedInstanceState != null) {
            mFrameRect = savedInstanceState.getParcelable(KEY_FRAME_RECT);
        }
        getIntentData();

        binding.imgRotateLeft.setOnClickListener(this);
        binding.imgRotateRight.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        binding.imgDone.setOnClickListener(this);

    }

    public void getIntentData() {
        if (getIntent().getExtras() != null) {
            sourceUri = Uri.parse(getIntent().getStringExtra(AppConstant.IntentKey.IMAGE_URI));
            cropX = getIntent().getIntExtra(AppConstant.IntentKey.CROP_RATIO_X, 0);
            cropY = getIntent().getIntExtra(AppConstant.IntentKey.CROP_RATIO_Y, 0);
            fileExtension = getIntent().getStringExtra(AppConstant.IntentKey.FILE_EXTENSION);
            if (fileExtension.equals(AppConstant.FileExtension.JPG))
                mCompressFormat = Bitmap.CompressFormat.JPEG;
            else if (fileExtension.equals(AppConstant.FileExtension.PNG))
                mCompressFormat = Bitmap.CompressFormat.PNG;
            else
                mCompressFormat = Bitmap.CompressFormat.JPEG;

            if (sourceUri != null) {
//                binding.cropImageView.load(sourceUri).useThumbnail(true)
//                        .executeAsCompletable();
//                binding.cropImageView.setCustomRatio(cropX, cropY);

                binding.cropImageView.load(sourceUri)
                        .initialFrameRect(mFrameRect)
                        .useThumbnail(true)
                        .execute(mLoadCallback);
                binding.cropImageView.setCustomRatio(cropX, cropY);


            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgRotateLeft:
                binding.cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                break;
            case R.id.imgRotateRight:
                binding.cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgDone:
                showProgress();
                binding.cropImageView.crop(sourceUri).execute(mCropCallback);
                break;
        }
    }

    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override
        public void onSuccess() {
        }

        @Override
        public void onError(Throwable e) {
        }
    };

    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {

            binding.cropImageView.save(cropped)
                    .compressFormat(mCompressFormat)
                    .execute(createSaveUri(cropped), mSaveCallback);
        }

        @Override
        public void onError(Throwable e) {
        }
    };

    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {
            hideProgress();
            Intent i = new Intent();
            i.putExtra(AppConstant.IntentKey.IMAGE_URI, outputUri.toString());
            setResult(1, i);
            finish();
        }

        @Override
        public void onError(Throwable e) {
            hideProgress();
        }
    };

    public Uri createSaveUri(Bitmap bitmap) {
        String newPath = AppUtils.getFilePathFromBitmap(mContext, bitmap, fileExtension);
        Uri uri = FileUtils.getUri(new File(newPath));
//        return createNewUri(mContext, mCompressFormat);
        return uri;
    }

}

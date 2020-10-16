package com.app.erldriver.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erldriver.R;
import com.app.erldriver.databinding.ActivityMyProfileBinding;
import com.app.erldriver.model.entity.response.ProfileResponse;
import com.app.erldriver.model.entity.response.User;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.ImagePickerUtility;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.UserAuthenticationViewModel;
import com.app.imagepicker.Model.FileWithPath;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.FileUtils;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ToastHelper;
import com.app.utilities.utils.ValidationUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MyProfileActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private ActivityMyProfileBinding binding;
    private Context mContext;
    private UserAuthenticationViewModel userAuthenticationViewModel;
    private ProfileResponse profileData;
    private ImagePickerUtility imagePickerUtility;
    private String[] EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);
        mContext = this;
        imagePickerUtility = new ImagePickerUtility(this);
        userAuthenticationViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(UserAuthenticationViewModel.class);
        userAuthenticationViewModel.createView(this);
        userAuthenticationViewModel.mProfileResponse()
                .observe(this, getProfileResponse());
        userAuthenticationViewModel.mSaveProfileResponse()
                .observe(this, saveProfileResponse());

        binding.imgBack.setOnClickListener(this);
        binding.txtSave.setOnClickListener(this);
        binding.routSelectImageView.setOnClickListener(this);

        userAuthenticationViewModel.getProfileRequest();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtSave:
                if (isValid()) {
                    userAuthenticationViewModel.saveProfile(getProfileData());
                }
                break;
            case R.id.routSelectImageView:
                checkPermission();
                break;
        }
    }

    public Observer getProfileResponse() {
        return (Observer<ProfileResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setProfileData(response);
                    binding.setInfo(getProfileData());
                    if (!StringHelper.isEmpty(getProfileData().getImage())) {
                        GlideUtil.loadImageUsingGlideTransformation(getProfileData().getImage(), binding.imgUserImage, Constant.TransformationType.CIRCLECROP_TRANSFORM, null, null, Constant.ImageScaleType.CENTER_CROP, 0, 0, "", 0, null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Observer saveProfileResponse() {
        return (Observer<ProfileResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    User user = AppUtils.getUserPrefrence(mContext);
                    user.setName(response.getName());
                    user.setEmail(response.getEmail());
                    user.setPhone(response.getPhone());
                    user.setImage(response.getImage());
                    AppUtils.setUserPrefrence(mContext, user);
                    setResult(1);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public boolean isValid() {
        boolean isValid = true;

        if (!ValidationUtil.isEmptyEditText(getProfileData().getPhone())) {
            binding.edtMobileNUmber.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtMobileNUmber, mContext.getString(R.string.error_empty_phone));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(getProfileData().getEmail())) {
            if (ValidationUtil.isValidEmail(binding.edtEmail.getText().toString())) {
                binding.edtEmail.setError(null);
            } else {
                ValidationUtil.setErrorIntoEditext(binding.edtEmail, mContext.getString(R.string.error_invalid_email));
                isValid = false;
            }
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtEmail, mContext.getString(R.string.error_empty_email));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(getProfileData().getName())) {
            binding.edtFullName.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtFullName, mContext.getString(R.string.error_empty_name));
            isValid = false;
        }

        return isValid;
    }

    public void showSelectImageFrom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.lbl_select_image_from));
        String[] items = {getString(R.string.lbl_camera), getString(R.string.lbl_gallery)};
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                        imagePickerUtility.openCamera(AppConstant.IntentKey.REQUEST_CAMERA_KITKAT);
                    } else {
                        imagePickerUtility.openCamera(AppConstant.IntentKey.REQUEST_CAMERA);
                    }
                    break;
                case 1:
                    imagePickerUtility.pickImage(AppConstant.IntentKey.REQUEST_GALLERY);
                    break;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri = null;
        switch (requestCode) {
            case AppConstant.IntentKey.REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    if (imageUri != null) {
                        startCrop(imageUri);
                    }
                }
                break;
            case AppConstant.IntentKey.REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    imageUri = Uri.parse(imagePickerUtility.getCurrentPhotoPath());
                    if (imageUri != null)
                        startCrop(imageUri);
                }
                break;

            case AppConstant.IntentKey.REQUEST_CAMERA_KITKAT:
                if (resultCode == RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imageUri = getImageUri(mContext, photo);
                    if (imageUri != null)
                        startCrop(imageUri);
                }
                break;
            case AppConstant.IntentKey.REQUEST_CROP_IMAGE:
                if (resultCode == 1) {
                    String[] imageExt = getResources().getStringArray(R.array.imageExtension);
                    String realPath = FileUtils.getPath(mContext, Uri.parse(data.getStringExtra(AppConstant.IntentKey.IMAGE_URI)));
                    if (!StringHelper.isEmpty(realPath)) {
                        if (!Arrays.asList(imageExt).contains(AppUtils.getFileExt(realPath).toLowerCase())) {
                            ToastHelper.error(mContext, getString(R.string.error_image_format), Toast.LENGTH_LONG, false);
                            return;
                        }
                    } else {
                        return;
                    }

                    if (!StringHelper.isEmpty(realPath)) {
//                        Bitmap bitmap = BitmapFactory.decodeFile(realPath);

                        binding.getInfo().setImage(realPath);
                        setLocalImage(realPath);
                    }
                }
                break;
            default:
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void setLocalImage(String imagePath) {
        if (!StringHelper.isEmpty(binding.getInfo().getImage())) {
            GlideUtil.loadRoundedImageFromFile(binding.imgUserImage, new File(imagePath), Constant.TransformationType.CIRCLECROP_TRANSFORM, null, null, Constant.ImageScaleType.CENTER_CROP, 0, 0, "", 0, null);
        }
    }

    public void startCrop(Uri uri) {
        try {
            String realPath = FileUtils.getPath(mContext, uri);
            File file = new File(realPath);
            FileWithPath fileWithPath = AppUtils.compressImage(realPath, file);
            if (fileWithPath != null && fileWithPath.getUri() != null) {
                imagePickerUtility.startCrop(fileWithPath.getUri(), 1, 1, AppConstant.FileExtension.JPG);
            } else {
                imagePickerUtility.startCrop(uri, 1, 1, AppConstant.FileExtension.JPG);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasPermission() {
        return EasyPermissions.hasPermissions(this, EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //    @AfterPermissionGranted(AppConstant.IntentKey.EXTERNAL_STORAGE_PERMISSION)
    public void checkPermission() {
        if (hasPermission()) {
            showSelectImageFrom();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.msg_storage_permission),
                    AppConstant.IntentKey.EXTERNAL_STORAGE_PERMISSION,
                    EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        showSelectImageFrom();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public ProfileResponse getProfileData() {
        return profileData;
    }

    public void setProfileData(ProfileResponse profileData) {
        this.profileData = profileData;
    }
}

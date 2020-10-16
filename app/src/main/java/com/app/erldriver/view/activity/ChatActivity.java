package com.app.erldriver.view.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erldriver.R;
import com.app.erldriver.adapter.ChatAdapter;
import com.app.erldriver.callback.SelectItemListener;
import com.app.erldriver.databinding.ActivityChatBinding;
import com.app.erldriver.model.entity.info.MessageInfo;
import com.app.erldriver.model.entity.response.GetMessagesResponse;
import com.app.erldriver.model.entity.response.SendMessageResponse;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.ImagePickerUtility;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.UserAuthenticationViewModel;
import com.app.imagepicker.Model.FileWithPath;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.FileUtils;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ToastHelper;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ChatActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, SelectItemListener {
    private ActivityChatBinding binding;
    private Context mContext;
    private ChatAdapter adapter;
    private UserAuthenticationViewModel userAuthenticationViewModel;
    private boolean isFromNotification = false;
    private ImagePickerUtility imagePickerUtility;
    private String[] EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        mContext = this;
        AppConstant.isOpenChatScreen = true;
        imagePickerUtility = new ImagePickerUtility(this);

        userAuthenticationViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(UserAuthenticationViewModel.class);
        userAuthenticationViewModel.createView(this);
        userAuthenticationViewModel.mSendMessageResponse()
                .observe(this, mSendMessageResponse());
        userAuthenticationViewModel.getMessagesResponse()
                .observe(this, mGetMessagesResponse());

//        binding.txtHome.setOnClickListener(this);
        getIntentData();

        loadChat(0, true);

        binding.imgSend.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);
        binding.imgSelectImage.setOnClickListener(this);
        binding.routPreview.routRootView.setOnClickListener(v -> binding.routPreview.routRootView.setVisibility(View.GONE));
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null) {
            if (getIntent().hasExtra(AppConstant.IntentKey.IS_FROM_NOTIFICATION))
                isFromNotification = getIntent().getExtras().getBoolean(AppConstant.IntentKey.IS_FROM_NOTIFICATION);
        }
    }

    public void loadChat(int lastMessageId, boolean isProgress) {
        userAuthenticationViewModel.getMessages(lastMessageId, isProgress);
    }

    public void sendMessage(String message, String imagePath, boolean isProgress) {
        userAuthenticationViewModel.sendMessage(message, imagePath, isProgress);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSend:
                String message = binding.edtMessageBox.getText().toString().trim();
                if (!StringHelper.isEmpty(message)) {
                    binding.edtMessageBox.setText("");
                    sendMessage(message, "", false);
                }
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgSelectImage:
                checkPermission();
                break;
        }
    }

    public void setAddressAdapter(List<MessageInfo> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewChat.setLayoutManager(linearLayoutManager);
        binding.recyclerViewChat.setHasFixedSize(true);
        adapter = new ChatAdapter(mContext, list, this);
        binding.recyclerViewChat.setAdapter(adapter);
        scrollToBottom();
    }

    public Observer mSendMessageResponse() {
        return (Observer<SendMessageResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    adapter.addMessage(response.getInfo());
                    scrollToBottom();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Observer mGetMessagesResponse() {
        return (Observer<GetMessagesResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    if (adapter == null) {
                        setAddressAdapter(response.getInfo());
                    } else {
                        adapter.addMessages(response.getInfo());
                        scrollToBottom();
                    }
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
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

    public void checkPermission() {
        if (hasPermission()) {
            selectImageFromGallery();
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
        selectImageFromGallery();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public void selectImageFromGallery() {
        imagePickerUtility.pickImage(AppConstant.IntentKey.REQUEST_GALLERY);
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
                        String[] imageExt = getResources().getStringArray(R.array.imageExtension);
                        String realPath = FileUtils.getPath(mContext, imageUri);
                        if (!StringHelper.isEmpty(realPath)) {
                            if (!Arrays.asList(imageExt).contains(AppUtils.getFileExt(realPath).toLowerCase())) {
                                ToastHelper.error(mContext, getString(R.string.error_image_format), Toast.LENGTH_LONG, false);
                                return;
                            }
                        } else {
                            return;
                        }

                        if (!StringHelper.isEmpty(realPath)) {
                            try {
                                FileWithPath fileWithPath = AppUtils.compressImage(realPath, new File(realPath));
                                if (fileWithPath != null && fileWithPath.getUri() != null) {
                                    sendMessage("", fileWithPath.getFile().getAbsolutePath(), false);
                                } else {
                                    sendMessage("", realPath, false);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerChatReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterChatReceiver();
        AppConstant.isOpenChatScreen = false;
    }

    public BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String action = intent.getStringExtra("action");
//            Log.e("test", "action:" + action);
            int lastMessageId = 0;
            if (adapter != null) {
                if (adapter.getMessageList().size() > 0)
                    lastMessageId = adapter.getMessageList().get(adapter.getMessageList().size() - 1).getId();
                loadChat(lastMessageId, false);
            }
        }
    };

    public void registerChatReceiver() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(AppConstant.Action.UPDATE_CHAT_DATA);
            registerReceiver(chatReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unRegisterChatReceiver() {
        try {
            unregisterReceiver(chatReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scrollToBottom() {
        if (adapter != null && adapter.getMessageList().size() > 0)
            binding.recyclerViewChat.smoothScrollToPosition(adapter.getMessageList().size() - 1);
    }

    @Override
    public void onBackPressed() {
        if (binding.routPreview.routRootView.getVisibility() == View.VISIBLE) {
            binding.routPreview.routRootView.setVisibility(View.GONE);
        } else {
            if (isFromNotification)
                moveActivity(mContext, DashBoardActivity.class, true, true, null);
            else
                finish();
        }
    }

    @Override
    public void onSelectItem(int position, int action) {
        if (action == AppConstant.Action.PREVIEW_IMAGE) {
            binding.routPreview.routRootView.setVisibility(View.VISIBLE);
            setImage(binding.routPreview.imgPreviewImage, adapter.getMessageList().get(position).getImage());
        }
    }

    private void setImage(ImageView imageView, String url) {
        if (!StringHelper.isEmpty(url)) {
            GlideUtil.loadImage(url, imageView, null, null, 0, null);
        }
    }
}

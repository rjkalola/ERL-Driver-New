package com.app.erldriver.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erldriver.R;
import com.app.erldriver.databinding.ActivityLoginBinding;
import com.app.erldriver.model.entity.response.UserResponse;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.UserAuthenticationViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.ToastHelper;
import com.app.utilities.utils.ValidationUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ActivityLoginBinding binding;
    private Context mContext;
    private UserAuthenticationViewModel userAuthenticationViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mContext = this;
        userAuthenticationViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(UserAuthenticationViewModel.class);
        userAuthenticationViewModel.createView(this);
        userAuthenticationViewModel.mUserResponse()
                .observe(this, getUserResponse());
        binding.setUserAuthenticationViewModel(userAuthenticationViewModel);

        binding.txtLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtRegisterHere:
                moveActivity(mContext, SignUpActivity.class, false, false, null);
                break;
            case R.id.txtForgotPassword:
                moveActivity(mContext, ForgotPasswordActivity.class, false, false, null);
                break;
            case R.id.txtLogin:
                if (isValid()) {
                    if (AppUtils.isNetworkConnected(mContext))
                        userAuthenticationViewModel.sendLoginRequest();
                    else
                        ToastHelper.error(mContext, getString(R.string.error_internet_connection), Toast.LENGTH_SHORT, false);
                }
                break;
        }
    }

    public Observer getUserResponse() {
        return (Observer<UserResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    AppUtils.setUserPrefrence(mContext, response.getInfo());
                    moveActivity(mContext, DashBoardActivity.class, true, true, null);
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public boolean isValid() {
        boolean isValid = true;

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getLoginRequest().getPassword())) {
            binding.edtPassword.setError(null);
        } else {
            ValidationUtil.setErrorIntoEditext(binding.edtPassword, mContext.getString(R.string.error_empty_password));
            isValid = false;
        }

        if (!ValidationUtil.isEmptyEditText(userAuthenticationViewModel.getLoginRequest().getEmail())) {
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
        return isValid;
    }
}

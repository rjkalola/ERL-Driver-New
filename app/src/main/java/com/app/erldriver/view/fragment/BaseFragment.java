package com.app.erldriver.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.erldriver.callback.OnFragmentBackListener;
import com.app.erldriver.callback.BackPressImpl;
import com.app.erldriver.callback.BaseCallBack;
import com.app.erldriver.callback.ViewListener;
import com.app.erldriver.network.RetrofitException;
import com.app.erldriver.util.AppUtils;
import com.app.utilities.utils.ProgressDialogHelper;
import com.app.utilities.utils.StringHelper;


public class BaseFragment extends Fragment implements OnFragmentBackListener, ViewListener {

    public BaseCallBack baseCallBack;
    private boolean isHidden = true;
    private ProgressDialogHelper progressDialogHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseCallBack) {
            baseCallBack = (BaseCallBack) context;
        }

    }

    @Override
    public boolean onBackPressed() {
        return new BackPressImpl(this).onBackPressed();
    }

    public BaseCallBack getBaseCallBack() {
        return baseCallBack;
    }

    public void moveActivity(Context context, Class<?> c, boolean finish, boolean clearStack, Bundle bundle) {
        Intent intent = new Intent(context, c);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
        if (finish) {
            ((Activity) context).finish();
        }
    }

    public void moveActivity(Context context, Intent intent, boolean finish, boolean clearStack, Bundle bundle) {
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        if (finish) {
            ((Activity) context).finish();
        }

        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);

    }

    public void showProgressDialog(Context context, String message) {
        if (progressDialogHelper == null) {
            progressDialogHelper = new ProgressDialogHelper(context);
        }
        if (StringHelper.isEmpty(message)) {
            progressDialogHelper.showCircularProgressDialog();
        } else {
            progressDialogHelper.showProgressDialog(message);
        }

    }

    public void hideProgressDialog() {
        if (progressDialogHelper != null) {
            progressDialogHelper.hideProgressDialog();
            progressDialogHelper.hideCircularProgressDialog();
        }
    }

    @Override
    public void showProgress() {
        showProgressDialog(getActivity(), "");
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showApiError(RetrofitException retrofitException, String errorCode) {
        AppUtils.handleApiError(getActivity(), retrofitException);
    }
}

package com.app.erldriver.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.erldriver.ERLApp;
import com.app.erldriver.R;
import com.app.erldriver.databinding.ActivityDashboardBinding;
import com.app.erldriver.model.entity.response.BaseResponse;
import com.app.erldriver.model.entity.response.DashBoardResponse;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.DashBoardViewModel;
import com.app.erldriver.viewModel.UserAuthenticationViewModel;
import com.app.utilities.callbacks.DialogButtonClickListener;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;
import com.app.utilities.utils.ToastHelper;
import com.google.firebase.iid.FirebaseInstanceId;

public class DashBoardActivity extends BaseActivity implements View.OnClickListener, DialogButtonClickListener {
    private ActivityDashboardBinding binding;
    private DashBoardViewModel dashBoardViewModel;
    private UserAuthenticationViewModel userAuthenticationViewModel;
    private Context mContext;
    private DashBoardResponse dashBoardResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
//        bindingNavHeader = DataBindingUtil.bind(binding.navView.getHeaderView(0));
        mContext = this;
        dashBoardViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(DashBoardViewModel.class);
        dashBoardViewModel.createView(this);
        userAuthenticationViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(UserAuthenticationViewModel.class);
        userAuthenticationViewModel.createView(this);
        dashBoardViewModel.dashBoardResponse()
                .observe(this, dashBoardResponse());
        userAuthenticationViewModel.mBaseResponse()
                .observe(this, registerFcmResponse());
        setupToolbar(getString(R.string.lbl_dashboard), false);

        binding.appBarLayout.txtPickUps.setOnClickListener(this);
        binding.appBarLayout.txtDrops.setOnClickListener(this);

        dashBoardViewModel.getDashboardRequest();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.txtPickUps:
                if (getDashBoardResponse() != null && getDashBoardResponse().getPickupCount() > 0) {
                    bundle.putInt(AppConstant.IntentKey.ORDER_TYPE, AppConstant.Type.ORDER_PICKUPS);
                    moveActivityForResult(mContext, MyOrderListActivity.class, false, false, AppConstant.IntentKey.VIEW_ORDER, bundle);
                } else {
                    ToastHelper.error(mContext, getString(R.string.lbl_no_pickup_order_found), Toast.LENGTH_SHORT, false);
                }
                break;
            case R.id.txtDrops:
                if (getDashBoardResponse() != null && getDashBoardResponse().getPickupCount() > 0) {
                    bundle.putInt(AppConstant.IntentKey.ORDER_TYPE, AppConstant.Type.ORDER_DROPS);
                    moveActivityForResult(mContext, MyOrderListActivity.class, false, false, AppConstant.IntentKey.VIEW_ORDER, bundle);
                } else {
                    ToastHelper.error(mContext, getString(R.string.lbl_no_deliver_order_found), Toast.LENGTH_SHORT, false);
                }

                break;
        }
    }

   /* public void setUserDetails() {
        User user = AppUtils.getUserPrefrence(mContext);
        if (user != null) {
            binding.txtUserName.setText(user.getName());
            if (!StringHelper.isEmpty(user.getImage())) {
                GlideUtil.loadImageUsingGlideTransformation(user.getImage(), binding.imgUser, Constant.TransformationType.CIRCLECROP_TRANSFORM, null, null, Constant.ImageScaleType.CENTER_CROP, 0, 0, "", 0, null);
            }
        }

    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstant.IntentKey.VIEW_ORDER:
//                if (resultCode == 1)
//                    setUserDetails();
                break;
            default:
                break;
        }
    }

    public Observer dashBoardResponse() {
        return (Observer<DashBoardResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setDashBoardResponse(response);
                    if (response.getPickupCount() > 0)
                        binding.appBarLayout.txtPickUps.setText(String.format(getString(R.string.lbl_display_pick_ups), response.getPickupCount()));
                    else
                        binding.appBarLayout.txtPickUps.setText(getString(R.string.lbl_pick_ups));

                    if (response.getDropCount() > 0)
                        binding.appBarLayout.txtDrops.setText(String.format(getString(R.string.lbl_display_drops), response.getDropCount()));
                    else
                        binding.appBarLayout.txtDrops.setText(getString(R.string.lbl_drops));

                    getFcmToken();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
            }
        };
    }

    public Observer registerFcmResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {

                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
            }
        };
    }

    public void getFcmToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
//                    User user = AppUtils.getUserPrefrence(mContext);
                    String fcmToken = task.getResult().getToken();
                    if (!StringHelper.isEmpty(fcmToken)) {
                        userAuthenticationViewModel.registerFcmRequest(fcmToken);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionLogout:
                AlertDialogHelper.showDialog(mContext, "", getString(R.string.logout_msg), getString(R.string.yes), getString(R.string.no), false, this, AppConstant.DialogIdentifier.LOGOUT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier) {
        if (dialogIdentifier == AppConstant.DialogIdentifier.LOGOUT) {
            ERLApp.get().clearData();
            moveActivity(mContext, LoginActivity.class, true, true, null);
        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {

    }


    public DashBoardResponse getDashBoardResponse() {
        return dashBoardResponse;
    }

    public void setDashBoardResponse(DashBoardResponse dashBoardResponse) {
        this.dashBoardResponse = dashBoardResponse;
    }
}

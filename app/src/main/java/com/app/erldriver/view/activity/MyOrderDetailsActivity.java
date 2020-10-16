package com.app.erldriver.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erldriver.R;
import com.app.erldriver.adapter.MyOrderServiceItemsListAdapter;
import com.app.erldriver.callback.SubmitOrderListener;
import com.app.erldriver.databinding.ActivityMyOrderDetailsBinding;
import com.app.erldriver.model.entity.response.BaseResponse;
import com.app.erldriver.model.entity.response.OrderDetailsResponse;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.view.dialog.SubmitOrderDialog;
import com.app.erldriver.viewModel.ManageOrderViewModel;
import com.app.utilities.utils.AlertDialogHelper;
import com.app.utilities.utils.StringHelper;

public class MyOrderDetailsActivity extends BaseActivity implements View.OnClickListener, SubmitOrderListener {
    private ActivityMyOrderDetailsBinding binding;
    private Context mContext;
    private MyOrderServiceItemsListAdapter adapter;
    private ManageOrderViewModel manageOrderViewModel;
    private OrderDetailsResponse orderDetails;
    private int orderId, orderType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_order_details);
        mContext = this;

        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
        manageOrderViewModel.createView(this);
        manageOrderViewModel.mOrderDetailsResponse()
                .observe(this, orderDetailsResponse());
        manageOrderViewModel.mBaseResponse()
                .observe(this, submitOrderResponse());

        binding.imgBack.setOnClickListener(this);
        binding.txtConfirmOrder.setOnClickListener(this);
        binding.routTrack.setOnClickListener(this);

//        manageOrderViewModel.getClientOrders(10, 0, true);
        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null && getIntent().hasExtra(AppConstant.IntentKey.ORDER_ID)) {
            orderId = getIntent().getIntExtra(AppConstant.IntentKey.ORDER_ID, 0);
            orderType = getIntent().getIntExtra(AppConstant.IntentKey.ORDER_TYPE, 0);
            manageOrderViewModel.clientOrderDetailsRequest(orderId);

            switch (orderType) {
                case AppConstant.Type.ORDER_PICKUPS:
                    binding.txtTitle.setText(getString(R.string.lbl_pick_up_details));
                    binding.txtOrderNumberTitle.setText(getString(R.string.lbl_pick_up_order_number));
                    binding.txtConfirmOrder.setText(getString(R.string.lbl_confirm_pickup));
                    break;
                case AppConstant.Type.ORDER_DROPS:
                    binding.txtTitle.setText(getString(R.string.lbl_drop_details));
                    binding.txtOrderNumberTitle.setText(getString(R.string.lbl_drop_order_number));
                    binding.txtConfirmOrder.setText(getString(R.string.lbl_drop));
                    break;
            }

        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtConfirmOrder:
                submitOrderDialog();
                break;
            case R.id.routTrack:
                String geoUri = "http://maps.google.com/maps?q=loc:" + getOrderDetails().getLatitude() + "," + getOrderDetails().getLongitude() + " (" + getOrderDetails().getName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                mContext.startActivity(intent);
                break;
        }
    }

    private void setAddressAdapter() {
        if (getOrderDetails() != null
                && getOrderDetails().getInfo() != null
                && getOrderDetails().getInfo().size() > 0) {
            binding.routOrderSummery.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.rvOrderItems.setLayoutManager(linearLayoutManager);
            binding.rvOrderItems.setHasFixedSize(true);
            adapter = new MyOrderServiceItemsListAdapter(mContext, getOrderDetails().getInfo());
            binding.rvOrderItems.setAdapter(adapter);
        } else {
            binding.routOrderSummery.setVisibility(View.GONE);
        }
    }

    public Observer submitOrderResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    moveActivity(mContext, OrderCompletedActivity.class, true, true, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Observer orderDetailsResponse() {
        return (Observer<OrderDetailsResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setOrderDetails(response);
                    binding.routMainView.setVisibility(View.VISIBLE);
                    binding.routBottomView.setVisibility(View.VISIBLE);

                    binding.setInfo(response);

                    binding.txtTotalPaces.setText(String.valueOf(getOrderDetails().getTotal_paces()));
                    binding.txtTotalPrice.setText(String.format(mContext.getString(R.string.lbl_display_price), String.valueOf(getOrderDetails().getTotal_price())));

                    if (!StringHelper.isEmpty(getOrderDetails().getDelivery_note()))
                        binding.routDeliveryNote.setVisibility(View.VISIBLE);
                    else
                        binding.routDeliveryNote.setVisibility(View.GONE);

                    if (!StringHelper.isEmpty(response.getLatitude())
                            && !StringHelper.isEmpty(response.getLongitude())) {
                        binding.routTrack.setVisibility(View.VISIBLE);
                    } else {
                        binding.routTrack.setVisibility(View.GONE);
                    }

                    String address = getOrderDetails().getAddress() + " ," + getOrderDetails().getStreet() + " ," + getOrderDetails().getLandmark() + " ," + getOrderDetails().getArea_name() + " ," + getOrderDetails().getCity_name();
                    binding.txtAddress.setText(address);

                    setAddressAdapter();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public void submitOrderDialog() {
        FragmentManager fm = ((BaseActivity) mContext).getSupportFragmentManager();
        SubmitOrderDialog submitOrderDialog = SubmitOrderDialog.newInstance(mContext, orderType, this);
        submitOrderDialog.setCancelable(false);
        submitOrderDialog.show(fm, "submitOrderDialog");
    }

    @Override
    public void onSubmitOrder(String note, int orderType) {
        switch (orderType) {
            case AppConstant.Type.ORDER_PICKUPS:
                manageOrderViewModel.pickedUpOrderRequest(orderId, note);
                break;
            case AppConstant.Type.ORDER_DROPS:
                manageOrderViewModel.deliveredOrderRequest(orderId, note);
                break;
        }
    }

    public OrderDetailsResponse getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetailsResponse orderDetails) {
        this.orderDetails = orderDetails;
    }
}

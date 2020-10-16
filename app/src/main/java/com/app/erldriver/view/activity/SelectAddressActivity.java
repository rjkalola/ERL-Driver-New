package com.app.erldriver.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.erldriver.R;
import com.app.erldriver.adapter.AddressListAdapter;
import com.app.erldriver.callback.SelectItemListener;
import com.app.erldriver.databinding.ActivitySelectAddressBinding;
import com.app.erldriver.model.entity.response.AddressListResponse;
import com.app.erldriver.model.entity.response.BaseResponse;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.ManageAddressViewModel;
import com.app.utilities.callbacks.DialogButtonClickListener;
import com.app.utilities.utils.AlertDialogHelper;

import org.parceler.Parcels;

public class SelectAddressActivity extends BaseActivity implements View.OnClickListener, DialogButtonClickListener
        , SelectItemListener {
    private ActivitySelectAddressBinding binding;
    private Context mContext;
    private AddressListResponse addressData;
    private AddressListAdapter adapter;
    private ManageAddressViewModel manageAddressViewModel;
    private boolean isUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_address);
        mContext = this;

        manageAddressViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageAddressViewModel.class);
        manageAddressViewModel.createView(this);
        manageAddressViewModel.getAddressListResponse()
                .observe(this, getAddressListResponse());
        manageAddressViewModel.mBaseResponse()
                .observe(this, deleteAddressResponse());
        manageAddressViewModel.chaneDefaultAddressResponse()
                .observe(this, changeDefaultAddressResponse());

        binding.imgBack.setOnClickListener(this);
        binding.txtCreateNewAddress.setOnClickListener(this);

        manageAddressViewModel.getAddressesRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtCreateNewAddress:
                moveActivityForResult(mContext, AddAddressActivity.class, false, false, AppConstant.IntentKey.ADD_NEW_ADDRESS, null);
                break;
        }
    }

    private void setAddressAdapter() {
        if (getAddressData() != null
                && getAddressData().getInfo() != null
                && getAddressData().getInfo().size() > 0) {
            binding.routDetailsView.setVisibility(View.VISIBLE);
            binding.btnCreateNewAddress.setVisibility(View.VISIBLE);
            binding.routEmptyView.setVisibility(View.GONE);

            binding.rvAddressList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            binding.rvAddressList.setHasFixedSize(true);
            adapter = new AddressListAdapter(mContext, getAddressData().getInfo(), this);
            binding.rvAddressList.setAdapter(adapter);
        } else {
            binding.routDetailsView.setVisibility(View.GONE);
            binding.btnCreateNewAddress.setVisibility(View.VISIBLE);
            binding.routEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public Observer getAddressListResponse() {
        return (Observer<AddressListResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setAddressData(response);
                    setAddressAdapter();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Observer deleteAddressResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    isUpdate = true;
                    manageAddressViewModel.getAddressesRequest();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Observer changeDefaultAddressResponse() {
        return (Observer<BaseResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setResult(1);
                    finish();
                } else {
                    AppUtils.handleUnauthorized(mContext, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void onSelectItem(int position, int action) {
        switch (action) {
            case AppConstant.Action.SELECT_ADDRESS:
                if (getAddressData().getInfo().get(position).getIs_default() == 1) {
                    setResult(1);
                    finish();
                } else {
                    manageAddressViewModel.changeDefaultAddressRequest(getAddressData().getInfo().get(position).getId());
                }
                break;
            case AppConstant.Action.EDIT_ADDRESS:
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstant.IntentKey.ADDRESS_DATA, Parcels.wrap(getAddressData().getInfo().get(position)));
                moveActivityForResult(mContext, AddAddressActivity.class, false, false, AppConstant.IntentKey.ADD_NEW_ADDRESS, bundle);
                break;
            case AppConstant.Action.DELETE_ADDRESS:
                AlertDialogHelper.showDialog(mContext, null, getString(R.string.msg_delete_address), getString(R.string.yes), getString(R.string.no), true, this, AppConstant.DialogIdentifier.DELETE_ADDRESS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstant.IntentKey.ADD_NEW_ADDRESS:
                if (resultCode == 1) {
                    isUpdate = true;
                    manageAddressViewModel.getAddressesRequest();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isUpdate)
            setResult(1);
        finish();
    }

    public AddressListResponse getAddressData() {
        return addressData;
    }

    public void setAddressData(AddressListResponse addressData) {
        this.addressData = addressData;
    }

    @Override
    public void onPositiveButtonClicked(int dialogIdentifier) {
        if (dialogIdentifier == AppConstant.DialogIdentifier.DELETE_ADDRESS) {
            manageAddressViewModel.deleteAddressRequest(getAddressData().getInfo().get(adapter.getPosition()).getId());
        }
    }

    @Override
    public void onNegativeButtonClicked(int dialogIdentifier) {

    }
}

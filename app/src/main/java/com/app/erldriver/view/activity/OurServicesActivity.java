package com.app.erldriver.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.erldriver.R;
import com.app.erldriver.adapter.ServiceItemsInfoListAdapter;
import com.app.erldriver.callback.SelectItemListener;
import com.app.erldriver.databinding.ActivityOurServicesBinding;
import com.app.erldriver.model.entity.info.PrivacyPolicyInfo;
import com.app.erldriver.model.entity.response.OurServicesResponse;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.DashBoardViewModel;
import com.app.utilities.utils.AlertDialogHelper;

import org.parceler.Parcels;

import java.util.List;

public class OurServicesActivity extends BaseActivity implements View.OnClickListener, SelectItemListener {
    private ActivityOurServicesBinding binding;
    private Context mContext;
    private int action;
    private DashBoardViewModel dashBoardViewModel;
    private ServiceItemsInfoListAdapter adapter;
    private OurServicesResponse ourServicesData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_our_services);
        mContext = this;

        dashBoardViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(DashBoardViewModel.class);
        dashBoardViewModel.createView(this);
        dashBoardViewModel.ourServicesResponse()
                .observe(this, ourServicesResponse());

        binding.imgBack.setOnClickListener(this);

        dashBoardViewModel.getOurServicesRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    private void setAddressAdapter(List<PrivacyPolicyInfo> list) {
        if (!list.isEmpty()) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            binding.rvOurServices.setLayoutManager(gridLayoutManager);
            binding.rvOurServices.setHasFixedSize(true);
            adapter = new ServiceItemsInfoListAdapter(mContext, list, this);
            binding.rvOurServices.setAdapter(adapter);
        }
    }

    public Observer ourServicesResponse() {
        return (Observer<OurServicesResponse>) response -> {
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setOurServicesData(response);
                    setAddressAdapter(response.getInfo());
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
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstant.IntentKey.OUR_SERVICE_INFO, Parcels.wrap(getOurServicesData().getInfo().get(position)));
        moveActivity(mContext,OurServiceDetailsActivity.class,false,false,bundle);
    }

    public OurServicesResponse getOurServicesData() {
        return ourServicesData;
    }

    public void setOurServicesData(OurServicesResponse ourServicesData) {
        this.ourServicesData = ourServicesData;
    }
}

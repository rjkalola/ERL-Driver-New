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
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.adapter.MyOrderListAdapter;
import com.app.erldriver.callback.SelectItemListener;
import com.app.erldriver.databinding.ActivityMyOrderListBinding;
import com.app.erldriver.model.entity.response.OrderListResponse;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.LoginViewModelFactory;
import com.app.erldriver.util.ResourceProvider;
import com.app.erldriver.viewModel.ManageOrderViewModel;
import com.app.utilities.utils.AlertDialogHelper;

public class MyOrderListActivity extends BaseActivity implements View.OnClickListener
        , SelectItemListener {
    private ActivityMyOrderListBinding binding;
    private Context mContext;
    private OrderListResponse ordersData;
    private MyOrderListAdapter adapter;
    private ManageOrderViewModel manageOrderViewModel;
    private int pastVisibleItems, visibleItemCount, totalItemCount, offset = 0, orderType;
    private boolean loading = true, mIsLastPage = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_order_list);
        mContext = this;

        manageOrderViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(new ResourceProvider(getResources()))).get(ManageOrderViewModel.class);
        manageOrderViewModel.createView(this);
        manageOrderViewModel.orderListResponse()
                .observe(this, getOrderListResponse());

        binding.imgBack.setOnClickListener(this);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            offset = 0;
            loadData(false);
        });

        getIntentData();
    }

    public void getIntentData() {
        if (getIntent().getExtras() != null) {
            orderType = getIntent().getIntExtra(AppConstant.IntentKey.ORDER_TYPE, 0);
            loadData(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    public void loadData(boolean showProgress) {
        switch (orderType) {
            case AppConstant.Type.ORDER_PICKUPS:
                binding.txtTitle.setText(getString(R.string.lbl_pick_up_order));
                manageOrderViewModel.getOrders(10, offset, showProgress, AppConstant.Type.ORDER_PICKUPS);
                break;
            case AppConstant.Type.ORDER_DROPS:
                binding.txtTitle.setText(getString(R.string.lbl_drop_order));
                manageOrderViewModel.getOrders(10, offset, showProgress, AppConstant.Type.ORDER_DROPS);
                break;
        }
    }

    private void setAddressAdapter() {
        if (getOrdersData() != null
                && getOrdersData().getInfo() != null
                && getOrdersData().getInfo().size() > 0) {
            binding.routDetailsView.setVisibility(View.VISIBLE);
            binding.routEmptyView.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.rvOrdersList.setLayoutManager(linearLayoutManager);
            binding.rvOrdersList.setHasFixedSize(true);
            adapter = new MyOrderListAdapter(mContext, getOrdersData().getInfo(), this);
            binding.rvOrdersList.setAdapter(adapter);
            recyclerViewScrollListener(linearLayoutManager);
        } else {
            binding.routDetailsView.setVisibility(View.GONE);
            binding.routEmptyView.setVisibility(View.VISIBLE);
        }
    }

    private void recyclerViewScrollListener(LinearLayoutManager layoutManager) {
        binding.rvOrdersList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            if (!mIsLastPage) {
                                loading = false;
                                binding.loadMore.setVisibility(View.VISIBLE);
                                loadData(false);
                            }
                        }
                    }
                }
            }
        });
    }

    public Observer getOrderListResponse() {
        return (Observer<OrderListResponse>) response -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            binding.loadMore.setVisibility(View.GONE);
            try {
                if (response == null) {
                    AlertDialogHelper.showDialog(mContext, null,
                            mContext.getString(R.string.error_unknown), mContext.getString(R.string.ok),
                            null, false, null, 0);
                    return;
                }
                if (response.isSuccess()) {
                    setOrdersData(response);
                    if (offset == 0) {
                        setAddressAdapter();
                    } else if (response.getInfo() != null && !response.getInfo().isEmpty()) {
                        if (adapter != null) {
                            adapter.addData(response.getInfo());
                            binding.loadMore.setVisibility(View.GONE);
                            loading = true;
                        }
                    } else if (response.getOffset() == 0) {
                        binding.loadMore.setVisibility(View.GONE);
                        loading = true;
                    }
                    offset = response.getOffset();

                    if (offset == 0)
                        mIsLastPage = true;
                    else
                        mIsLastPage = false;
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
            case AppConstant.Action.VIEW_ORDER:
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstant.IntentKey.ORDER_ID, adapter.getList().get(adapter.getPosition()).getId());
                bundle.putInt(AppConstant.IntentKey.ORDER_TYPE, orderType);
                moveActivityForResult(mContext, MyOrderDetailsActivity.class, false, false, AppConstant.IntentKey.VIEW_ORDER, bundle);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConstant.IntentKey.VIEW_ORDER:
                if (resultCode == 1) {
                    offset = 0;
                    loadData(true);
                }
                break;
            default:
                break;
        }
    }

    public OrderListResponse getOrdersData() {
        return ordersData;
    }

    public void setOrdersData(OrderListResponse ordersData) {
        this.ordersData = ordersData;
    }
}

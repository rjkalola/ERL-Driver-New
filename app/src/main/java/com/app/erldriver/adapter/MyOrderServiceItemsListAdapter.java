package com.app.erldriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.databinding.RowMyOrderServiceItemsListBinding;
import com.app.erldriver.model.entity.info.OrderItemInfo;
import com.app.erldriver.model.entity.info.ServiceItemInfo;

import java.util.List;

public class MyOrderServiceItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<OrderItemInfo> list;

    public MyOrderServiceItemsListAdapter(Context context, List<OrderItemInfo> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_order_service_items_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        OrderItemInfo info = list.get(position);
        setAddressAdapter(itemViewHolder.binding.rvServiceList, info.getData());
        itemViewHolder.getData(info);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowMyOrderServiceItemsListBinding binding;

        public void getData(OrderItemInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    private void setAddressAdapter(RecyclerView recyclerView, List<ServiceItemInfo> data) {
        if (data != null
                && data.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            MyOrderServiceSubItemsListAdapter adapter = new MyOrderServiceSubItemsListAdapter(mContext, data);
            recyclerView.setAdapter(adapter);
        }
    }
}

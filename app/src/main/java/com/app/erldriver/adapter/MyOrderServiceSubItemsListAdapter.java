package com.app.erldriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.databinding.RowMyOrderServiceItemsListBinding;
import com.app.erldriver.databinding.RowMyOrderServiceSubItemsListBinding;
import com.app.erldriver.model.entity.info.ServiceItemInfo;

import java.util.List;

public class MyOrderServiceSubItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ServiceItemInfo> list;

    public MyOrderServiceSubItemsListAdapter(Context context, List<ServiceItemInfo> list) {
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_order_service_sub_items_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ServiceItemInfo info = list.get(position);
        itemViewHolder.getData(info);
        itemViewHolder.binding.txtPrice.setText(String.format(mContext.getString(R.string.lbl_display_price), String.valueOf(info.getPrice())));
        itemViewHolder.binding.txtQty.setText(String.valueOf(info.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowMyOrderServiceSubItemsListBinding binding;

        public void getData(ServiceItemInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}

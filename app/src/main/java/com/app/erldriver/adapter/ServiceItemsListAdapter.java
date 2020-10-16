package com.app.erldriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.callback.SelectItemListener;
import com.app.erldriver.databinding.RowServiceItemsListBinding;
import com.app.erldriver.model.entity.info.ItemInfo;
import com.app.erldriver.util.AppConstant;

import java.util.List;

public class ServiceItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ItemInfo> list;
    private SelectItemListener listener;
    private int position;

    public ServiceItemsListAdapter(Context context, List<ItemInfo> list, SelectItemListener listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_service_items_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ItemInfo info = list.get(position);
        itemViewHolder.getData(info);

        itemViewHolder.binding.routMainView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSelectItem(position, AppConstant.Action.SELECT_SERVICE_HOUR_TYPE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowServiceItemsListBinding binding;

        public void getData(ItemInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

//    public void setSelectedItemCont(ServiceItemInfo info, RowServiceItemsListBinding binding) {
//        if (info.getQuantity() > 0) {
//            binding.txtAdd.setVisibility(View.INVISIBLE);
//            binding.routAddRemoveView.setVisibility(View.VISIBLE);
//        } else {
//            binding.txtAdd.setVisibility(View.VISIBLE);
//            binding.routAddRemoveView.setVisibility(View.INVISIBLE);
//        }
//        binding.txtQuantity.setText(String.valueOf(info.getQuantity()));
//    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<ItemInfo> getList() {
        return list;
    }

    public void setList(List<ItemInfo> list) {
        this.list = list;
    }
}

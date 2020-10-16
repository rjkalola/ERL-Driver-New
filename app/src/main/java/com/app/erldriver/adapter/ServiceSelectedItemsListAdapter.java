package com.app.erldriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.callback.SelectedServiceItemListener;
import com.app.erldriver.databinding.RowServiceItemsListBinding;
import com.app.erldriver.databinding.RowServiceSelectedItemsListBinding;
import com.app.erldriver.model.entity.info.ServiceItemInfo;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

import java.util.List;

public class ServiceSelectedItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ServiceItemInfo> list;
    private SelectedServiceItemListener listener;
    private int rootPosition;

    public ServiceSelectedItemsListAdapter(Context context, List<ServiceItemInfo> list, int rootPosition, SelectedServiceItemListener listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
        this.rootPosition = rootPosition;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_service_selected_items_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ServiceItemInfo info = list.get(position);
        itemViewHolder.getData(info);

        if (!StringHelper.isEmpty(info.getImage()))
            GlideUtil.loadImage(info.getImage(), itemViewHolder.binding.imgService, null, null, 0, null);

        itemViewHolder.binding.txtPrice.setText(String.format(mContext.getString(R.string.lbl_display_price), String.valueOf(info.getPrice())));

        setSelectedItemCont(info, itemViewHolder.binding);

        itemViewHolder.binding.txtAdd.setOnClickListener(v -> {
            if (listener != null) {
                info.setQuantity(info.getQuantity() + 1);
                setSelectedItemCont(info, itemViewHolder.binding);
                listener.onSelectServiceItem(rootPosition, position, info.getQuantity());
            }
        });

        itemViewHolder.binding.imgAdd.setOnClickListener(v -> {
            if (listener != null) {
                info.setQuantity(info.getQuantity() + 1);
                setSelectedItemCont(info, itemViewHolder.binding);
                listener.onSelectServiceItem(rootPosition, position, info.getQuantity());
            }
        });

        itemViewHolder.binding.imgRemove.setOnClickListener(v -> {
            if (listener != null && info.getQuantity() > 0) {
                info.setQuantity(info.getQuantity() - 1);
                setSelectedItemCont(info, itemViewHolder.binding);
                listener.onSelectServiceItem(rootPosition, position, info.getQuantity());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowServiceSelectedItemsListBinding binding;

        public void getData(ServiceItemInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void setSelectedItemCont(ServiceItemInfo info, RowServiceSelectedItemsListBinding binding) {
        if (info.getQuantity() > 0) {
            binding.txtAdd.setVisibility(View.INVISIBLE);
            binding.routAddRemoveView.setVisibility(View.VISIBLE);
        } else {
            binding.txtAdd.setVisibility(View.VISIBLE);
            binding.routAddRemoveView.setVisibility(View.INVISIBLE);
        }
        binding.txtQuantity.setText(String.valueOf(info.getQuantity()));
    }
}

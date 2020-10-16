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
import com.app.erldriver.databinding.RowServiceItemsInfoListBinding;
import com.app.erldriver.model.entity.info.PrivacyPolicyInfo;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

import java.util.List;

public class ServiceItemsInfoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PrivacyPolicyInfo> list;
    private SelectItemListener listener;

    public ServiceItemsInfoListAdapter(Context context, List<PrivacyPolicyInfo> list, SelectItemListener listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_service_items_info_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        PrivacyPolicyInfo info = list.get(position);
        itemViewHolder.getData(info);

        if (!StringHelper.isEmpty(info.getImage()))
            GlideUtil.loadImage(info.getImage(), itemViewHolder.binding.img, null, null, Constant.ImageScaleType.CENTER_CROP, null);

        itemViewHolder.binding.routMainView.setOnClickListener(v -> {
            if (listener != null)
                listener.onSelectItem(position,0);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowServiceItemsInfoListBinding binding;

        public void getData(PrivacyPolicyInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}

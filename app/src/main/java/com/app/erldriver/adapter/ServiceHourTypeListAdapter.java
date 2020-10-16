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
import com.app.erldriver.databinding.RowMyOrderListBinding;
import com.app.erldriver.databinding.RowServiceHourTypeListBinding;
import com.app.erldriver.model.entity.info.ServiceInfo;
import com.app.erldriver.util.AppConstant;

import java.util.List;

public class ServiceHourTypeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ServiceInfo> list;
    private SelectItemListener listener;
    private int position;

    public ServiceHourTypeListAdapter(Context context, List<ServiceInfo> list, SelectItemListener listener) {
        this.mContext = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_service_hour_type_list, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        ServiceInfo info = list.get(position);
        itemViewHolder.getData(info);

        if (this.position == position) {
            itemViewHolder.binding.txtHourType.setBackgroundColor(mContext.getResources().getColor(R.color.colorActiveHourModule));
            itemViewHolder.binding.txtHourType.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        } else {
            itemViewHolder.binding.txtHourType.setBackgroundColor(mContext.getResources().getColor(R.color.colorInActiveHourModule));
            itemViewHolder.binding.txtHourType.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryText));
        }

        itemViewHolder.binding.routMainView.setOnClickListener(v -> {
            if (listener != null && getPosition() != position) {
                listener.onSelectItem(position, AppConstant.Action.SELECT_SERVICE_HOUR_TYPE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowServiceHourTypeListBinding binding;

        public void getData(ServiceInfo info) {
            binding.setInfo(info);
        }

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    public List<ServiceInfo> getList() {
        return list;
    }

    public void setList(List<ServiceInfo> list) {
        this.list = list;
    }
}

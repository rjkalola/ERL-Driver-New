package com.app.erldriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.databinding.RowAddressListBinding;
import com.app.erldriver.databinding.RowDashboardPagerDotsBinding;

public class DashboardOfferPagerDotsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private int totalSize, position;

    public DashboardOfferPagerDotsAdapter(Context context, int totalSize) {
        this.mContext = context;
        this.totalSize = totalSize;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_dashboard_pager_dots, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if (this.position == position) {
            itemViewHolder.binding.imgDot.setImageResource(R.drawable.img_circle_yellow_with_black_corner);
        } else {
            itemViewHolder.binding.imgDot.setImageResource(R.drawable.img_circle_black);
        }
    }

    @Override
    public int getItemCount() {
        return totalSize;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RowDashboardPagerDotsBinding binding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void setSelectedDot(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

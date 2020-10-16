package com.app.erldriver.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.callback.SelectedServiceItemListener;
import com.app.erldriver.databinding.DialogAddItemsToCartBinding;
import com.app.erldriver.databinding.RowServiceTypeListBinding;
import com.app.erldriver.model.entity.info.ServiceItemInfo;
import com.app.utilities.utils.Constant;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class AddItemsToCardBottomSheetDialog extends BottomSheetDialog {
    DialogAddItemsToCartBinding binding;
    private static Context context;
    private static SelectedServiceItemListener listener = null;
    private static List<ServiceItemInfo> list;
    private static int rootPosition = 0;
    private static String itemImage;
    private ServiceTypeListAdapter adapter;

    public AddItemsToCardBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public static AddItemsToCardBottomSheetDialog newInstance(Context mContext, int position, List<ServiceItemInfo> listItems, String image, SelectedServiceItemListener selectedServiceItemListener) {
        context = mContext;
        listener = selectedServiceItemListener;
        rootPosition = position;
        list = listItems;
        itemImage = image;
        return new AddItemsToCardBottomSheetDialog(mContext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_add_items_to_cart, null);
        binding = DataBindingUtil.bind(sheetView);
        if (!StringHelper.isEmpty(itemImage))
            GlideUtil.loadImage(itemImage, binding.imgItem, null, null, Constant.ImageScaleType.CENTER_CROP, null);
        setAdapter();

        binding.imgClose.setOnClickListener(v -> dismiss());

        setContentView(sheetView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    private void setAdapter() {
        if (list != null && list.size() > 0) {
            binding.rvServiceList.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            binding.rvServiceList.setLayoutManager(linearLayoutManager);
            binding.rvServiceList.setHasFixedSize(true);
            adapter = new ServiceTypeListAdapter(context);
            binding.rvServiceList.setAdapter(adapter);
        }
    }

    public class ServiceTypeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context mContext;

        public ServiceTypeListAdapter(Context context) {
            this.mContext = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_service_type_list, parent, false);
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
            private RowServiceTypeListBinding binding;

            public void getData(ServiceItemInfo info) {
                binding.setInfo(info);
            }

            public ItemViewHolder(View itemView) {
                super(itemView);
                binding = DataBindingUtil.bind(itemView);
            }
        }

        public void setSelectedItemCont(ServiceItemInfo info, RowServiceTypeListBinding binding) {
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


}
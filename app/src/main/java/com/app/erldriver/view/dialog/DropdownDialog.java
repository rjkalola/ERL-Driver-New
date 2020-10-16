package com.app.erldriver.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.erldriver.R;
import com.app.erldriver.databinding.DialogDropDownBinding;
import com.app.erldriver.databinding.RowDialogDropdownBinding;
import com.app.erldriver.model.entity.info.ModuleInfo;
import com.app.erldriver.model.entity.info.ModuleSelection;
import com.app.utilities.utils.StringHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DropdownDialog extends DialogFragment {
    DialogDropDownBinding layoutBinding;
    private static List<ModuleInfo> mList;
    private static String title;
    private static int type;
    private static boolean singleChoice;
    private static Context context;
    private ListAdapter adapter;
    AlertDialog dialog;

    public DropdownDialog() {
    }

    public static DropdownDialog newInstance(Context mContext, String dropDownTitle, List<ModuleInfo> dataList, int moduleType, boolean isSingleChoice) {
        DropdownDialog frag = new DropdownDialog();
        mList = new ArrayList<>();
        if (dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                ModuleInfo info = new ModuleInfo();
                info.copyData(dataList.get(i));
                mList.add(info);
            }
        }

//        mList.addAll(dataList);
        title = dropDownTitle;
        singleChoice = isSingleChoice;
        type = moduleType;
        context = mContext;
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragmentStyle);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.setTitle(title);
//        return dialog;


        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());

        if (!StringHelper.isEmpty(title))
            ad.setTitle(title);

        if (!singleChoice) {
            ad.setPositiveButton(context.getString(R.string.lbl_select),
                    (dialog, whichButton) -> {
                        ModuleSelection moduleSelection = new ModuleSelection(type, null, mList);
                        EventBus.getDefault().post(moduleSelection);
                    });

            ad.setNegativeButton(context.getString(R.string.lbl_cancel),
                    (dialog, whichButton) -> {

                    });
        }

        LayoutInflater i = getActivity().getLayoutInflater();
        View view = i.inflate(R.layout.dialog_drop_down, null);
        layoutBinding = DataBindingUtil.bind(view);
        setAdapter();

//        layoutBinding.routClearText.setOnClickListener(v -> {
//            Log.e("test","click");
//            layoutBinding.edtSearch.setText("ravi");
//            layoutBinding.edtSearch.invalidate();
//        });

        layoutBinding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ad.setView(view);
        dialog = ad.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                checkButtonVisibility();
            }
        });
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_drop_down, container, false);
        layoutBinding = DataBindingUtil.bind(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setAdapter() {
        layoutBinding.recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        layoutBinding.recyclerView.setLayoutManager(mLayoutManager);
        adapter = new ListAdapter();
        layoutBinding.recyclerView.setAdapter(adapter);
    }

    public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<ModuleInfo> _listOfAllData;

        public ListAdapter() {
            _listOfAllData = new ArrayList<>();
            _listOfAllData.addAll(mList);
            setHasStableIds(true);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_dialog_dropdown, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ModuleInfo data = mList.get(position);
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            if (!singleChoice) {
                itemViewHolder.binding.check.setVisibility(View.VISIBLE);
                itemViewHolder.binding.check.setChecked(data.isCheck());
            } else {
                itemViewHolder.binding.check.setVisibility(View.GONE);
            }

            itemViewHolder.binding.check.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mList.get(position).setCheck(isChecked);
                checkButtonVisibility();

            });

            itemViewHolder.binding.routMainView.setTag(position);
            itemViewHolder.binding.routMainView.setOnClickListener(v -> {
                if (singleChoice) {
                    ModuleSelection moduleSelection = new ModuleSelection(type, data, mList);
                    EventBus.getDefault().post(moduleSelection);
                    getDialog().dismiss();
                } else {
                    if (mList.get(position).isCheck()) {
                        mList.get(position).setCheck(false);
                        itemViewHolder.binding.check.setChecked(false);
                    } else {
                        mList.get(position).setCheck(true);
                        itemViewHolder.binding.check.setChecked(true);
                    }
                    checkButtonVisibility();
                }
            });
            itemViewHolder.getData(data);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            RowDialogDropdownBinding binding;

            public void getData(ModuleInfo info) {
                binding.setInfo(info);
            }

            public ItemViewHolder(View itemView) {
                super(itemView);
                binding = DataBindingUtil.bind(itemView);
            }
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            mList.clear();

            if (charText.length() == 0) {
                mList.addAll(_listOfAllData);
            } else {
                for (ModuleInfo wp : _listOfAllData) {
                    try {
                        if (String.valueOf(wp.getName()).toLowerCase(Locale.getDefault()).contains(charText)) {
                            mList.add(wp);
                        }
                    } catch (Exception e) {
                        Log.e(getClass().getName(), "Exception in Filter :" + e.getMessage());
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public void checkButtonVisibility() {
        boolean enable = false;
        for (int i = 0; i < mList.size(); i++) {
            ModuleInfo info = mList.get(i);
            if (info.isCheck()) {
                enable = true;
            }
        }
        if (enable)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        else
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

    }


}

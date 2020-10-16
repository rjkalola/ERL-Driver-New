package com.app.erldriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.app.erldriver.R;
import com.app.erldriver.databinding.RowDashboardOfferPagerItemBinding;
import com.app.erldriver.model.entity.info.ClientDashBoardInfo;
import com.app.utilities.utils.GlideUtil;
import com.app.utilities.utils.StringHelper;

import java.util.List;


public class DashboardOfferPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private Context mContext;
    private List<ClientDashBoardInfo> list;

    public DashboardOfferPagerAdapter(Context context, List<ClientDashBoardInfo> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RowDashboardOfferPagerItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.row_dashboard_offer_pager_item, container, false);
        if (!StringHelper.isEmpty(list.get(position).getImage()))
            GlideUtil.loadImage(list.get(position).getImage(), binding.imgSlide, null, null, 0, null);
        container.addView(binding.getRoot());
        return binding.getRoot();
    }


    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }


}

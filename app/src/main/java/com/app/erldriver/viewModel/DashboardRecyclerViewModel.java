package com.app.erldriver.viewModel;

import android.content.Context;

import com.app.erldriver.binding.RecyclerConfiguration;
import com.app.erldriver.model.entity.dashboard.DashboardModel;

import java.util.List;

/**
 * Created by pc on 29-06-2017.
 */

public class DashboardRecyclerViewModel extends BaseViewModel {

    public final RecyclerConfiguration recyclerConfiguration = new RecyclerConfiguration();
    private List<DashboardModel> dashboardDetailList;
    private Context mContext;

    public DashboardRecyclerViewModel(Context context) {
        mContext = context;
        initRecycler();
    }

    private void initRecycler() {
//        RecyclerBindingAdapter<DashboardModel> adapter = getAdapter();
//        recyclerConfiguration.setLayoutManager(new GridLayoutManager(mContext, 2));
//        recyclerConfiguration.setItemAnimator(new DefaultItemAnimator());
//        recyclerConfiguration.setAdapter(adapter);
    }

    /*private RecyclerBindingAdapter<DashboardModel> getAdapter() {
        dashboardDetailList = new ArrayList<>();

        if(AppUtils.getLoginPreference() != null){
            if(AppUtils.getLoginPreference().isShowGallery()){
                dashboardDetailList.add(new DashboardModel(mContext.getString(R.string.about_us),   R.color.color_notification, R.drawable.about_us_vector));
                dashboardDetailList.add(new DashboardModel(mContext.getString(R.string.donate_pustak),   R.color.color_offer, R.drawable.pustak_bank_logo));
                dashboardDetailList.add(new DashboardModel(mContext.getString(R.string.gallery),  R.color.color_my_purchase, R.drawable.gallery_white_vector));
                dashboardDetailList.add(new DashboardModel(mContext.getString(R.string.contact_us),   R.color.color_upload_purchase , R.drawable.contact_us_vector));

            }else{
                dashboardDetailList.add(new DashboardModel(mContext.getString(R.string.about_us),   R.color.color_notification, R.drawable.about_us_vector));
                dashboardDetailList.add(new DashboardModel(mContext.getString(R.string.donate_pustak),   R.color.color_offer, R.drawable.donate_vector));
                dashboardDetailList.add(new DashboardModel(mContext.getString(R.string.contact_us),   R.color.color_upload_purchase , R.drawable.contact_us_vector));

            }
        }


        RecyclerBindingAdapter<DashboardModel> adapter = new RecyclerBindingAdapter<DashboardModel>(R.layout.view_dashboard_adapter_item, BR.dashboardModel, (AbstractList<DashboardModel>) dashboardDetailList);

        adapter.setOnItemClickListener((position, item) -> {
            if(item.getTitle().equals(mContext.getString(R.string.donate_pustak))){
                ((BaseActivity)mContext).moveActivity(mContext, DonateListActivity.class, false, false, null);

            }else if(item.getTitle().equals(mContext.getString(R.string.gallery))){
                ((BaseActivity)mContext).moveActivity(mContext, GalleryActivity.class, false, false, null);

            }else if(item.getTitle().equals(mContext.getString(R.string.contact_us))){
                ((BaseActivity)mContext).moveActivity(mContext, ContactUsActivity.class, false, false, null);

            }else if(item.getTitle().equals(mContext.getString(R.string.about_us))){
                ((BaseActivity)mContext).moveActivity(mContext, AboutUsActivity.class, false, false, null);

            }
        });
        return adapter;
    }*/


}

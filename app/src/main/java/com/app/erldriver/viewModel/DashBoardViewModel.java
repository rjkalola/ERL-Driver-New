package com.app.erldriver.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.app.erldriver.ERLApp;
import com.app.erldriver.model.entity.response.DashBoardResponse;
import com.app.erldriver.model.entity.response.OurServicesResponse;
import com.app.erldriver.model.entity.response.PrivacyPolicyResponse;
import com.app.erldriver.model.entity.response.ServiceItemsResponse;
import com.app.erldriver.model.entity.response.StoreLocatorResponse;
import com.app.erldriver.model.state.DashBoardServiceInterface;
import com.app.erldriver.network.RXRetroManager;
import com.app.erldriver.network.RetrofitException;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.ResourceProvider;

import javax.inject.Inject;

import io.reactivex.Observable;

public class DashBoardViewModel extends BaseViewModel {
    @Inject
    DashBoardServiceInterface dashBoardServiceInterface;

    private MutableLiveData<DashBoardResponse> dashBoardResponse;
    private MutableLiveData<ServiceItemsResponse> serviceItemsResponse;
    private MutableLiveData<PrivacyPolicyResponse> privacyPolicyResponse;
    private MutableLiveData<StoreLocatorResponse> storeLocatorResponse;
    private MutableLiveData<OurServicesResponse> ourServicesResponse;

    public DashBoardViewModel(ResourceProvider resourceProvider) {
        ERLApp.getServiceComponent().inject(this);
    }

    public void getDashboardRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<DashBoardResponse>() {
            @Override
            protected void onSuccess(DashBoardResponse response) {
                if (view != null) {
                    dashBoardResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashBoardServiceInterface.getDashboard());
    }

    public void getServiceItemsRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<ServiceItemsResponse>() {
            @Override
            protected void onSuccess(ServiceItemsResponse response) {
                if (view != null) {
                    serviceItemsResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashBoardServiceInterface.getServiceItems());
    }

    public void getPrivacyPolicyRequest(int action) {
        if (view != null) {
            view.showProgress();
        }
        Observable<PrivacyPolicyResponse> observable = null;
        if (action == AppConstant.Type.TERMS_CONDITIONS) {
            observable = dashBoardServiceInterface.getTermsConditions();
        } else if (action == AppConstant.Type.PRIVACY_POLICY) {
            observable = dashBoardServiceInterface.getPrivacyPolicy();
        }

        new RXRetroManager<PrivacyPolicyResponse>() {
            @Override
            protected void onSuccess(PrivacyPolicyResponse response) {
                if (view != null) {
                    privacyPolicyResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(observable);
    }

    public void getOurServicesRequest() {
        if (view != null) {
            view.showProgress();
        }

        new RXRetroManager<OurServicesResponse>() {
            @Override
            protected void onSuccess(OurServicesResponse response) {
                if (view != null) {
                    ourServicesResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashBoardServiceInterface.getOurServices());
    }

    public void getStoreLocatorRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<StoreLocatorResponse>() {
            @Override
            protected void onSuccess(StoreLocatorResponse response) {
                if (view != null) {
                    storeLocatorResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashBoardServiceInterface.getStoreLocator());
    }

    public MutableLiveData<DashBoardResponse> dashBoardResponse() {
        if (dashBoardResponse == null) {
            dashBoardResponse = new MutableLiveData<>();
        }
        return dashBoardResponse;
    }

    public MutableLiveData<ServiceItemsResponse> serviceItemsResponse() {
        if (serviceItemsResponse == null) {
            serviceItemsResponse = new MutableLiveData<>();
        }
        return serviceItemsResponse;
    }

    public MutableLiveData<PrivacyPolicyResponse> privacyPolicyResponse() {
        if (privacyPolicyResponse == null) {
            privacyPolicyResponse = new MutableLiveData<>();
        }
        return privacyPolicyResponse;
    }

    public MutableLiveData<StoreLocatorResponse> storeLocatorResponse() {
        if (storeLocatorResponse == null) {
            storeLocatorResponse = new MutableLiveData<>();
        }
        return storeLocatorResponse;
    }

    public MutableLiveData<OurServicesResponse> ourServicesResponse() {
        if (ourServicesResponse == null) {
            ourServicesResponse = new MutableLiveData<>();
        }
        return ourServicesResponse;
    }
}

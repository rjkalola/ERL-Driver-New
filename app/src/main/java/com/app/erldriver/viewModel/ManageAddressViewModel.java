package com.app.erldriver.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.app.erldriver.ERLApp;
import com.app.erldriver.model.entity.request.SaveAddressRequest;
import com.app.erldriver.model.entity.response.AddressListResponse;
import com.app.erldriver.model.entity.response.AddressResourcesResponse;
import com.app.erldriver.model.entity.response.BaseResponse;
import com.app.erldriver.model.state.ManageAddressInterface;
import com.app.erldriver.network.RXRetroManager;
import com.app.erldriver.network.RetrofitException;
import com.app.erldriver.util.ResourceProvider;

import javax.inject.Inject;

public class ManageAddressViewModel extends BaseViewModel {
    @Inject
    ManageAddressInterface manageAddressInterface;

    private MutableLiveData<AddressResourcesResponse> addressResourcesResponse;
    private MutableLiveData<AddressListResponse> addressListResponse;
    private MutableLiveData<BaseResponse> mBaseResponse;
    private MutableLiveData<BaseResponse> chaneDefaultAddressResponse;

    private SaveAddressRequest saveAddressRequest;

    public ManageAddressViewModel(ResourceProvider resourceProvider) {
        ERLApp.getServiceComponent().inject(this);
        saveAddressRequest = new SaveAddressRequest();
    }

    public void getAddressResourcesRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<AddressResourcesResponse>() {
            @Override
            protected void onSuccess(AddressResourcesResponse response) {
                if (view != null) {
                    addressResourcesResponse.postValue(response);
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
        }.rxSingleCall(manageAddressInterface.getAddressResources());
    }

    public void saveAddressRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<BaseResponse>() {
            @Override
            protected void onSuccess(BaseResponse response) {
                if (view != null) {
                    mBaseResponse.postValue(response);
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
        }.rxSingleCall(manageAddressInterface.saveAddress(saveAddressRequest));
    }

    public void getAddressesRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<AddressListResponse>() {
            @Override
            protected void onSuccess(AddressListResponse response) {
                if (view != null) {
                    addressListResponse.postValue(response);
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
        }.rxSingleCall(manageAddressInterface.getAddresses());
    }

    public void changeDefaultAddressRequest(int id) {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<BaseResponse>() {
            @Override
            protected void onSuccess(BaseResponse response) {
                if (view != null) {
                    chaneDefaultAddressResponse.postValue(response);
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
        }.rxSingleCall(manageAddressInterface.changeDefaultAddress(id));
    }

    public void deleteAddressRequest(int id) {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<BaseResponse>() {
            @Override
            protected void onSuccess(BaseResponse response) {
                if (view != null) {
                    mBaseResponse.postValue(response);
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
        }.rxSingleCall(manageAddressInterface.deleteAddress(id));
    }

    public MutableLiveData<AddressResourcesResponse> addressResourcesResponse() {
        if (addressResourcesResponse == null) {
            addressResourcesResponse = new MutableLiveData<>();
        }
        return addressResourcesResponse;
    }

    public MutableLiveData<BaseResponse> mBaseResponse() {
        if (mBaseResponse == null) {
            mBaseResponse = new MutableLiveData<>();
        }
        return mBaseResponse;
    }

    public MutableLiveData<AddressListResponse> getAddressListResponse() {
        if (addressListResponse == null) {
            addressListResponse = new MutableLiveData<>();
        }
        return addressListResponse;
    }

    public MutableLiveData<BaseResponse> chaneDefaultAddressResponse() {
        if (chaneDefaultAddressResponse == null) {
            chaneDefaultAddressResponse = new MutableLiveData<>();
        }
        return chaneDefaultAddressResponse;
    }

    public SaveAddressRequest getSaveAddressRequest() {
        return saveAddressRequest;
    }

    public void setSaveAddressRequest(SaveAddressRequest saveAddressRequest) {
        this.saveAddressRequest = saveAddressRequest;
    }
}

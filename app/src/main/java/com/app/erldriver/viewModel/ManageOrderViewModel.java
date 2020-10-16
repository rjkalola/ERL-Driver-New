package com.app.erldriver.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.app.erldriver.ERLApp;
import com.app.erldriver.model.entity.request.SaveOrderRequest;
import com.app.erldriver.model.entity.response.BaseResponse;
import com.app.erldriver.model.entity.response.OrderDetailsResponse;
import com.app.erldriver.model.entity.response.OrderListResponse;
import com.app.erldriver.model.entity.response.OrderResourcesResponse;
import com.app.erldriver.model.state.ManageOrderInterface;
import com.app.erldriver.network.RXRetroManager;
import com.app.erldriver.network.RetrofitException;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.ResourceProvider;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ManageOrderViewModel extends BaseViewModel {
    @Inject
    ManageOrderInterface manageOrderInterface;

    private MutableLiveData<OrderResourcesResponse> orderResourcesResponse;
    private MutableLiveData<BaseResponse> mBaseResponse;
    private MutableLiveData<OrderListResponse> mOrderListResponse;
    private MutableLiveData<OrderDetailsResponse> mOrderDetailsResponse;

    private SaveOrderRequest saveOrderRequest;

    public ManageOrderViewModel(ResourceProvider resourceProvider) {
        ERLApp.getServiceComponent().inject(this);
        saveOrderRequest = new SaveOrderRequest();
    }

    public void getOrderResourcesRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<OrderResourcesResponse>() {
            @Override
            protected void onSuccess(OrderResourcesResponse response) {
                if (view != null) {
                    orderResourcesResponse.postValue(response);
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
        }.rxSingleCall(manageOrderInterface.getOrderResources());
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
        }.rxSingleCall(manageOrderInterface.placeOrder(saveOrderRequest));
    }


    public void getOrders(int limit, int offset, boolean isProgress, int orderType) {
        if (view != null && isProgress) {
            view.showProgress();
        }

        Observable<OrderListResponse> observable = null;
        if (orderType == AppConstant.Type.ORDER_PICKUPS) {
            observable = manageOrderInterface.pickUpOrders();
        } else if (orderType == AppConstant.Type.ORDER_DROPS) {
            observable = manageOrderInterface.dropOrders();
        }

        new RXRetroManager<OrderListResponse>() {
            @Override
            protected void onSuccess(OrderListResponse response) {
                if (view != null) {
                    mOrderListResponse.postValue(response);
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

    public void clientCancelOrders(int id) {
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
        }.rxSingleCall(manageOrderInterface.clientCancelOrders(id));
    }

    public void clientOrderDetailsRequest(int orderId) {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<OrderDetailsResponse>() {
            @Override
            protected void onSuccess(OrderDetailsResponse response) {
                if (view != null) {
                    mOrderDetailsResponse.postValue(response);
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
        }.rxSingleCall(manageOrderInterface.clientOrderDetails(orderId));
    }

    public void pickedUpOrderRequest(int id, String note) {
        RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id));
        RequestBody noteBody = RequestBody.create(MediaType.parse("text/plain"), note);

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
        }.rxSingleCall(manageOrderInterface.pickedUpOrder(idBody, noteBody));
    }

    public void deliveredOrderRequest(int id, String note) {
        RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id));
        RequestBody noteBody = RequestBody.create(MediaType.parse("text/plain"), note);

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
        }.rxSingleCall(manageOrderInterface.deliveredOrder(idBody,noteBody));
    }

    public MutableLiveData<BaseResponse> mBaseResponse() {
        if (mBaseResponse == null) {
            mBaseResponse = new MutableLiveData<>();
        }
        return mBaseResponse;
    }

    public MutableLiveData<OrderResourcesResponse> orderResourcesResponse() {
        if (orderResourcesResponse == null) {
            orderResourcesResponse = new MutableLiveData<>();
        }
        return orderResourcesResponse;
    }

    public MutableLiveData<OrderListResponse> orderListResponse() {
        if (mOrderListResponse == null) {
            mOrderListResponse = new MutableLiveData<>();
        }
        return mOrderListResponse;
    }

    public MutableLiveData<OrderDetailsResponse> mOrderDetailsResponse() {
        if (mOrderDetailsResponse == null) {
            mOrderDetailsResponse = new MutableLiveData<>();
        }
        return mOrderDetailsResponse;
    }


    public SaveOrderRequest getSaveOrderRequest() {
        return saveOrderRequest;
    }

    public void setSaveOrderRequest(SaveOrderRequest saveOrderRequest) {
        this.saveOrderRequest = saveOrderRequest;
    }
}

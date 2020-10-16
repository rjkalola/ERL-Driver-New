package com.app.erldriver.model.state;


import com.app.erldriver.model.entity.request.SaveAddressRequest;
import com.app.erldriver.model.entity.response.AddressListResponse;
import com.app.erldriver.model.entity.response.AddressResourcesResponse;
import com.app.erldriver.model.entity.response.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ManageAddressInterface {
    @GET("address-resources")
    Observable<AddressResourcesResponse> getAddressResources();

    @GET("get-addresses")
    Observable<AddressListResponse> getAddresses();

    @POST("save-address")
    Observable<BaseResponse> saveAddress(@Body SaveAddressRequest saveAddressRequest);

    @Multipart
    @POST("delete-address")
    Observable<BaseResponse> deleteAddress(@Part("id") int id);

    @Multipart
    @POST("change-default-address")
    Observable<BaseResponse> changeDefaultAddress(@Part("id") int id);
}

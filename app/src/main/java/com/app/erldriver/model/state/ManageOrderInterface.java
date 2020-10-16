package com.app.erldriver.model.state;


import com.app.erldriver.model.entity.request.SaveOrderRequest;
import com.app.erldriver.model.entity.response.BaseResponse;
import com.app.erldriver.model.entity.response.OrderDetailsResponse;
import com.app.erldriver.model.entity.response.OrderListResponse;
import com.app.erldriver.model.entity.response.OrderResourcesResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ManageOrderInterface {
    @GET("order-resources")
    Observable<OrderResourcesResponse> getOrderResources();

    @POST("place-order")
    Observable<BaseResponse> placeOrder(@Body SaveOrderRequest saveOrderRequest);

    @GET("pickup-orders")
    Observable<OrderListResponse> pickUpOrders();

    @GET("drop-orders")
    Observable<OrderListResponse> dropOrders();


    @Multipart
    @POST("client-cancel-order")
    Observable<BaseResponse> clientCancelOrders(@Part("id") int id);

    @Multipart
    @POST("client-order-detail")
    Observable<OrderDetailsResponse> clientOrderDetails(@Part("order_id") int order_id);

    @Multipart
    @POST("pickedup-order")
    Observable<BaseResponse> pickedUpOrder(@Part("id") RequestBody id,@Part("pickup_note") RequestBody pickup_note);

    @Multipart
    @POST("delivered-order")
    Observable<BaseResponse> deliveredOrder(@Part("id") RequestBody id,@Part("drop_note") RequestBody drop_note);


}

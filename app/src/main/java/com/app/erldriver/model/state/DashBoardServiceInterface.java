package com.app.erldriver.model.state;


import com.app.erldriver.model.entity.response.DashBoardResponse;
import com.app.erldriver.model.entity.response.OurServicesResponse;
import com.app.erldriver.model.entity.response.PrivacyPolicyResponse;
import com.app.erldriver.model.entity.response.ServiceItemsResponse;
import com.app.erldriver.model.entity.response.StoreLocatorResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface DashBoardServiceInterface {
    @GET("dashboard")
    Observable<DashBoardResponse> getDashboard();

    @GET("get-service-items")
    Observable<ServiceItemsResponse> getServiceItems();

    @GET("terms-condition")
    Observable<PrivacyPolicyResponse> getTermsConditions();

    @GET("privacy-policy")
    Observable<PrivacyPolicyResponse> getPrivacyPolicy();

    @GET("our-services")
    Observable<OurServicesResponse> getOurServices();

    @GET("store-locators")
    Observable<StoreLocatorResponse> getStoreLocator();
}

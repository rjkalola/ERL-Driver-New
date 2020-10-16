package com.app.erldriver.injection.module;


import com.app.erldriver.injection.scope.UserScope;
import com.app.erldriver.model.state.ManageAddressInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ManageAddressModule {
    @UserScope
    @Provides
    public ManageAddressInterface provideUserAuthenticationService(Retrofit retrofit) {
        return retrofit.create(ManageAddressInterface.class);
    }

}

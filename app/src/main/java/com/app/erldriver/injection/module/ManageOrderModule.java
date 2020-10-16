package com.app.erldriver.injection.module;


import com.app.erldriver.injection.scope.UserScope;
import com.app.erldriver.model.state.ManageOrderInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ManageOrderModule {
    @UserScope
    @Provides
    public ManageOrderInterface provideUserAuthenticationService(Retrofit retrofit) {
        return retrofit.create(ManageOrderInterface.class);
    }

}

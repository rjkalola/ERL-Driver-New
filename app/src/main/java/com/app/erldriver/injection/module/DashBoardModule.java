package com.app.erldriver.injection.module;



import com.app.erldriver.injection.scope.UserScope;
import com.app.erldriver.model.state.DashBoardServiceInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class DashBoardModule {
    @UserScope
    @Provides
    public DashBoardServiceInterface provideUserAuthenticationService(Retrofit retrofit){
        return retrofit.create(DashBoardServiceInterface.class);
    }

}

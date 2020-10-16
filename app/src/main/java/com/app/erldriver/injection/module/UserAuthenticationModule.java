package com.app.erldriver.injection.module;



import com.app.erldriver.injection.scope.UserScope;
import com.app.erldriver.model.state.UserAuthenticationServiceInterface;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 */
@Module
public class UserAuthenticationModule {
    @UserScope
    @Provides
    public UserAuthenticationServiceInterface provideUserAuthenticationService(Retrofit retrofit){
        return retrofit.create(UserAuthenticationServiceInterface.class);
    }

}

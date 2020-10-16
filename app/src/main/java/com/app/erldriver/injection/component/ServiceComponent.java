package com.app.erldriver.injection.component;

import com.app.erldriver.injection.module.DashBoardModule;
import com.app.erldriver.injection.module.ManageAddressModule;
import com.app.erldriver.injection.module.ManageOrderModule;
import com.app.erldriver.injection.module.UserAuthenticationModule;
import com.app.erldriver.injection.scope.UserScope;
import com.app.erldriver.viewModel.DashBoardViewModel;
import com.app.erldriver.viewModel.ManageAddressViewModel;
import com.app.erldriver.viewModel.ManageOrderViewModel;
import com.app.erldriver.viewModel.UserAuthenticationViewModel;

import dagger.Component;

@UserScope
@Component(dependencies = NetworkComponent.class
        , modules = {UserAuthenticationModule.class, DashBoardModule.class, ManageAddressModule.class, ManageOrderModule.class})

public interface ServiceComponent {
    void inject(UserAuthenticationViewModel userAuthenticationViewModel);

    void inject(DashBoardViewModel dashboardModel);

    void inject(ManageAddressViewModel manageAddressViewModel);

    void inject(ManageOrderViewModel manageOrderViewModel);
}

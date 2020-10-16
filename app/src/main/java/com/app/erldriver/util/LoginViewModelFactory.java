package com.app.erldriver.util;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.erldriver.viewModel.DashBoardViewModel;
import com.app.erldriver.viewModel.ManageAddressViewModel;
import com.app.erldriver.viewModel.ManageOrderViewModel;
import com.app.erldriver.viewModel.UserAuthenticationViewModel;

public class LoginViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private ResourceProvider resourceProvider;


    public LoginViewModelFactory(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserAuthenticationViewModel.class)) {
            return (T) new UserAuthenticationViewModel(resourceProvider);
        } else if (modelClass.isAssignableFrom(DashBoardViewModel.class)) {
            return (T) new DashBoardViewModel(resourceProvider);
        }else if (modelClass.isAssignableFrom(ManageAddressViewModel.class)) {
            return (T) new ManageAddressViewModel(resourceProvider);
        }else if (modelClass.isAssignableFrom(ManageOrderViewModel.class)) {
            return (T) new ManageOrderViewModel(resourceProvider);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
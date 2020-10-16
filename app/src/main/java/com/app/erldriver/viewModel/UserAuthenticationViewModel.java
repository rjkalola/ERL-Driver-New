package com.app.erldriver.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.app.erldriver.ERLApp;
import com.app.erldriver.model.entity.request.LoginRequest;
import com.app.erldriver.model.entity.request.SignUpRequest;
import com.app.erldriver.model.entity.response.BaseResponse;
import com.app.erldriver.model.entity.response.ForgotPasswordResponse;
import com.app.erldriver.model.entity.response.GetMessagesResponse;
import com.app.erldriver.model.entity.response.ProfileResponse;
import com.app.erldriver.model.entity.response.SendMessageResponse;
import com.app.erldriver.model.entity.response.UserResponse;
import com.app.erldriver.model.state.UserAuthenticationServiceInterface;
import com.app.erldriver.network.RXRetroManager;
import com.app.erldriver.network.RetrofitException;
import com.app.erldriver.util.AppConstant;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.ResourceProvider;
import com.app.utilities.utils.StringHelper;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserAuthenticationViewModel extends BaseViewModel {
    private LoginRequest loginRequest;
    private SignUpRequest signUpRequest;

    @Inject
    UserAuthenticationServiceInterface userAuthenticationServiceInterface;

    private MutableLiveData<UserResponse> mUserResponse;
    private MutableLiveData<ForgotPasswordResponse> mForgotPasswordResponse;
    private MutableLiveData<BaseResponse> mBaseResponse;
    private MutableLiveData<ProfileResponse> mProfileResponse;
    private MutableLiveData<ProfileResponse> mSaveProfileResponse;
    private MutableLiveData<SendMessageResponse> mSendMessageResponse;
    private MutableLiveData<GetMessagesResponse> getMessagesResponse;

    public UserAuthenticationViewModel(ResourceProvider resourceProvider) {
        ERLApp.getServiceComponent().inject(this);
        this.loginRequest = new LoginRequest();
        this.signUpRequest = new SignUpRequest();
    }

    public void sendLoginRequest() {
        loginRequest.setDevice_token(AppUtils.getDeviceUniqueId());

        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<UserResponse>() {
            @Override
            protected void onSuccess(UserResponse response) {
                if (view != null) {
                    mUserResponse.postValue(response);
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
        }.rxSingleCall(userAuthenticationServiceInterface.login(loginRequest));
    }

    public void sendSignUpRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<UserResponse>() {
            @Override
            protected void onSuccess(UserResponse response) {
                if (view != null) {
                    mUserResponse.postValue(response);
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
        }.rxSingleCall(userAuthenticationServiceInterface.signUp(signUpRequest));
    }

    public void forgotPasswordRequest(String email) {
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);

        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<ForgotPasswordResponse>() {
            @Override
            protected void onSuccess(ForgotPasswordResponse response) {
                if (view != null) {
                    mForgotPasswordResponse.postValue(response);
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
        }.rxSingleCall(userAuthenticationServiceInterface.forgotPassword(emailBody));
    }

    public void resendCode(int userId) {
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
        }.rxSingleCall(userAuthenticationServiceInterface.resendCode(userId));
    }

    public void verifyCode(int userId, String code) {
        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));
        RequestBody codeBody = RequestBody.create(MediaType.parse("text/plain"), code);

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
        }.rxSingleCall(userAuthenticationServiceInterface.verifyCode(userIdBody, codeBody));
    }

    public void resetPassword(int userId, String password) {
        RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);

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
        }.rxSingleCall(userAuthenticationServiceInterface.resetPassword(userIdBody, passwordBody));
    }

    public void getProfileRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<ProfileResponse>() {
            @Override
            protected void onSuccess(ProfileResponse response) {
                if (view != null) {
                    mProfileResponse.postValue(response);
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
        }.rxSingleCall(userAuthenticationServiceInterface.getProfile());
    }

    public void saveProfile(ProfileResponse info) {
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), info.getName());
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), info.getEmail());
        RequestBody phoneBody = RequestBody.create(MediaType.parse("text/plain"), info.getPhone());

        MultipartBody.Part imageFileBody = null;
        if (!StringHelper.isEmpty(info.getImage()) && !info.getImage().startsWith("http")) {
            File file = new File(info.getImage());
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            imageFileBody = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        }

        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<ProfileResponse>() {
            @Override
            protected void onSuccess(ProfileResponse response) {
                if (view != null) {
                    mSaveProfileResponse.postValue(response);
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
        }.rxSingleCall(userAuthenticationServiceInterface.saveProfile(nameBody, emailBody, phoneBody, imageFileBody));
    }

    public void sendMessage(String message, String imagePath,boolean isProgress) {
        RequestBody messageBody = RequestBody.create(MediaType.parse("text/plain"), message);

        MultipartBody.Part imageFileBody = null;
        if (!StringHelper.isEmpty(imagePath)) {
            File file = new File(imagePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            imageFileBody = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        }

        if (view != null && isProgress) {
            view.showProgress();
        }
        new RXRetroManager<SendMessageResponse>() {
            @Override
            protected void onSuccess(SendMessageResponse response) {
                if (view != null) {
                    mSendMessageResponse.postValue(response);
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
        }.rxSingleCall(userAuthenticationServiceInterface.sendMessage(messageBody,imageFileBody));
    }

    public void getMessages(int lastMessageId, boolean isProgress) {
        if (view != null && isProgress) {
            view.showProgress();
        }
        new RXRetroManager<GetMessagesResponse>() {
            @Override
            protected void onSuccess(GetMessagesResponse response) {
                if (view != null) {
                    getMessagesResponse.postValue(response);
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
        }.rxSingleCall(userAuthenticationServiceInterface.getMessages(lastMessageId));
    }

    public void registerFcmRequest(String token) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody deviceTypeBody = RequestBody.create(MediaType.parse("text/plain"), AppConstant.DEVICE_TYPE);

//        if (view != null) {
//            view.showProgress();
//        }
        new RXRetroManager<BaseResponse>() {
            @Override
            protected void onSuccess(BaseResponse response) {
                if (view != null) {
                    mBaseResponse.postValue(response);
//                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
//                    view.hideProgress();
                }
            }
        }.rxSingleCall(userAuthenticationServiceInterface.registerToken(tokenBody, deviceTypeBody));

    }

    public MutableLiveData<BaseResponse> mBaseResponse() {
        if (mBaseResponse == null) {
            mBaseResponse = new MutableLiveData<>();
        }
        return mBaseResponse;
    }

    public MutableLiveData<UserResponse> mUserResponse() {
        if (mUserResponse == null) {
            mUserResponse = new MutableLiveData<>();
        }
        return mUserResponse;
    }

    public MutableLiveData<ForgotPasswordResponse> mForgotPasswordResponse() {
        if (mForgotPasswordResponse == null) {
            mForgotPasswordResponse = new MutableLiveData<>();
        }
        return mForgotPasswordResponse;
    }

    public MutableLiveData<ProfileResponse> mProfileResponse() {
        if (mProfileResponse == null) {
            mProfileResponse = new MutableLiveData<>();
        }
        return mProfileResponse;
    }

    public MutableLiveData<ProfileResponse> mSaveProfileResponse() {
        if (mSaveProfileResponse == null) {
            mSaveProfileResponse = new MutableLiveData<>();
        }
        return mSaveProfileResponse;
    }

    public MutableLiveData<SendMessageResponse> mSendMessageResponse() {
        if (mSendMessageResponse == null) {
            mSendMessageResponse = new MutableLiveData<>();
        }
        return mSendMessageResponse;
    }

    public MutableLiveData<GetMessagesResponse> getMessagesResponse() {
        if (getMessagesResponse == null) {
            getMessagesResponse = new MutableLiveData<>();
        }
        return getMessagesResponse;
    }

    public LoginRequest getLoginRequest() {
        return loginRequest;
    }

    public void setLoginRequest(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
    }

    public SignUpRequest getSignUpRequest() {
        return signUpRequest;
    }

    public void setSignUpRequest(SignUpRequest signUpRequest) {
        this.signUpRequest = signUpRequest;
    }
}

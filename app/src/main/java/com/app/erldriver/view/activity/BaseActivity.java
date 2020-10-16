package com.app.erldriver.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.erldriver.R;
import com.app.erldriver.callback.ViewListener;
import com.app.erldriver.network.RetrofitException;
import com.app.erldriver.util.AppUtils;
import com.app.erldriver.util.LocaleManager;
import com.app.utilities.utils.ProgressDialogHelper;
import com.app.utilities.utils.StringHelper;


/**
 *
 */
public class BaseActivity extends AppCompatActivity implements ViewListener {

    private ProgressDialogHelper progressDialogHelper;

    @Override
    protected void attachBaseContext(Context newBase) {
        //super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        super.attachBaseContext(LocaleManager.setLocale(newBase));
    }

    public void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AppCompatDelegate.getDefaultNightMode()
                    != AppCompatDelegate.MODE_NIGHT_YES) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.WHITE);
            } else {
                getWindow().setStatusBarColor(Color.parseColor("#191b24"));
            }
        }
    }

    public void replaceFragment(@IdRes int containerViewId, @NonNull Fragment mTargetFragment, boolean addToBackStack) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(mTargetFragment.getTag());
        }
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(containerViewId, mTargetFragment, mTargetFragment.getTag());
        fragmentTransaction.commit();
    }

    public void addFragment(@IdRes int containerViewId, @NonNull Fragment mTargetFragment, boolean addToBackStack) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(mTargetFragment.getTag());
        }
        fragmentTransaction.add(containerViewId, mTargetFragment, mTargetFragment.getTag());
        fragmentTransaction.commit();
    }


    public void setupToolbar(final String text, boolean isShowBack) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.toolBarNavigation);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                //getSupportActionBar().setTitle(toolbarTitle.toUpperCase());
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                title.setText(text);
            }

            if (isShowBack) {
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
                toolbar.setNavigationOnClickListener(v -> onBackPressed());
            }
        }
    }

    public void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void setTransparentStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static Intent createNewIntent(Context context, Class<?> tClass) {
        return new Intent(context, tClass);
    }

    public static Intent createNewIntentWithBundle(Context context, Class<?> tClass, Bundle bundle) {
        return createNewIntent(context, tClass).putExtras(bundle);
    }

    public void moveActivity(Context context, Class<?> c, boolean finish, boolean clearStack, Bundle bundle) {
        Intent intent = new Intent(context, c);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
//        Activity activity = (Activity) context;
//        activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        if (finish) {
            ((Activity) context).finish();
        }
    }

    public void moveActivity(Context context, Intent intent, boolean finish, boolean clearStack, Bundle bundle) {
        if (bundle != null) {
            intent.putExtras(bundle);
        }


        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
        Activity activity = (Activity) context;
//        activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        if (finish) {
            ((Activity) context).finish();
        }

    }

    public void moveActivityForResult(Context context, Class<?> c, boolean finish, boolean clearStack, int requestCode, Bundle bundle) {
        Intent intent = new Intent(context, c);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        ((Activity) context).startActivityForResult(intent, requestCode);
        Activity activity = (Activity) context;
//        activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        if (finish) {
            ((Activity) context).finish();
        }
    }

    public void showProgressDialog(Context context, String message) {
        if (progressDialogHelper == null) {
            progressDialogHelper = new ProgressDialogHelper(context);
        }
        if (StringHelper.isEmpty(message)) {
            progressDialogHelper.showCircularProgressDialog();
        } else {
            progressDialogHelper.showProgressDialog(message);
        }

    }

    public void hideProgressDialog() {
        if (progressDialogHelper != null) {
            progressDialogHelper.hideProgressDialog();
            progressDialogHelper.hideCircularProgressDialog();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        showProgressDialog(this, "");
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showApiError(RetrofitException retrofitException, String errorCode) {
        AppUtils.handleApiError(this, retrofitException);
    }
}
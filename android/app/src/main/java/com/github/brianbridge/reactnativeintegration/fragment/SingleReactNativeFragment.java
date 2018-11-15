package com.github.brianbridge.reactnativeintegration.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.github.brianbridge.reactnativeintegration.BuildConfig;
import com.github.brianbridge.reactnativeintegration.R;
import com.github.brianbridge.reactnativeintegration.activity.MainActivity;

/**
 * Created by Brian Ho on 15/11/2018.
 */
public class SingleReactNativeFragment extends Fragment implements DefaultHardwareBackBtnHandler, MainActivity.OnBackPressedListener, MainActivity.OnKeyUpListener {

    private Activity mActivity;
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    private boolean invokeDefaultOnBackPressed;
    private boolean skipOnBackPressed;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mReactRootView = (ReactRootView) inflater.inflate(R.layout.fragment_single_react_native, container, false);

        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(mActivity.getApplication())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModulePath("index")
                .addPackage(new MainReactPackage())
                .setDefaultHardwareBackBtnHandler(this)
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        mReactRootView.startReactApplication(mReactInstanceManager, "MyReactNativeApp", null);

        return mReactRootView;
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        invokeDefaultOnBackPressed = true;
        mActivity.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause(mActivity);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(mActivity, this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy(mActivity);
        }
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (!invokeDefaultOnBackPressed && !skipOnBackPressed && mReactInstanceManager != null) {
            mReactInstanceManager.onBackPressed();
            return true;
        } else if (invokeDefaultOnBackPressed && !skipOnBackPressed) {
            skipOnBackPressed = true;
            return false;
        } else {
            invokeDefaultOnBackPressed = false;
            skipOnBackPressed = false;
            return false;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
            mReactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return false;
    }
}

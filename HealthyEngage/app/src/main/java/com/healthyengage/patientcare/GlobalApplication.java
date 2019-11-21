package com.healthyengage.patientcare;

import android.app.Application;

import com.facebook.stetho.Stetho;

import SessionTimeOut.ApplockManager;


public class GlobalApplication extends Application {



public static GlobalApplication sThis;
    protected String userAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        sThis = this;

        Stetho.initializeWithDefaults(this);

    }
    public void touch() {
        ApplockManager.getInstance().enableDefaultAppLockIfAvailable(sThis);
        ApplockManager.getInstance().updateTouch();
    }

    public static GlobalApplication getThis() {
        return sThis;
    }
}

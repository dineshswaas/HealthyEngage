package com.healthyengage.patientcare;

import android.app.Application;

import com.facebook.stetho.Stetho;


public class GlobalApplication extends Application {



public static GlobalApplication sThis;
    protected String userAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        sThis = this;

        Stetho.initializeWithDefaults(this);

    }

    public static GlobalApplication getThis() {
        return sThis;
    }
}

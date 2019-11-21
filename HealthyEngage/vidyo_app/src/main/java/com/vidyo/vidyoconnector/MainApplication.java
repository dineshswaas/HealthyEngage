
package com.vidyo.vidyoconnector;

import android.app.Application;

import com.vidyo.vidyoconnector.utils.Preferences;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Preferences.initialize(getApplicationContext());
    }
}
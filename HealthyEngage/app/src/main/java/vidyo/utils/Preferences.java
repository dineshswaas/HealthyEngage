package vidyo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.healthyengage.patientcare.BuildConfig;


public class Preferences {

    private static final String VIDYO_PREFERENCES = BuildConfig.APPLICATION_ID + ".vidyo.io.preferences";

    public static final String GUEST_API_ENABLED_KEY = BuildConfig.APPLICATION_ID + ".guest.api.enabled";

    private static SharedPreferences sInstance;

    public static void initialize(Context context) {
        sInstance = context.getSharedPreferences(VIDYO_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void store(String key, boolean value) {
        sInstance.edit().putBoolean(key, value).apply();
    }

    public static boolean get(String key, boolean def) {
        return sInstance.getBoolean(key, def);
    }
}
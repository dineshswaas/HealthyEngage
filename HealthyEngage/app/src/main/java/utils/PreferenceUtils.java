package utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

import models.CarePlanModels;

public class PreferenceUtils {

    private static final String HEALTHY_ENGAGE = "HEALTHY_ENGAGE";
    private static final String Authorization = "Authorization";
    private static final String PATIENT_ID = "PATIENT_ID";
    private static final String CARE_PLAN_ID = "CARE_PLAN_ID";
    private static final String DELEGATE_ID = "DELEGATE_ID";
    private static final String CARE_PLAN_LIST = "CARE_PLAN_LIST";

    public static String getAuthorizationKey(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString(Authorization, "");
        return mode;
    }

    public static void setAuthorizationKey(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Authorization, value);
        editor.commit();
    }


    public static String getPatientId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString(PATIENT_ID, "");
        return mode;
    }

    public static void setPatientId(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PATIENT_ID, value);
        editor.commit();
    }


    public static String getCarePlanId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString(CARE_PLAN_ID, "");
        return mode;
    }

    public static void setCarePlanId(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CARE_PLAN_ID, value);
        editor.commit();
    }

    public static String getDelegateId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString(DELEGATE_ID, "");
        return mode;
    }

    public static void setDelegateId(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DELEGATE_ID, value);
        editor.commit();
    }


    public static void setCarePlanList(Context context, List<CarePlanModels> carePlanModelsList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        if (carePlanModelsList != null) {
            String doctorObject = gson.toJson(carePlanModelsList);
            editor.putString(CARE_PLAN_LIST, doctorObject);
        } else {
            editor.putString(CARE_PLAN_LIST, null);
        }
        editor.commit();
    }

    public static String getCarePlanList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        String doctorJsonString = sharedPreferences.getString(CARE_PLAN_LIST, null);
        return doctorJsonString;
    }
}

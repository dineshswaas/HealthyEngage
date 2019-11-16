package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.swaas.healthyengage.R;

import java.util.List;

import models.CarePlanModels;

public class PreferenceUtils {

    private static final String HEALTHY_ENGAGE = "HEALTHY_ENGAGE";
    private static final String Authorization = "Authorization";
    private static final String PATIENT_ID = "PATIENT_ID";
    private static final String CARE_PLAN_ID = "CARE_PLAN_ID";
    private static final String DELEGATE_ID = "DELEGATE_ID";
    private static final String CARE_PLAN_LIST = "CARE_PLAN_LIST";
    private static final String LAST_SYNC_DATE = "LAST_SYNC_DATE";
    private static final String USER_ID = "USER_ID";
    private static final String UUID = "UUID";
    private static final String LOGIN_MOBILE_NUMBER = "LOGIN_MOBILE_NUMBER";
    private static final String USER_NAME = "USER_NAME";
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

    public static String getLastSyncDate(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString(LAST_SYNC_DATE, "");
        return mode;
    }

    public static void setLastSyncDate(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_SYNC_DATE, value);
        editor.commit();
    }
    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString(USER_ID, "");
        return mode;
    }

    public static void setUserId(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, value);
        editor.commit();
    }


    public static String getUuid(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString(UUID, "");
        return mode;
    }

    public static void setUuid(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UUID, value);
        editor.commit();
    }

    public static String getLoginMobileNumber(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString(LOGIN_MOBILE_NUMBER, "");
        return mode;
    }

    public static void setUserName(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, value);
        editor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString(USER_NAME, "");
        return mode;
    }

    public static void setLoginMobileNumber(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(HEALTHY_ENGAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_MOBILE_NUMBER, value);
        editor.commit();
    }



    public static void clearAllData(Context mContext){
        PreferenceUtils.setCarePlanList(mContext,null);
        PreferenceUtils.setUserId(mContext,null);
        PreferenceUtils.setLoginMobileNumber(mContext,null);
        PreferenceUtils.setPatientId(mContext,null);
        PreferenceUtils.setDelegateId(mContext,null);
        PreferenceUtils.setCarePlanId(mContext,null);
    }

    public static String GetCountryZipCode(Context context,String cCode){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
       // CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=context.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(cCode.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }
}

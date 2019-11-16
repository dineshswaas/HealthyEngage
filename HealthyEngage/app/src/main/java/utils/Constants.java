package utils;

import retrofit.http.PUT;

public class Constants {

    // ***************************** Production ********************************************
  //  public static final String COMPANY_BASE_URL = "https://www.healthyengage.net/"; // Prod
    public static final String COMPANY_BASE_URL = "http://18.191.216.174:3000/"; // DEV

    public static final String AUTHY_API_URL = "https://api.authy.com/";
    public static final String AUTHY_API_KEY="X-Authy-API-Key";
    public static final String AUTHY_API_VALUE = "yINoSYAbwIhZ11jNqGZ690BgbslJe7j2";

    public static final String INTENT_PARM = "INTENT_PARM";
    public static final String INPUT_SLIDER = "Slider";
    public static final String INPUT_TEXT = "Text";
    public static final String INPUT_BOOLEAN = "Boolean";
    public static final String INPUT_PICKER ="Picker";
    public static final String DEFAULT_SERVER_DATE = "1970-01-01T00:00:00.000Z";


    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

    public static final String COLOR_ORANGE="#F7941D";
    public static final String COLOR_PURPLE="#A71930";


}

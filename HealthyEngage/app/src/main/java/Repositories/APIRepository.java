package Repositories;

import android.content.Context;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.swaas.healthyengage.LoginWithMobileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import APIServices.APIServices;
import APIServices.RetrofitAPIBuilder;
import models.APIResponseModels;
import models.CarePlanModels;
import models.ConnectAPIModel;
import models.Delegates;
import models.PatientMessageAPIModel;
import models.PatientMessageModels;
import models.UserVerifyModel;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import utils.Constants;
import utils.NetworkUtils;
import utils.PreferenceUtils;

public class APIRepository {

    private Context mContext;
    private GetCarePlanModelDetails getCarePlanModelDetails;
    private GerPatientMessages gerPatientMessages;
    private GetCareTakersDetails getCareTakersDetails;
    private GetAPIResponseModel getAPIResponseModel;
    private GetUserVerifyModel getUserVerifyModel;
    private GetDelegateDetails getDelegateDetails;
  public APIRepository(Context context){
    this.mContext = context;
  }

public void getCarePlanDetails(CarePlanModels carePlanModels){
    if(NetworkUtils.isNetworkAvailable(mContext)){
        Retrofit retrofit = RetrofitAPIBuilder.getInstance();
        APIServices carePlanServices =retrofit.create(APIServices.class);

        Call call =carePlanServices.getCarePlanDetails(PreferenceUtils.getAuthorizationKey(mContext),
                carePlanModels.getCarePlanId(),carePlanModels.getPatientId(),carePlanModels);
        call.enqueue(new Callback<APIResponseModels<CarePlanModels>>() {
            @Override
            public void onResponse(Response<APIResponseModels<CarePlanModels>> response, Retrofit retrofit) {
            APIResponseModels apiResponseModels =response.body();
            if(apiResponseModels != null){
                if(apiResponseModels.getCareplan() != null){
                    getCarePlanModelDetails.getCarePlanSuccess(apiResponseModels.getCareplan(),apiResponseModels.getLastSyncDate());
                }else if(apiResponseModels.getError() != null){
                    getCarePlanModelDetails.getCarePlanSuccess(apiResponseModels.getCareplan(),apiResponseModels.getLastSyncDate());
                }
            }else{
                getCarePlanModelDetails.getCarePlanFailure("No Care plan is assigned to this patient");
                PreferenceUtils.clearAllData(mContext);
            }
            }

            @Override
            public void onFailure(Throwable t) {
                getCarePlanModelDetails.getCarePlanFailure(t.getMessage());
            }
        });

    }
}

    public void setGetCarePlanDetails(GetCarePlanModelDetails getCarePlanDetails){
        this.getCarePlanModelDetails = getCarePlanDetails;

    }
    public interface GetCarePlanModelDetails{

        void getCarePlanSuccess(List<CarePlanModels> carePlanModels,String lastSyncDate);
        void getCarePlanFailure(String s);
    }



    /* Patient Messages*/

    public void getPatientMessages(PatientMessageModels patientMessageModels){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            APIServices carePlanServices =retrofit.create(APIServices.class);
            Call call =carePlanServices.getPatientMessages(PreferenceUtils.getAuthorizationKey(mContext),
                    PreferenceUtils.getPatientId(mContext),patientMessageModels);
            call.enqueue(new Callback<PatientMessageAPIModel<CarePlanModels>>() {
                @Override
                public void onResponse(Response<PatientMessageAPIModel<CarePlanModels>> response, Retrofit retrofit) {
                    PatientMessageAPIModel apiResponseModels =response.body();
                    if(apiResponseModels != null){
                        if(apiResponseModels.getPatientMessages() != null){
                            gerPatientMessages.getPatientMessages(apiResponseModels.getPatientMessages());
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    gerPatientMessages.getPatientMessagesFailure(t.getMessage());
                }
            });

        }
    }

    public void setGetPatientMessage(GerPatientMessages gerPatientMessages){
        this.gerPatientMessages = gerPatientMessages;
    }

    public interface GerPatientMessages{
        void getPatientMessages(List<PatientMessageModels> carePlanModels);
        void getPatientMessagesFailure(String s);
    }


    /*Connect */

    public void getCareTakers(ConnectAPIModel connectAPIModel){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            APIServices carePlanServices =retrofit.create(APIServices.class);
            Call call =carePlanServices.getCareTakers(PreferenceUtils.getAuthorizationKey(mContext),
                    PreferenceUtils.getPatientId(mContext),connectAPIModel);
            call.enqueue(new Callback<ConnectAPIModel<ConnectAPIModel>>() {
                @Override
                public void onResponse(Response<ConnectAPIModel<ConnectAPIModel>> response, Retrofit retrofit) {
                    ConnectAPIModel apiResponseModels =response.body();
                    if(apiResponseModels != null){
                        if(apiResponseModels != null){
                            getCareTakersDetails.getCareTakersSuccess(apiResponseModels);
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    getCareTakersDetails.getCareTakersFailure(t.getMessage());
                }
            });

        }
    }


    public void getRelationDetails(){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            APIServices carePlanServices =retrofit.create(APIServices.class);
            Call call =carePlanServices.getRelationDetails(PreferenceUtils.getAuthorizationKey(mContext));
            call.enqueue(new Callback<ConnectAPIModel<ConnectAPIModel>>() {
                @Override
                public void onResponse(Response<ConnectAPIModel<ConnectAPIModel>> response, Retrofit retrofit) {
                    ConnectAPIModel apiResponseModels =response.body();
                    if(apiResponseModels != null){
                        if(apiResponseModels != null){
                            getCareTakersDetails.getCareTakersSuccess(apiResponseModels);
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    getCareTakersDetails.getCareTakersFailure(t.getMessage());
                }
            });

        }
    }


    public void setGetCareTakersDetails(GetCareTakersDetails getCareTakersDetails){
        this.getCareTakersDetails = getCareTakersDetails;
    }

    public interface GetCareTakersDetails{

        void getCareTakersSuccess(ConnectAPIModel connectAPIModel);
        void getCareTakersFailure(String s);

    }


    /*UPDATE INTERVENTION*/

    public void updateIntervention(CarePlanModels.CarePlanIntervention.InterventionFrequency frequency){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            APIServices carePlanServices =retrofit.create(APIServices.class);
            Call call =carePlanServices.updateCarePlanIntervention(PreferenceUtils.getAuthorizationKey(mContext),
                    frequency);
            call.enqueue(new Callback<APIResponseModels>() {
                @Override
                public void onResponse(Response<APIResponseModels> response, Retrofit retrofit) {
                    APIResponseModels apiResponseModels =response.body();
                    if(apiResponseModels != null){
                        if(apiResponseModels != null){
                            getAPIResponseModel.getAPIResponseModelSuccess(apiResponseModels);
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    getAPIResponseModel.getAPIResponseModelFailure(t.getMessage());
                }
            });

        }
    }

    /*Care Plan Assessment*/

    public void updateAssessment(CarePlanModels.CarePlanAssessment patientAssessment){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            APIServices carePlanServices =retrofit.create(APIServices.class);
            Call call =carePlanServices.updateCarePlanAssessment(PreferenceUtils.getAuthorizationKey(mContext),
                    patientAssessment);
            call.enqueue(new Callback<APIResponseModels>() {
                @Override
                public void onResponse(Response<APIResponseModels> response, Retrofit retrofit) {
                    APIResponseModels apiResponseModels =response.body();
                    if(apiResponseModels != null){
                        if(apiResponseModels != null){
                            getAPIResponseModel.getAPIResponseModelSuccess(apiResponseModels);
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    getAPIResponseModel.getAPIResponseModelFailure(t.getMessage());
                }
            });

        }
    }



    public void setGetAPIResponseModel(GetAPIResponseModel getAPIResponseModel){
        this.getAPIResponseModel = getAPIResponseModel;
    }

    public interface GetAPIResponseModel{

        void getAPIResponseModelSuccess(APIResponseModels apiResponseModels);
        void getAPIResponseModelFailure(String s);

    }

/*User verify mobile*/

    public void verifyMobile(UserVerifyModel userVerifyModel){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            APIServices carePlanServices =retrofit.create(APIServices.class);
            Call call =carePlanServices.patientVerifyMobile(userVerifyModel);
            call.enqueue(new Callback<UserVerifyModel>() {
                @Override
                public void onResponse(Response<UserVerifyModel> response, Retrofit retrofit) {
                    UserVerifyModel apiResponseModels =response.body();
                    if(apiResponseModels != null){
                        getUserVerifyModel.getUserVerifyModelSuccess(apiResponseModels);
                    }else{

                        getUserVerifyModel.getUserVerifyModelFailure("No Patient attached to this number.");
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                getUserVerifyModel.getUserVerifyModelFailure(t.getMessage());
                }
            });

        }
    }


    public void sendOTPToMobile(UserVerifyModel userVerifyModel){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            String url = "https://api.authy.com/protected/json/phones/verification/start?via=sms&phone_number="
                    +userVerifyModel.getMobileNo()+"&country_code="+userVerifyModel.getCountry_code()+"&locale=en";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        UserVerifyModel userVerifyModel = new UserVerifyModel();
                        userVerifyModel.setCarrier(jsonResponse.getString("carrier"));
                        userVerifyModel.setIs_cellphone(jsonResponse.getBoolean("is_cellphone"));
                        userVerifyModel.setMessage(jsonResponse.getString("message"));
                        userVerifyModel.setUuid(jsonResponse.getString("uuid"));
                        userVerifyModel.setStatus(jsonResponse.getBoolean("success"));
                        getUserVerifyModel.getUserVerifyModelSuccess(userVerifyModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.d("par,",error.getMessage());
                    String responseBody = null;
                    try {
                        responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    //JSONArray errors = data.getJSONArray("errors");
                   // JSONObject jsonMessage = errors.getJSONObject(0);
                    String message = data.getString("message");
                    getUserVerifyModel.getUserVerifyModelFailure(message);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        getUserVerifyModel.getUserVerifyModelFailure("Something went wrong.");
                    } catch (JSONException e) {
                        getUserVerifyModel.getUserVerifyModelFailure("Something went wrong.");
                        e.printStackTrace();
                    }
                }
            }){
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    headers.put(Constants.AUTHY_API_KEY, Constants.AUTHY_API_VALUE);
                    return headers;
                }
            };

            requestQueue.add(stringRequest);

        }
    }


    /*Send otp*/

    public void verifyOTP(UserVerifyModel userVerifyModel){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            String url = "https://api.authy.com/protected/json/phones/verification/check?phone_number="+userVerifyModel.getMobileNo()+"" +
                    "&country_code="+userVerifyModel.getCountry_code()+"&verification_code="+userVerifyModel.getVerificationCode();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        UserVerifyModel userVerifyModel = new UserVerifyModel();
                        userVerifyModel.setMessage(jsonResponse.getString("message"));
                        userVerifyModel.setSuccess(jsonResponse.getBoolean("success"));
                        getUserVerifyModel.getUserVerifyModelSuccess(userVerifyModel);
                    } catch (JSONException e) {
                        getUserVerifyModel.getUserVerifyModelFailure("Verification code is incorrect");
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String responseBody = null;
                    try {
                        responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        UserVerifyModel userVerifyModel = new UserVerifyModel();
                        userVerifyModel.setMessage(data.getString("message"));
                        userVerifyModel.setSuccess(data.getBoolean("success"));
                        if(data.getString("error_code").equalsIgnoreCase("60000")){
                            userVerifyModel.setSuccess(true);
                        }
                        getUserVerifyModel.getUserVerifyModelSuccess(userVerifyModel);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        getUserVerifyModel.getUserVerifyModelFailure("Something went wrong.");
                    } catch (JSONException e) {
                        getUserVerifyModel.getUserVerifyModelFailure("Something went wrong.");
                        e.printStackTrace();
                    }
                }
            }){
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    headers.put(Constants.AUTHY_API_KEY, Constants.AUTHY_API_VALUE);
                    return headers;
                }
            };

            requestQueue.add(stringRequest);

        }
    }

    public void getPatientDetails(UserVerifyModel userVerifyModel){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            APIServices carePlanServices =retrofit.create(APIServices.class);
            Call call =carePlanServices.checkPatientVerify(userVerifyModel);
            call.enqueue(new Callback<APIResponseModels>() {
                @Override
                public void onResponse(Response<APIResponseModels> response, Retrofit retrofit) {
                    APIResponseModels apiResponseModels =response.body();
                    if(apiResponseModels != null){
                        getAPIResponseModel.getAPIResponseModelSuccess(apiResponseModels);
                    }else{
                        getAPIResponseModel.getAPIResponseModelFailure("No Care Plan Assigned");
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    getAPIResponseModel.getAPIResponseModelFailure(t.getMessage());
                }
            });

        }
    }



    public void submitDelegate(Delegates delegates){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            APIServices carePlanServices =retrofit.create(APIServices.class);
            Call call =carePlanServices.addDelegate(PreferenceUtils.getAuthorizationKey(mContext),delegates);
            call.enqueue(new Callback<APIResponseModels>() {
                @Override
                public void onResponse(Response<APIResponseModels> response, Retrofit retrofit) {
                    APIResponseModels apiResponseModels =response.body();
                    getAPIResponseModel.getAPIResponseModelSuccess(apiResponseModels);
                }

                @Override
                public void onFailure(Throwable t) {
                    getAPIResponseModel.getAPIResponseModelFailure(t.getMessage());
                }
            });

        }
    }


    public void updateDelegate(Delegates delegates){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            APIServices carePlanServices =retrofit.create(APIServices.class);
            Call call =carePlanServices.updateDelegate(PreferenceUtils.getAuthorizationKey(mContext),delegates);
            call.enqueue(new Callback<APIResponseModels>() {
                @Override
                public void onResponse(Response<APIResponseModels> response, Retrofit retrofit) {
                    APIResponseModels apiResponseModels =response.body();
                    getAPIResponseModel.getAPIResponseModelSuccess(apiResponseModels);
                }

                @Override
                public void onFailure(Throwable t) {
                    getAPIResponseModel.getAPIResponseModelFailure(t.getMessage());
                }
            });

        }
    }

    public void gethippa(){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            APIServices carePlanServices =retrofit.create(APIServices.class);
            Call call =carePlanServices.acknowledgement(PreferenceUtils.getAuthorizationKey(mContext));
            call.enqueue(new Callback<APIResponseModels>() {
                @Override
                public void onResponse(Response<APIResponseModels> response, Retrofit retrofit) {
                    APIResponseModels apiResponseModels =response.body();
                    getAPIResponseModel.getAPIResponseModelSuccess(apiResponseModels);
                }

                @Override
                public void onFailure(Throwable t) {
                    getAPIResponseModel.getAPIResponseModelFailure(t.getMessage());
                }
            });

        }
    }
    public void updateHippa(Delegates delegates){
        if(NetworkUtils.isNetworkAvailable(mContext)){
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            APIServices carePlanServices =retrofit.create(APIServices.class);
            Call call =carePlanServices.submitHippa(PreferenceUtils.getAuthorizationKey(mContext),delegates);
            call.enqueue(new Callback<APIResponseModels>() {
                @Override
                public void onResponse(Response<APIResponseModels> response, Retrofit retrofit) {
                    APIResponseModels apiResponseModels =response.body();
                    getAPIResponseModel.getAPIResponseModelSuccess(apiResponseModels);
                }

                @Override
                public void onFailure(Throwable t) {
                    getAPIResponseModel.getAPIResponseModelFailure(t.getMessage());
                }
            });

        }
    }



    public void setGetDelegateDetails( GetDelegateDetails getDelegateDetails){
        this.getDelegateDetails = getDelegateDetails;
    }
    public interface GetDelegateDetails{
        void getSuccess(Delegates delegates);
        void getFailure(String s);
    }

    public void setGetUserVerifyModel(GetUserVerifyModel getUserVerifyModel){
        this.getUserVerifyModel = getUserVerifyModel;
    }

    public interface GetUserVerifyModel{

        void getUserVerifyModelSuccess(UserVerifyModel apiResponseModels);
        void getUserVerifyModelFailure(String s);

    }
}

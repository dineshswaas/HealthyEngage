package com.swaas.healthyengage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Alerts.IOSDialog;
import Alerts.IOSDialogBuilder;
import Alerts.IOSDialogClickListener;
import Repositories.APIRepository;
import models.APIResponseModels;
import models.UserVerifyModel;
import utils.NetworkUtils;
import utils.PreferenceUtils;

public class OTPScreenActivity extends AppCompatActivity {

    EditText editText;
    ProgressDialog progressDialog;
    UserVerifyModel userVerifyModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpscreen);
        getSupportActionBar().hide();
        editText = (EditText)findViewById(R.id.editText);
        userVerifyModel = new UserVerifyModel();
    }

    public void verifyOTP(View view) {
        if(NetworkUtils.isNetworkAvailable(this)){
            if(!TextUtils.isEmpty(editText.getText().toString().trim())){
                if(NetworkUtils.isNetworkAvailable(this)){
                    sendOTPVerification(editText.getText().toString().trim());
                }
            }else{
                showAlertMessage("Enter your verification code");
            }
        }


    }

    private void sendOTPVerification(String otp) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verification in progress.");
        progressDialog.show();
        APIRepository apiRepository = new APIRepository(this);
        apiRepository.setGetUserVerifyModel(new APIRepository.GetUserVerifyModel() {
        @Override
        public void getUserVerifyModelSuccess(UserVerifyModel apiResponseModels) {
            if(apiResponseModels.isSuccess()){
                getPatientDetails();
            }else{
                progressDialog.hide();
                showAlertMessage(apiResponseModels.getMessage());
            }
        }

        @Override
        public void getUserVerifyModelFailure(String s) {
            showAlertMessage("Something went wrong");
            progressDialog.hide();
        }
    });
        userVerifyModel.setMobileNo(PreferenceUtils.getLoginMobileNumber(this));
        userVerifyModel.setVerificationCode(otp);
        String cCode = Locale.getDefault().getCountry();
        userVerifyModel.setCountry_code(PreferenceUtils.GetCountryZipCode(this,cCode));
        apiRepository.verifyOTP(userVerifyModel);
    }

    private void getPatientDetails() {

    APIRepository apiRepository = new APIRepository(this);
    apiRepository.setGetAPIResponseModel(new APIRepository.GetAPIResponseModel() {
        @Override
        public void getAPIResponseModelSuccess(APIResponseModels apiResponseModels) {
            if(apiResponseModels != null){
                progressDialog.hide();
                moveToHome(apiResponseModels);
            }
        }

        @Override
        public void getAPIResponseModelFailure(String s) {
            progressDialog.hide();
            Toast.makeText(OTPScreenActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    });

        userVerifyModel.setMobile_no(PreferenceUtils.getLoginMobileNumber(this));
        String cCode = Locale.getDefault().getCountry();
        userVerifyModel.setCountry_code(PreferenceUtils.GetCountryZipCode(this,cCode));
        userVerifyModel.setToken(FirebaseInstanceId.getInstance().getToken());
    apiRepository.getPatientDetails(userVerifyModel);
    }

    private void moveToHome(APIResponseModels apiResponseModels) {
        APIResponseModels.AccessToken accessToken = apiResponseModels.getAccessToken();
        PreferenceUtils.setAuthorizationKey(OTPScreenActivity.this,accessToken.getId());
        PreferenceUtils.setUserId(OTPScreenActivity.this,accessToken.getUserId());
        PreferenceUtils.setCarePlanId(OTPScreenActivity.this,apiResponseModels.getCareplanId());
        PreferenceUtils.setPatientId(OTPScreenActivity.this,apiResponseModels.getPatientId());
        PreferenceUtils.setDelegateId(OTPScreenActivity.this,apiResponseModels.getDelegateId());
        PreferenceUtils.setLastSyncDate(OTPScreenActivity.this,apiResponseModels.getLastSyncDate());

        if(!TextUtils.isEmpty(apiResponseModels.getDelegateId()) && !apiResponseModels.isIs_hipaa_signed()){
                startActivity(new Intent(OTPScreenActivity.this,HippaActivity.class));
        }else{
            startActivity(new Intent(OTPScreenActivity.this,HomePageActivity.class));
        }



    }

    void showAlertMessage(String message){
        new IOSDialogBuilder(this)
                .setTitle("Alert")
                .setSubtitle(message)
                .setBoldPositiveLabel(false)
                .setCancelable(false)
                .setSingleButtonView(true)
                .setPositiveListener("",null)
                .setNegativeListener("",null)
                .setSinglePositiveListener("OK", new IOSDialogClickListener() {
                    @Override
                    public void onClick(IOSDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build().show();

    }

}

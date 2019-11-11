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
import java.util.Map;

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
                Toast.makeText(this,"Enter your verification code",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(OTPScreenActivity.this,apiResponseModels.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void getUserVerifyModelFailure(String s) {
            Toast.makeText(OTPScreenActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            progressDialog.hide();
        }
    });
        userVerifyModel.setMobileNo(PreferenceUtils.getLoginMobileNumber(this));
        userVerifyModel.setVerificationCode(otp);
        apiRepository.verifyOTP(userVerifyModel);
    }

    private void getPatientDetails() {

    APIRepository apiRepository = new APIRepository(this);
    apiRepository.setGetAPIResponseModel(new APIRepository.GetAPIResponseModel() {
        @Override
        public void getAPIResponseModelSuccess(APIResponseModels apiResponseModels) {
            if(apiResponseModels != null){
                progressDialog.hide();
                APIResponseModels.AccessToken accessToken = apiResponseModels.getAccessToken();
                PreferenceUtils.setAuthorizationKey(OTPScreenActivity.this,accessToken.getId());
                PreferenceUtils.setUserId(OTPScreenActivity.this,accessToken.getUserId());
            PreferenceUtils.setCarePlanId(OTPScreenActivity.this,apiResponseModels.getCareplanId());
            PreferenceUtils.setPatientId(OTPScreenActivity.this,apiResponseModels.getPatientId());
            PreferenceUtils.setDelegateId(OTPScreenActivity.this,apiResponseModels.getDelegateId());
            PreferenceUtils.setLastSyncDate(OTPScreenActivity.this,apiResponseModels.getLastSyncDate());
            startActivity(new Intent(OTPScreenActivity.this,HomePageActivity.class));
            }
        }

        @Override
        public void getAPIResponseModelFailure(String s) {
            progressDialog.hide();
            Toast.makeText(OTPScreenActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    });

        userVerifyModel.setMobile_no(PreferenceUtils.getLoginMobileNumber(this));
        userVerifyModel.setCountry_code("91");
        userVerifyModel.setToken(FirebaseInstanceId.getInstance().getToken());
    apiRepository.getPatientDetails(userVerifyModel);
    }
}

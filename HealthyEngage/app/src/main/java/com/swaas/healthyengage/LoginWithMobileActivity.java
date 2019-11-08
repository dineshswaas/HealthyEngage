package com.swaas.healthyengage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import Repositories.APIRepository;
import models.UserVerifyModel;
import utils.NetworkUtils;
import utils.PreferenceUtils;

public class LoginWithMobileActivity extends AppCompatActivity {

    EditText editText;
    ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loing_with_mobile);
        getSupportActionBar().hide();
        editText = (EditText)findViewById(R.id.editText);
    }

    public void gotoVerifyMobile(View view) {
        if(NetworkUtils.isNetworkAvailable(this)){
            if(!TextUtils.isEmpty(editText.getText().toString().trim())){
                verifyEnteredMobile(editText.getText().toString().trim());
            }else{
                Toast.makeText(this,"Enter your mobile number.",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void verifyEnteredMobile(String mobile) {
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Verifying your mobile number");
        progressBar.show();
        APIRepository apiRepository = new APIRepository(this);
        apiRepository.setGetUserVerifyModel(new APIRepository.GetUserVerifyModel() {
            @Override
            public void getUserVerifyModelSuccess(UserVerifyModel apiResponseModels) {
                UserVerifyModel userVerifyModel = apiResponseModels;
                if(userVerifyModel.isStatus()){
                    PreferenceUtils.setLoginMobileNumber(LoginWithMobileActivity.this,editText.getText().toString().trim());
                    sendOTPToPatient(editText.getText().toString().trim());
                }else{
                    Toast.makeText(LoginWithMobileActivity.this,"Something went wrong.",Toast.LENGTH_LONG).show();
                    PreferenceUtils.setLoginMobileNumber(LoginWithMobileActivity.this,"");
                }

            }

            @Override
            public void getUserVerifyModelFailure(String s) {
                Toast.makeText(LoginWithMobileActivity.this,s,Toast.LENGTH_LONG).show();
                progressBar.hide();
            }
        });
        UserVerifyModel userVerifyModel = new UserVerifyModel();
        userVerifyModel.setMobileNo(mobile);
        apiRepository.verifyMobile(userVerifyModel);
    }


    private void sendOTPToPatient(String mobileNumber) {

        APIRepository apiRepository = new APIRepository(this);
        UserVerifyModel userVerifyModel = new UserVerifyModel();
        userVerifyModel.setMobileNo(mobileNumber);
        apiRepository.setGetUserVerifyModel(new APIRepository.GetUserVerifyModel() {
            @Override
            public void getUserVerifyModelSuccess(UserVerifyModel apiResponseModels) {
                if(apiResponseModels != null && apiResponseModels.isStatus()){
                    progressBar.hide();
                    PreferenceUtils.setUuid(LoginWithMobileActivity.this,apiResponseModels.getUuid());
                    startActivity(new Intent(LoginWithMobileActivity.this,OTPScreenActivity.class));
                }else{
                    Toast.makeText(LoginWithMobileActivity.this,"Account suspended: too many attempts",Toast.LENGTH_SHORT).show();
                    progressBar.hide();
                }
            }

            @Override
            public void getUserVerifyModelFailure(String s) {
                Toast.makeText(LoginWithMobileActivity.this,s,Toast.LENGTH_SHORT).show();
                progressBar.hide();
            }
        });
        apiRepository.sendOTPToMobile(userVerifyModel);


    }

}

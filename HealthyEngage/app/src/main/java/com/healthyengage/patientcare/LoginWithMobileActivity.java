package com.healthyengage.patientcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.Locale;

import Alerts.IOSDialog;
import Alerts.IOSDialogBuilder;
import Alerts.IOSDialogClickListener;
import Repositories.APIRepository;
import models.UserVerifyModel;
import utils.NetworkUtils;
import utils.PreferenceUtils;

public class LoginWithMobileActivity extends AppCompatActivity {

    EditText editText;
    ProgressDialog progressBar;
    private SQLiteDatabase database = null;
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
                showAlertMessage("Enter your registered mobile number");
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
                    //getPatientDetails();
                }else{
                    showAlertMessage("Something went wrong.please try again");
                    PreferenceUtils.setLoginMobileNumber(LoginWithMobileActivity.this,"");
                }

            }

            @Override
            public void getUserVerifyModelFailure(String s) {
                showAlertMessage(s);
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
                    showAlertMessage("Account suspended: too many attempts");
                   // Toast.makeText(LoginWithMobileActivity.this,"Account suspended: too many attempts",Toast.LENGTH_SHORT).show();
                    progressBar.hide();
                }
            }

            @Override
            public void getUserVerifyModelFailure(String s) {
                showAlertMessage(s);
                //Toast.makeText(LoginWithMobileActivity.this,s,Toast.LENGTH_SHORT).show();
                progressBar.hide();
            }
        });

        String cCode = Locale.getDefault().getCountry();
        userVerifyModel.setCountry_code(PreferenceUtils.GetCountryZipCode(this,cCode));
        apiRepository.sendOTPToMobile(userVerifyModel);

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

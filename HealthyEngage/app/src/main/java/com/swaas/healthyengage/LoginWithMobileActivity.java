package com.swaas.healthyengage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import DataBase.DatabaseHandler;
import Repositories.APIRepository;
import models.APIResponseModels;
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
                    //getPatientDetails();
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




    private void getPatientDetails() {

        APIRepository apiRepository = new APIRepository(this);
        apiRepository.setGetAPIResponseModel(new APIRepository.GetAPIResponseModel() {
            @Override
            public void getAPIResponseModelSuccess(APIResponseModels apiResponseModels) {
                if(apiResponseModels != null){
                    progressBar.hide();
                    //openOrCreateDatabaseCustom();
                    APIResponseModels.AccessToken accessToken = apiResponseModels.getAccessToken();
                    PreferenceUtils.setAuthorizationKey(LoginWithMobileActivity.this,accessToken.getId());
                    PreferenceUtils.setUserId(LoginWithMobileActivity.this,accessToken.getUserId());
                    PreferenceUtils.setCarePlanId(LoginWithMobileActivity.this,apiResponseModels.getCareplanId());
                    PreferenceUtils.setPatientId(LoginWithMobileActivity.this,apiResponseModels.getPatientId());
                    PreferenceUtils.setDelegateId(LoginWithMobileActivity.this,apiResponseModels.getDelegateId());
                    PreferenceUtils.setLastSyncDate(LoginWithMobileActivity.this,apiResponseModels.getLastSyncDate());
                    startActivity(new Intent(LoginWithMobileActivity.this,HomePageActivity.class));
                }
            }

            @Override
            public void getAPIResponseModelFailure(String s) {
                progressBar.hide();
                Toast.makeText(LoginWithMobileActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
        UserVerifyModel userVerifyModel = new UserVerifyModel();
        userVerifyModel.setMobile_no(PreferenceUtils.getLoginMobileNumber(this));
        userVerifyModel.setCountry_code("91");
        userVerifyModel.setToken(FirebaseInstanceId.getInstance().getToken());
        apiRepository.getPatientDetails(userVerifyModel);
    }

    private void openOrCreateDatabaseCustom() {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        database = databaseHandler.getWritableDatabase();
        databaseHandler.onCreate(database);
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

        String cCode = Locale.getDefault().getCountry();
        userVerifyModel.setCountry_code(PreferenceUtils.GetCountryZipCode(this,cCode));
        apiRepository.sendOTPToMobile(userVerifyModel);


    }

}

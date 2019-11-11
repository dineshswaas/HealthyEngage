package com.swaas.healthyengage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import java.util.Locale;

import utils.PreferenceUtils;

public class TimeOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_out);
        getSupportActionBar().hide();
        if(!TextUtils.isEmpty(PreferenceUtils.getLoginMobileNumber(this)) &&
                !TextUtils.isEmpty(PreferenceUtils.getCarePlanId(this)) &&
                !TextUtils.isEmpty(PreferenceUtils.getPatientId(this)) &&
                !TextUtils.isEmpty(PreferenceUtils.getUserId(this))){
            startActivity(new Intent(this, HomePageActivity.class));
        }

    }

    public void gotonext(View view) {
            startActivity(new Intent(this, LoginWithMobileActivity.class));

    }
}

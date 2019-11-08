package com.swaas.healthyengage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TimeOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_out);
        getSupportActionBar().hide();
    }

    public void gotonext(View view) {
        startActivity(new Intent(this, LoginWithMobileActivity.class));
    }
}

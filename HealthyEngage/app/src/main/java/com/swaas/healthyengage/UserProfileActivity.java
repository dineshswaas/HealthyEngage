package com.swaas.healthyengage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class UserProfileActivity extends AppCompatActivity {

    TextView nameValue,lastnameValue,sexValue,dobValue,emailValue;
    MaskedEditText mobileValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initializeViews();
    }

    private void initializeViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("User Profile");

    }

}

package com.swaas.healthyengage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoingWithMobileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loing_with_mobile);
        getSupportActionBar().hide();
    }

    public void gonext(View view) {
        startActivity(new Intent(this,HomePageActivity.class));
    }
}

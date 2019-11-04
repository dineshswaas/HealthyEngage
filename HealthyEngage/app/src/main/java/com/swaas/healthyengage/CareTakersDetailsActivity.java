package com.swaas.healthyengage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import models.ConnectAPIModel;
import utils.Constants;

public class CareTakersDetailsActivity extends AppCompatActivity {

    TextView firstheaderletter,name,phonenumber,textnumber,emailtext;
    ConnectAPIModel connectAPIModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_takers_details);
        initializeViews();
        getIntentData();
    }

    private void getIntentData() {

        connectAPIModel = (ConnectAPIModel) getIntent().getSerializableExtra(Constants.INTENT_PARM);
        firstheaderletter.setText(connectAPIModel.getFirst_name().charAt(0) +" "+connectAPIModel.getLast_name().charAt(0));
        name.setText(connectAPIModel.getFirst_name()+""+connectAPIModel.getLast_name());
        if(TextUtils.isEmpty(connectAPIModel.getEmergency_number())){
            phonenumber.setText(connectAPIModel.getMobile_no());
        }else{
            phonenumber.setText(connectAPIModel.getEmergency_number());
        }

        textnumber.setText(connectAPIModel.getMobile_no());
        emailtext.setText(connectAPIModel.getEmail());

    }

    private void initializeViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        firstheaderletter = (TextView)findViewById(R.id.firstheaderletter);
        name = (TextView)findViewById(R.id.name);
        phonenumber = (TextView)findViewById(R.id.phonenumber);
        textnumber = (TextView)findViewById(R.id.textnumber);
        emailtext = (TextView)findViewById(R.id.emailtext);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

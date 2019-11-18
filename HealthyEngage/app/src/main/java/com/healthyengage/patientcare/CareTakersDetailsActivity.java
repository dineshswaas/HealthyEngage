package com.healthyengage.patientcare;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import models.ConnectAPIModel;
import utils.Constants;
import utils.NetworkUtils;

public class CareTakersDetailsActivity extends AppCompatActivity {

    TextView firstheaderletter,name,phonenumber,textnumber,emailtext,others;
    ConnectAPIModel connectAPIModel;
    int Update_Request_code=91;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_takers_details);
        initializeViews();
        getIntentData();
    }

    private void getIntentData() {

        connectAPIModel = (ConnectAPIModel) getIntent().getSerializableExtra(Constants.INTENT_PARM);
        if(TextUtils.isEmpty(connectAPIModel.getLast_name())){
            firstheaderletter.setText(connectAPIModel.getFirst_name().trim().charAt(0)+"");
            name.setText(connectAPIModel.getFirst_name());
        }else{
            firstheaderletter.setText(connectAPIModel.getFirst_name().charAt(0) +" "+connectAPIModel.getLast_name().charAt(0));
            name.setText(connectAPIModel.getFirst_name()+" "+connectAPIModel.getLast_name());
        }


        if(TextUtils.isEmpty(connectAPIModel.getEmergency_number())){
            phonenumber.setText(connectAPIModel.getMobile_no());
        }else{
            phonenumber.setText(connectAPIModel.getEmergency_number());
        }
        if(connectAPIModel.isIs_Delegate()){
            findViewById(R.id.textMainLayout).setVisibility(View.GONE);
            findViewById(R.id.secondDivider).setVisibility(View.GONE);
            findViewById(R.id.emailMainLayout).setVisibility(View.GONE);
        }
        textnumber.setText(connectAPIModel.getMobile_no());
        emailtext.setText(connectAPIModel.getEmail());
        if(connectAPIModel.isIs_Delegate()){
            others.setText(connectAPIModel.getRelationName());
        }

    }

    private void initializeViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Connect");
        firstheaderletter = (TextView)findViewById(R.id.firstheaderletter);
        name = (TextView)findViewById(R.id.name);
        phonenumber = (TextView)findViewById(R.id.phonenumber);
        textnumber = (TextView)findViewById(R.id.textnumber);
        emailtext = (TextView)findViewById(R.id.emailtext);
        others = (TextView)findViewById(R.id.others);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            getMenuInflater().inflate(R.menu.menu_with_refresh_and_more, menu);
            MenuItem editDelegate =menu.findItem(R.id.delegateEdit);
            if(connectAPIModel != null && connectAPIModel.isIs_Delegate()){
                editDelegate.setVisible(true);
            }else{
                editDelegate.setVisible(false);
            }
        } catch (Exception e) {

        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delegateEdit){
            if(NetworkUtils.isNetworkAvailable(CareTakersDetailsActivity.this)){
                Intent intent = new Intent(CareTakersDetailsActivity.this,AddDelegateActivity.class);
                intent.putExtra(Constants.INTENT_PARM,connectAPIModel);
                startActivityForResult(intent,Update_Request_code);
            }
        }else{
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Update_Request_code){
            setResult(Update_Request_code);
            finish();
        }
    }
}

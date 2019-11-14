package com.swaas.healthyengage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import Repositories.APIRepository;
import models.APIResponseModels;
import models.Delegates;
import utils.NetworkUtils;
import utils.PreferenceUtils;

public class HippaActivity extends AppCompatActivity {

    TextView hippatext,disagree,agree;
    Delegates delegates;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hippa);
        initializeViews();
    }

    private void initializeViews() {
        hippatext = (TextView)findViewById(R.id.hippatext);
        disagree = (TextView)findViewById(R.id.disagree);
        agree = (TextView)findViewById(R.id.agree);
        delegates = new Delegates();
        progressDialog = new ProgressDialog(this);
        getHippaDetails();
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitHippaApiCall(true);
            }
        });
        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitHippaApiCall(false);
            }
        });
    }

    private void submitHippaApiCall(boolean status) {
        APIRepository apiRepository = new APIRepository(this);
        apiRepository.setGetAPIResponseModel(new APIRepository.GetAPIResponseModel() {
            @Override
            public void getAPIResponseModelSuccess(APIResponseModels apiResponseModels) {
                progressDialog.show();
                if(apiResponseModels.isIs_hipaa_signed()){
                    Intent intent = new Intent(HippaActivity.this,HomePageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    PreferenceUtils.clearAllData(HippaActivity.this);
                    startActivity(new Intent(HippaActivity.this,TimeOutActivity.class));
                }
            }

            @Override
            public void getAPIResponseModelFailure(String s) {
                progressDialog.show();
            }
        });
        delegates.setDelegate_id(PreferenceUtils.getDelegateId(this));
        delegates.setPatient_id(PreferenceUtils.getPatientId(this));
        delegates.setIs_hipaa_signed(status);
        if(NetworkUtils.isNetworkAvailable(this)){
            progressDialog.setMessage("updating Acknowledgement details");
            progressDialog.setCancelable(false);
            progressDialog.show();
            apiRepository.updateHippa(delegates);
        }

    }


    @Override
    public void onBackPressed() {

    }

    private void getHippaDetails() {
        progressDialog.setMessage("Getting Acknowledgement details");
        progressDialog.setCancelable(false);
        progressDialog.show();
        APIRepository apiRepository = new APIRepository(this);
        apiRepository.setGetAPIResponseModel(new APIRepository.GetAPIResponseModel() {
            @Override
            public void getAPIResponseModelSuccess(APIResponseModels apiResponseModels) {
                progressDialog.dismiss();
                if(apiResponseModels != null && !TextUtils.isEmpty(apiResponseModels.getContent())){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        hippatext.setText(Html.fromHtml(apiResponseModels.getContent(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        hippatext.setText(Html.fromHtml(apiResponseModels.getContent()));
                    }
                }
            }

            @Override
            public void getAPIResponseModelFailure(String s) {
                progressDialog.dismiss();
            }
        });
        apiRepository.gethippa();
    }
}

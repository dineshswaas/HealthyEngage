package com.swaas.healthyengage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Alerts.IOSDialog;
import Alerts.IOSDialogBuilder;
import Alerts.IOSDialogClickListener;
import Repositories.APIRepository;
import br.com.sapereaude.maskedEditText.MaskedEditText;
import models.UserModel;
import utils.PreferenceUtils;

public class UserProfileActivity extends AppCompatActivity {

    EditText nameValue,lastnameValue;
    Spinner sexValue;
    TextView dobValue,emailValue,firstletter,loginname,LogoutText;
    MaskedEditText mobileValue;
    UserModel userModel;
    ProgressDialog progressDialog;
    MenuItem menuItem;
    List<String> stringList;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initializeViews();
        setLableDisabled(false);
        getUserDetails();
    }


    private void getUserDetails() {
        showProgressBar("Fetching user details");
        APIRepository apiRepository = new APIRepository(UserProfileActivity.this);
        apiRepository.setGetUserDetails(new APIRepository.GetUserDetails() {
            @Override
            public void getSuccess(UserModel userModelAPI) {
                userModel = userModelAPI;
                hideProgress();
                UserModel userModel = userModelAPI;
                if(userModel != null){
                    if(TextUtils.isEmpty(userModel.getLast_name())){
                        userModel.setLast_name("");
                    }
                    loginname.setText(userModel.getFirst_name()+" "+userModel.getLast_name());
                    firstletter.setText(userModel.getFirst_name().charAt(0)+"");
                }
                if(userModel != null){
                    setUserDetails();
                }
            }

            @Override
            public void getFailure(String s) {
                hideProgress();
            }
        });
        apiRepository.getUserDetails(PreferenceUtils.getUserId(UserProfileActivity.this));

    }

    private void setUserDetails() {

        if(!TextUtils.isEmpty(userModel.getFirst_name())){
            nameValue.setText(userModel.getFirst_name());
        }
        if(!TextUtils.isEmpty(userModel.getLast_name())){
            lastnameValue.setText(userModel.getLast_name());
        }

        if(!TextUtils.isEmpty(userModel.getDob())){
            dobValue.setText(DateHelper.getDisplayFormat(userModel.getDob().split("T")[0],"yyyy-MM-dd"));
        }

        if(!TextUtils.isEmpty(userModel.getMobile_no())){
            mobileValue.setText(userModel.getMobile_no());
        }
        if(!TextUtils.isEmpty(userModel.getEmail())){
            emailValue.setText(userModel.getEmail());
        }


    }

    private void setLableDisabled(boolean status) {
        nameValue.setEnabled(status);
        lastnameValue.setEnabled(status);
        dobValue.setEnabled(status);
        mobileValue.setEnabled(status);
        sexValue.setEnabled(status);
    }


    private void initializeViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("User Profile");
        nameValue = (EditText)findViewById(R.id.nameValue);
        lastnameValue= (EditText)findViewById(R.id.lastnameValue);
        sexValue = (Spinner)findViewById(R.id.sexValue);
        dobValue = (TextView)findViewById(R.id.dobValue);
        emailValue = (TextView)findViewById(R.id.emailValue);
        mobileValue = (MaskedEditText)findViewById(R.id.mobileValue);
        firstletter = (TextView)findViewById(R.id.firstletter);
        loginname = (TextView)findViewById(R.id.loginname);
        LogoutText = (TextView)findViewById(R.id.LogoutText);
        stringList = new ArrayList<>();
        stringList.add("Male");
        stringList.add("Female");
        stringList.add("Other");
        adapter = new ArrayAdapter<String>
                (UserProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, stringList);
        sexValue.setAdapter(adapter);
        userModel = new UserModel();
        LogoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IOSDialogBuilder(UserProfileActivity.this)
                        .setTitle("Logout")
                        .setSubtitle("Are you sure,you want to logout ?")
                        .setBoldPositiveLabel(false)
                        .setCancelable(false)
                        .setSingleButtonView(false)
                        .setPositiveListener("Yes", new IOSDialogClickListener() {
                            @Override
                            public void onClick(IOSDialog dialog) {
                                dialog.dismiss();
                                PreferenceUtils.clearAllData(UserProfileActivity.this);
                                startActivity(new Intent(UserProfileActivity.this,TimeOutActivity.class));
                            }
                        })
                        .setNegativeListener("No", new IOSDialogClickListener() {
                            @Override
                            public void onClick(IOSDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setSinglePositiveListener("", null)
                        .build().show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_refresh_and_more,menu);
        menuItem = menu.findItem(R.id.delegateEdit);
        menuItem.setTitle("Edit");
        menuItem.setVisible(false);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delegateEdit){
            if(item.getTitle().equals("Edit")){
                menuItem.setTitle("Done");
                setLableDisabled(true);
            }else{
                menuItem.setTitle("Edit");
                setLableDisabled(false);
            }

        }else{
            finish();
        }
        return true;
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


    void showProgressBar(String message){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    void hideProgress(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }


    }


}

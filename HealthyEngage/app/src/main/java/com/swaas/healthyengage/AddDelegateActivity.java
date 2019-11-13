package com.swaas.healthyengage;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Alerts.IOSDialog;
import Alerts.IOSDialogBuilder;
import Alerts.IOSDialogClickListener;
import Repositories.APIRepository;
import models.APIResponseModels;
import models.ConnectAPIModel;
import models.Delegates;
import models.RelationshipCategoryModel;
import utils.NetworkUtils;
import utils.PreferenceUtils;

public class AddDelegateActivity extends AppCompatActivity {

    EditText mobileEd,nameEd;
    Spinner relationSpinner;
    ImageView contactImage;
    int PICK_CONTACT = 121;
    int REQUEST_MULTIPLE_PERMISSIONS = 123;
    int Request_code=91;
    ArrayAdapter adapter;
    RelationshipCategoryModel relationshipCategoryModel = new RelationshipCategoryModel();
    List<RelationshipCategoryModel> relationshipCategoryModelList;
    RelativeLayout buttonlayout;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delegate);
        initializeViews();
        if(NetworkUtils.isNetworkAvailable(this)){
            getRelationShipDetails();
        }
    }

    private void getRelationShipDetails() {

        APIRepository apiRepository = new APIRepository(this);
        apiRepository.setGetCareTakersDetails(new APIRepository.GetCareTakersDetails() {
            @Override
            public void getCareTakersSuccess(ConnectAPIModel connectAPIModel) {
                relationshipCategoryModelList = connectAPIModel.getRelationshipCategory();
                adapter = new ArrayAdapter<RelationshipCategoryModel>
                        (AddDelegateActivity.this, android.R.layout.simple_spinner_dropdown_item, relationshipCategoryModelList);
                relationSpinner.setAdapter(adapter);
            }

            @Override
            public void getCareTakersFailure(String s) {

            }
        });
        apiRepository.getRelationDetails();


        relationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relationshipCategoryModel = relationshipCategoryModelList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Delegate");
        mobileEd = (EditText)findViewById(R.id.mobileEd);
        nameEd = (EditText)findViewById(R.id.nameEd);
        progressDialog = new ProgressDialog(this);
        relationSpinner = (Spinner)findViewById(R.id.relationSpinner);
        contactImage = (ImageView)findViewById(R.id.contactimage);
        buttonlayout = (RelativeLayout)findViewById(R.id.buttonlayout);
        contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   // AccessContact();
                    if(ActivityCompat.checkSelfPermission(AddDelegateActivity.this, Manifest.permission.READ_CONTACTS)
                            == PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent, PICK_CONTACT);
                    }else{

                        new IOSDialogBuilder(AddDelegateActivity.this)
                                .setTitle("Permission Needed")
                                .setSubtitle("You need to grant access to read contact")
                                .setBoldPositiveLabel(false)
                                .setCancelable(false)
                                .setSingleButtonView(false)
                                .setPositiveListener("Cancel", new IOSDialogClickListener() {
                                    @Override
                                    public void onClick(IOSDialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeListener("OK", new IOSDialogClickListener() {
                                    @Override
                                    public void onClick(IOSDialog dialog) {
                                        dialog.dismiss();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                                    REQUEST_MULTIPLE_PERMISSIONS);

                                        }
                                    }
                                })
                                .setSinglePositiveListener("OK", null)
                                .build().show();

                    }

                }

            }
        });


        buttonlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nameEd.getText().toString())){
                    showAlertMessage("Enter delegate name");
                }else if(TextUtils.isEmpty(mobileEd.getText().toString())){
                    showAlertMessage("Enter delegate mobile number");
                } else if(relationshipCategoryModel == null){
                    showAlertMessage("select delegate relationship");
                }else{
                    if(NetworkUtils.isNetworkAvailable(AddDelegateActivity.this)){
                        submitApiCall();
                    }
                }


            }
        });

    }

    private void submitApiCall() {
        progressDialog.setMessage("Adding new delegate");
        progressDialog.show();
        APIRepository apiRepository = new APIRepository(AddDelegateActivity.this);
        apiRepository.setGetAPIResponseModel(new APIRepository.GetAPIResponseModel() {
            @Override
            public void getAPIResponseModelSuccess(APIResponseModels apiResponseModels) {
                progressDialog.dismiss();
                if(apiResponseModels!= null){
                    setResult(Request_code);
                    finish();
                }else{
                    showAlertMessage("Delegate mobile number already exists");
                }
            }

            @Override
            public void getAPIResponseModelFailure(String s) {
                showAlertMessage("Something went wrong");
                progressDialog.dismiss();
            }
        });
        Delegates delegates = new Delegates();
        delegates.setPatient_id(PreferenceUtils.getPatientId(AddDelegateActivity.this));
        if(nameEd.getText().toString().contains(" ")){
            delegates.setFirst_name(nameEd.getText().toString().split(" ")[0]);
            delegates.setLast_name(nameEd.getText().toString().split(" ")[1]);
        }else{
            delegates.setFirst_name(nameEd.getText().toString().trim());
            delegates.setLast_name("");
        }
        delegates.setMobile_no(mobileEd.getText().toString());
        String cCode = Locale.getDefault().getCountry();
        delegates.setCountry_code(PreferenceUtils.GetCountryZipCode(AddDelegateActivity.this,cCode));
        delegates.setRelationship_category_id(relationshipCategoryModel.getId());
        apiRepository.submitDelegate(delegates);
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


    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if(reqCode == PICK_CONTACT){
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor c = managedQuery(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    try {
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:" + cNumber);
                            mobileEd.setText(cNumber);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        nameEd.setText(name);
                    }
                    catch (Exception ex)
                    {
                        Log.d("parm",ex.getMessage());
                    }
                }
            }

        }
    }
}

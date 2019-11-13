package com.swaas.healthyengage;

import android.Manifest;
import android.app.Activity;
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
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import Alerts.IOSDialog;
import Alerts.IOSDialogBuilder;
import Alerts.IOSDialogClickListener;
import Repositories.APIRepository;
import models.ConnectAPIModel;
import models.RelationshipCategoryModel;
import utils.NetworkUtils;

public class AddDelegateActivity extends AppCompatActivity {

    EditText mobileEd,nameEd;
    Spinner relationSpinner;
    ImageView contactImage;
    int PICK_CONTACT = 121;
    int REQUEST_MULTIPLE_PERMISSIONS = 123;
    ArrayAdapter adapter;

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
                List<RelationshipCategoryModel> relationshipCategoryModelList = connectAPIModel.getRelationshipCategory();
                adapter = new ArrayAdapter<RelationshipCategoryModel>
                        (AddDelegateActivity.this, android.R.layout.simple_spinner_dropdown_item, relationshipCategoryModelList);
                relationSpinner.setAdapter(adapter);
            }

            @Override
            public void getCareTakersFailure(String s) {

            }
        });
        apiRepository.getRelationDetails();
    }

    private void initializeViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Delegate");
        mobileEd = (EditText)findViewById(R.id.mobileEd);
        nameEd = (EditText)findViewById(R.id.nameEd);
        relationSpinner = (Spinner)findViewById(R.id.relationSpinner);
        contactImage = (ImageView)findViewById(R.id.contactimage);
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




    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddDelegateActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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

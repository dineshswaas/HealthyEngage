package com.healthyengage.patientcare;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import Alerts.IOSDialog;
import Alerts.IOSDialogBuilder;
import Alerts.IOSDialogClickListener;
import Repositories.APIRepository;
import br.com.sapereaude.maskedEditText.MaskedEditText;
import models.APIResponseModels;
import models.ConnectAPIModel;
import models.Delegates;
import models.RelationshipCategoryModel;
import utils.Constants;
import utils.NetworkUtils;
import utils.PreferenceUtils;

public class AddDelegateActivity extends AppCompatActivity {

    EditText nameEd;
    MaskedEditText mobileEd;
    Spinner relationSpinner;
    ImageView contactImage;
    int PICK_CONTACT = 121;
    int REQUEST_MULTIPLE_PERMISSIONS = 123;
    int Request_code=91;
    int Update_Request_code=92;
    ArrayAdapter adapter;
    RelationshipCategoryModel relationshipCategoryModel = new RelationshipCategoryModel();
    List<RelationshipCategoryModel> relationshipCategoryModelList;
    RelativeLayout buttonlayout;
    ProgressDialog progressDialog;
    ConnectAPIModel connectAPIModel;
    TextView submitText;
    boolean isFromDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delegate);
        initializeViews();
        getIntentData();
        if(NetworkUtils.isNetworkAvailable(this)){
            getRelationShipDetails();
        }

    }


    private void getRelationShipDetails() {
        showProgress("Fetching relationship details");
        APIRepository apiRepository = new APIRepository(this);
        apiRepository.setGetCareTakersDetails(new APIRepository.GetCareTakersDetails() {
            @Override
            public void getCareTakersSuccess(ConnectAPIModel connectAPIModel) {
                relationshipCategoryModelList = connectAPIModel.getRelationshipCategory();
                adapter = new ArrayAdapter<RelationshipCategoryModel>
                        (AddDelegateActivity.this, android.R.layout.simple_spinner_dropdown_item, relationshipCategoryModelList);
                relationSpinner.setAdapter(adapter);
                if(connectAPIModel != null){
                    if(relationshipCategoryModelList != null && relationshipCategoryModelList.size() > 0){
                        for(RelationshipCategoryModel categoryModel : relationshipCategoryModelList ){
                            if(categoryModel.getId().equalsIgnoreCase(connectAPIModel.getRelationship_category_id())){
                                relationSpinner.setSelection(relationshipCategoryModelList.indexOf(categoryModel));
                                break;
                            }
                        }
                    }
                }
                hideProgress();
            }

            @Override
            public void getCareTakersFailure(String s) {
                hideProgress();
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
    private void getIntentData() {
        connectAPIModel = (ConnectAPIModel) getIntent().getSerializableExtra(Constants.INTENT_PARM);
        if(connectAPIModel != null){
            if(!TextUtils.isEmpty(connectAPIModel.getLast_name())){
                nameEd.setText(connectAPIModel.getFirst_name()+ " "+connectAPIModel.getLast_name());
            }else{
                nameEd.setText(connectAPIModel.getFirst_name());
            }
            if(!TextUtils.isEmpty(connectAPIModel.getMobile_no())){
                mobileEd.setText(connectAPIModel.getMobile_no());
            }

            submitText.setText("UPDATE");
        }


    }

    private void initializeViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Delegate");
        mobileEd = (MaskedEditText) findViewById(R.id.mobileEd);
        String cCode = Locale.getDefault().getCountry();
        cCode = PreferenceUtils.GetCountryZipCode(this,cCode);
       // mobileEd.setMask("(###)###-####");
        nameEd = (EditText)findViewById(R.id.nameEd);
        submitText = (TextView)findViewById(R.id.submitText);
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
                validationCheck();
            }
        });



        mobileEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    validationCheck();
                }
                return false;
            }
        });

        nameEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    validationCheck();
                }
                return false;
            }
        });
    }

    private void validationCheck() {
        if(TextUtils.isEmpty(nameEd.getText().toString())){
            showAlertMessage("Enter delegate name");
        }else if(TextUtils.isEmpty(mobileEd.getRawText().trim())){
            showAlertMessage("Enter delegate mobile number");
        } else if(relationshipCategoryModel == null){
            showAlertMessage("select delegate relationship");
        }else{
            if(NetworkUtils.isNetworkAvailable(AddDelegateActivity.this)){
                submitApiCall();
            }
        }

    }

    private void submitApiCall() {

        APIRepository apiRepository = new APIRepository(AddDelegateActivity.this);
        apiRepository.setGetAPIResponseModel(new APIRepository.GetAPIResponseModel() {
            @Override
            public void getAPIResponseModelSuccess(APIResponseModels apiResponseModels) {
                if(apiResponseModels!= null){
                    if(connectAPIModel != null){
                        if(isFromDelete){
                            showAlertSuccessMessage("Delegate has been deleted","Delete");
                        }else{
                            showAlertSuccessMessage("Delegate details has been updated","Update");
                        }
                    }else{
                        showAlertSuccessMessage("Delegate has been added successfully","Success");
                    }
                }else{
                    showAlertMessage("Delegate mobile number already exists");
                }
                hideProgress();
            }

            @Override
            public void getAPIResponseModelFailure(String s) {
                showAlertMessage("Something went wrong");
                hideProgress();
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
        delegates.setMobile_no(mobileEd.getRawText());
        String cCode = Locale.getDefault().getCountry();
        delegates.setCountry_code(PreferenceUtils.GetCountryZipCode(AddDelegateActivity.this,cCode));
        delegates.setRelationship_category_id(relationshipCategoryModel.getId());
        if(connectAPIModel != null){
            delegates.setDelegate_id(connectAPIModel.getDelegate_id());
            if(isFromDelete){
                showProgress("Deleting the delegate details");
                delegates.setIs_deleted(isFromDelete);
            }else{
                showProgress("Updating delegate details");
                delegates.setIs_deleted(isFromDelete);
            }
            delegates.setIs_patient(true);

            apiRepository.updateDelegate(delegates);
        }else{
            showProgress("Adding new delegate");
            apiRepository.submitDelegate(delegates);
        }

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
        getMenuInflater().inflate(R.menu.menu_with_refresh_and_more,menu);
        MenuItem menuItem = menu.findItem(R.id.delegateEdit);
        if(connectAPIModel != null){
            menuItem.setTitle("Delete");
            menuItem.setVisible(true);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delegateEdit){
            isFromDelete = false;
            deleteConfirmation();
        }else{
            finish();
        }
        return true;
    }

    private void deleteConfirmation() {
            new IOSDialogBuilder(this)
                    .setTitle("Delete confirmation")
                    .setSubtitle("Are you sure you want to delete this delegate?")
                    .setBoldPositiveLabel(false)
                    .setCancelable(false)
                    .setSingleButtonView(false)
                    .setPositiveListener("Delete", new IOSDialogClickListener() {
                        @Override
                        public void onClick(IOSDialog dialog) {
                            dialog.dismiss();
                            isFromDelete = true;
                            submitApiCall();
                        }
                    })
                    .setNegativeListener("Cancel", new IOSDialogClickListener() {
                        @Override
                        public void onClick(IOSDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .setSinglePositiveListener("OK", null)
                    .build().show();

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

    void showAlertSuccessMessage(String message,String title){
        new IOSDialogBuilder(this)
                .setTitle(title)
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
                        setResult(Request_code);
                        finish();
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


    void showProgress(String message){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    void hideProgress(){
        progressDialog.dismiss();
    }
}

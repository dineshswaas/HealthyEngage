package com.swaas.healthyengage;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.warkiz.tickseekbar.OnSeekChangeListener;
import com.warkiz.tickseekbar.SeekParams;
import com.warkiz.tickseekbar.TextPosition;
import com.warkiz.tickseekbar.TickMarkType;
import com.warkiz.tickseekbar.TickSeekBar;
import com.xw.repo.BubbleSeekBar;
import com.zhouyou.view.seekbar.SignSeekBar;

import java.util.ArrayList;
import java.util.List;

import Alerts.IOSDialog;
import Alerts.IOSDialogBuilder;
import Alerts.IOSDialogClickListener;
import Repositories.APIRepository;
import models.APIResponseModels;
import models.CarePlanModels;
import utils.Constants;
import utils.NetworkUtils;
import utils.PreferenceUtils;

import static java.security.AccessController.getContext;

public class AssessmentDetailsActivity extends AppCompatActivity {


    TextView name,description,question,booleanquestion,pickerquestion,sliderquestion,minlabel,maxlabel;
    CarePlanModels.CarePlanAssessment carePlanAssessment;
    RelativeLayout textParentLayout,booleanParentView,pickerParentView,sliderParentView,buttonlayout;
    EditText textEditText;
    RadioGroup radioGroup;
    RadioButton rpositiveButton,rnegativeButton;
    Spinner pickerSpinner;
    List<String> pickerList;
    TickSeekBar  tickSeekBar;
    TextView submitText;
    int  min=0,max=0;
    String previousValue,selectedValue;
    SeekBar seekBar;
    BubbleSeekBar demo_3_seek_bar_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);
        try {
            initializeView();
            getIntentData();
        }catch (Exception e){
            Log.d("parm",e.getMessage());
        }


    }

    private void getIntentData() {
        carePlanAssessment = (CarePlanModels.CarePlanAssessment) getIntent().getSerializableExtra(Constants.INTENT_PARM);

        if(!TextUtils.isEmpty(carePlanAssessment.getName())){
            name.setText(carePlanAssessment.getName());
        }
        if(!TextUtils.isEmpty(carePlanAssessment.getDescription())){
            description.setText(carePlanAssessment.getDescription());
        }else{
            description.setVisibility(View.GONE);
        }



        submitTextDisabled();
        if(carePlanAssessment.getInput_type().equalsIgnoreCase(Constants.INPUT_TEXT)){
            textParentLayout.setVisibility(View.VISIBLE);
            booleanParentView.setVisibility(View.GONE);
            pickerParentView.setVisibility(View.GONE);
            sliderParentView.setVisibility(View.GONE);
            onBindTextDetails();
        }else if(carePlanAssessment.getInput_type().equalsIgnoreCase(Constants.INPUT_BOOLEAN)){
            textParentLayout.setVisibility(View.GONE);
            booleanParentView.setVisibility(View.VISIBLE);
            pickerParentView.setVisibility(View.GONE);
            sliderParentView.setVisibility(View.GONE);
            onBindBooleanDetails();
        }else if(carePlanAssessment.getInput_type().equalsIgnoreCase(Constants.INPUT_PICKER)){
            textParentLayout.setVisibility(View.GONE);
            booleanParentView.setVisibility(View.GONE);
            pickerParentView.setVisibility(View.VISIBLE);
            sliderParentView.setVisibility(View.GONE);
            onBindPickerDetails();
        }else if(carePlanAssessment.getInput_type().equalsIgnoreCase(Constants.INPUT_SLIDER)){
            textParentLayout.setVisibility(View.GONE);
            pickerParentView.setVisibility(View.GONE);
            booleanParentView.setVisibility(View.GONE);
            sliderParentView.setVisibility(View.VISIBLE);
            onBindSliderDetails();
        }




    }

    private void onBindSliderDetails() {






        if(!TextUtils.isEmpty(carePlanAssessment.getQuestion())){
            sliderquestion.setText(carePlanAssessment.getQuestion());
        }

        if(!TextUtils.isEmpty(carePlanAssessment.getLabel_min())){
            minlabel.setText(carePlanAssessment.getLabel_min());
        }
        if(!TextUtils.isEmpty(carePlanAssessment.getLabel_max())){
            maxlabel.setText(carePlanAssessment.getLabel_max());
        }


        if(carePlanAssessment.getPatientAssessment() != null && carePlanAssessment.getPatientAssessment().size() > 0){
            CarePlanModels.CarePlanAssessment.PatientAssessment assessment = carePlanAssessment.getPatientAssessment().get(0);
            if(assessment != null){
                if(carePlanAssessment.getPatientAssessment() != null && carePlanAssessment.getPatientAssessment().size() > 0){
                    for(CarePlanModels.CarePlanAssessment.PatientAssessment patientAssessment : carePlanAssessment.getPatientAssessment()){
                        if(patientAssessment.getAssessment_date().split("T")[0].equalsIgnoreCase(carePlanAssessment.getAssessmentDate())){
                            if(!TextUtils.isEmpty(patientAssessment.getValue())){
                                previousValue = patientAssessment.getValue();
                                if(Integer.parseInt(patientAssessment.getValue()) > 0){
                                    selectedValue = patientAssessment.getValue();
                                }
                                submitTextEnabled();
                            }
                            break;
                        }
                    }
                }
            }
        }
        if(TextUtils.isEmpty(selectedValue)){
           selectedValue = carePlanAssessment.getMin();
        }


        demo_3_seek_bar_2.getConfigBuilder()
                .min(Integer.parseInt(carePlanAssessment.getMin()))
                .max(Integer.parseInt(carePlanAssessment.getMax()))
                .progress(Integer.parseInt(selectedValue))
                .sectionCount(Integer.parseInt(carePlanAssessment.getMax()) - Integer.parseInt(carePlanAssessment.getMin()))
                .trackColor(ContextCompat.getColor(AssessmentDetailsActivity.this, R.color.grey_non_pressed))
                .secondTrackColor(ContextCompat.getColor(AssessmentDetailsActivity.this, R.color.colorPrimary))
                .thumbColor(ContextCompat.getColor(AssessmentDetailsActivity.this, R.color.colorPrimary))
                .showSectionText()
                .sectionTextColor(ContextCompat.getColor(AssessmentDetailsActivity.this, R.color.draft_grey))
                .sectionTextSize(18)
                .showThumbText()
                .touchToSeek()
                .thumbTextColor(ContextCompat.getColor(AssessmentDetailsActivity.this, R.color.dark_red))
                .thumbTextSize(18)
                .bubbleColor(ContextCompat.getColor(AssessmentDetailsActivity.this, R.color.dark_red))
                .bubbleTextSize(18)
                .showSectionMark()
                .seekBySection()
                .autoAdjustSectionMark()
                .sectionTextPosition(BubbleSeekBar.TextPosition.BELOW_SECTION_MARK)
                .build();


        demo_3_seek_bar_2.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                super.getProgressOnActionUp(bubbleSeekBar, progress, progressFloat);
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                super.getProgressOnFinally(bubbleSeekBar, progress, progressFloat, fromUser);
                selectedValue = String.valueOf(progress);
                submitTextEnabled();
            }
        });

        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkUtils.isNetworkAvailable(AssessmentDetailsActivity.this)){
                    if(!TextUtils.isEmpty(previousValue) && previousValue.equalsIgnoreCase(selectedValue)){
                        finish();
                    }else{
                        carePlanAssessment.setValue(selectedValue);
                        updateAssessmentValue();
                    }

                }
            }
        });

    }

    private void onBindPickerDetails() {

        if(!TextUtils.isEmpty(carePlanAssessment.getQuestion())){
            pickerquestion.setText(carePlanAssessment.getQuestion());
        }
        submitTextDisabled();
        if(!TextUtils.isEmpty(carePlanAssessment.getMin()) && !TextUtils.isEmpty(carePlanAssessment.getMax())){
            int min = Integer.parseInt(carePlanAssessment.getMin());
            int max = Integer.parseInt(carePlanAssessment.getMax());
            pickerList = new ArrayList<>();
            pickerList.add("Select an answer");
            for (int i = min; i <= max ;i++){
                pickerList.add(String.valueOf(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item,pickerList);
            pickerSpinner.setAdapter(adapter);

            pickerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedValue = pickerList.get(position);
                    if(selectedValue.equalsIgnoreCase("Select an answer")){
                        selectedValue ="";
                        submitTextDisabled();
                    }else{
                        submitTextEnabled();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        if(carePlanAssessment.getPatientAssessment() != null && carePlanAssessment.getPatientAssessment().size() > 0){
            CarePlanModels.CarePlanAssessment.PatientAssessment assessment = carePlanAssessment.getPatientAssessment().get(0);
            if(assessment != null){
                if(carePlanAssessment.getPatientAssessment() != null && carePlanAssessment.getPatientAssessment().size() > 0){
                    for(CarePlanModels.CarePlanAssessment.PatientAssessment patientAssessment : carePlanAssessment.getPatientAssessment()){
                        if(patientAssessment.getAssessment_date().split("T")[0].equalsIgnoreCase(carePlanAssessment.getAssessmentDate())){
                            if(!TextUtils.isEmpty(patientAssessment.getValue())){
                                previousValue = patientAssessment.getValue();
                                if(Integer.parseInt(patientAssessment.getValue()) > 0){
                                    pickerSpinner.setSelection(getIndex(pickerSpinner, patientAssessment.getValue()));
                                }
                                submitTextEnabled();
                            }
                            break;
                        }
                    }
                }
            }
        }

        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkUtils.isNetworkAvailable(AssessmentDetailsActivity.this)){
                    if(!TextUtils.isEmpty(previousValue) && previousValue.equalsIgnoreCase(selectedValue)){
                        finish();
                    }else{
                        carePlanAssessment.setValue(selectedValue);
                        updateAssessmentValue();
                    }

                }
            }
        });




    }
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
    private void onBindTextDetails() {
        if(!TextUtils.isEmpty(carePlanAssessment.getQuestion())){
            question.setText(carePlanAssessment.getQuestion());
        }
        if(!TextUtils.isEmpty(carePlanAssessment.getMin()) && !TextUtils.isEmpty(carePlanAssessment.getMax())){
            min = Integer.parseInt(carePlanAssessment.getMin());
            max = Integer.parseInt(carePlanAssessment.getMax());
        }

        submitTextDisabled();

        if(carePlanAssessment.getPatientAssessment() != null && carePlanAssessment.getPatientAssessment().size() > 0){
            CarePlanModels.CarePlanAssessment.PatientAssessment assessment = carePlanAssessment.getPatientAssessment().get(0);
            if(assessment != null){
                if(carePlanAssessment.getPatientAssessment() != null && carePlanAssessment.getPatientAssessment().size() > 0){
                    for(CarePlanModels.CarePlanAssessment.PatientAssessment patientAssessment : carePlanAssessment.getPatientAssessment()){
                        if(patientAssessment.getAssessment_date().split("T")[0].equalsIgnoreCase(carePlanAssessment.getAssessmentDate())){
                            if(!TextUtils.isEmpty(patientAssessment.getValue())){
                                textEditText.setText(patientAssessment.getValue());
                                previousValue = patientAssessment.getValue();
                                submitTextEnabled();
                            }
                            break;
                        }
                    }
                }
            }

        }

        textEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            if(!TextUtils.isEmpty(s)){
                submitTextEnabled();
            }else{
                submitTextDisabled();
            }
            }
        });
        //9 exceeds the maximum allowed value (8)
        //0 is less than minimum allowed value (1)

        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(textEditText.getText().toString())){
                    int actualValue = Integer.parseInt(textEditText.getText().toString());
                    if(actualValue < min){
                        showAlertMessage(String.valueOf(actualValue)+" exceeds the minimum allowed value ("+String.valueOf(min)+")");
                    } else if(actualValue > max){
                        showAlertMessage(String.valueOf(actualValue)+" exceeds the maximum allowed value ("+String.valueOf(max)+")");
                    }else{
                        carePlanAssessment.setValue(textEditText.getText().toString().trim());
                        if(NetworkUtils.isNetworkAvailable(AssessmentDetailsActivity.this)){
                            if(!TextUtils.isEmpty(previousValue) && previousValue.equalsIgnoreCase(textEditText.getText().toString().trim())){
                                finish();
                            }else{
                                updateAssessmentValue();
                            }

                        }

                    }
                }
            }
        });



    }

    private void updateAssessmentValue() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating assessment detail");
        progressDialog.show();
        APIRepository apiRepository = new APIRepository(this);
        apiRepository.setGetAPIResponseModel(new APIRepository.GetAPIResponseModel() {
            @Override
            public void getAPIResponseModelSuccess(APIResponseModels apiResponseModels) {
                progressDialog.dismiss();
                if(apiResponseModels.isSync()){
                    Intent intent = new Intent();
                    setResult(100,intent);
                    finish();
                }else{
                    Toast.makeText(AssessmentDetailsActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void getAPIResponseModelFailure(String s) {
                Toast.makeText(AssessmentDetailsActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        carePlanAssessment.setUserId(PreferenceUtils.getUserId(this));
        carePlanAssessment.setPatientId(PreferenceUtils.getPatientId(this));
        carePlanAssessment.setLastSyncDate(PreferenceUtils.getLastSyncDate(this));
        apiRepository.updateAssessment(carePlanAssessment);

    }

    private void onBindBooleanDetails() {

        if(!TextUtils.isEmpty(carePlanAssessment.getQuestion())){
            booleanquestion.setText(carePlanAssessment.getQuestion());
        }
        if(!TextUtils.isEmpty(carePlanAssessment.getPositve_label())){
            rpositiveButton.setText(carePlanAssessment.getPositve_label());
        }
        if(!TextUtils.isEmpty(carePlanAssessment.getNegative_label())){
            rnegativeButton.setText(carePlanAssessment.getNegative_label());
        }

        radioGroup.clearCheck();
        if(carePlanAssessment.getPatientAssessment() != null && carePlanAssessment.getPatientAssessment().size() > 0){
            CarePlanModels.CarePlanAssessment.PatientAssessment assessment = carePlanAssessment.getPatientAssessment().get(0);
            if(assessment != null){
                if(carePlanAssessment.getPatientAssessment() != null && carePlanAssessment.getPatientAssessment().size() > 0){
                    for(CarePlanModels.CarePlanAssessment.PatientAssessment patientAssessment : carePlanAssessment.getPatientAssessment()){
                        if(patientAssessment.getAssessment_date().split("T")[0].equalsIgnoreCase(carePlanAssessment.getAssessmentDate())){
                            if(!TextUtils.isEmpty(patientAssessment.getValue())){
                                previousValue = patientAssessment.getValue();
                                if(Integer.parseInt(patientAssessment.getValue()) > 0){
                                    rpositiveButton.setChecked(true);
                                }else{
                                    rnegativeButton.setChecked(true);
                                }
                                submitTextEnabled();
                            }
                            break;
                        }
                    }
                }
            }

        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submitTextEnabled();
            }
        });



        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rpositiveButton.isChecked() || rnegativeButton.isChecked()){
                    if(rpositiveButton.isChecked()){
                        carePlanAssessment.setValue("1");
                    }else if(rnegativeButton.isChecked()){
                        carePlanAssessment.setValue("0");
                    }
                    if(NetworkUtils.isNetworkAvailable(AssessmentDetailsActivity.this)){
                        if(!TextUtils.isEmpty(previousValue) && previousValue.equalsIgnoreCase(carePlanAssessment.getValue())){
                            finish();
                        }else{
                            updateAssessmentValue();
                        }

                    }
                }

            }
        });



    }

    void showAlertMessage(final String message){
        new IOSDialogBuilder(AssessmentDetailsActivity.this)
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


    private void initializeView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Healthy Engage");
        getSupportActionBar().setSubtitle("Assessment");
        name = (TextView)findViewById(R.id.name);
        description = (TextView)findViewById(R.id.description);
        textParentLayout = (RelativeLayout)findViewById(R.id.textParentView);
        question = (TextView)findViewById(R.id.question);
        textEditText = (EditText) findViewById(R.id.textEditText);
        booleanParentView = (RelativeLayout)findViewById(R.id.booleanParentView);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        rpositiveButton = (RadioButton)findViewById(R.id.rpositiveButton);
        rnegativeButton = (RadioButton)findViewById(R.id.radiobuttonnegative);
        booleanquestion = (TextView)findViewById(R.id.booleanquestion);
        pickerParentView = (RelativeLayout)findViewById(R.id.pickerParentView);
        pickerSpinner= (Spinner)findViewById(R.id.pickerSpinner);
        pickerquestion = (TextView)findViewById(R.id.pickerquestion);
        sliderParentView = (RelativeLayout)findViewById(R.id.sliderParentView);
        sliderquestion = (TextView)findViewById(R.id.sliderquestion);
        tickSeekBar = (TickSeekBar) findViewById(R.id.tickSeekBar);
        minlabel = (TextView)findViewById(R.id.minlabel);
        maxlabel = (TextView)findViewById(R.id.maxlabel);
        buttonlayout = (RelativeLayout)findViewById(R.id.buttonlayout);
        submitText = (TextView)findViewById(R.id.submitText);
        seekBar = (SeekBar)findViewById(R.id.seekbar);
        demo_3_seek_bar_2 = (BubbleSeekBar)findViewById(R.id.demo_3_seek_bar_2);

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


    void submitTextDisabled(){
        submitText.setEnabled(false);
        submitText.setTextColor(getResources().getColor(R.color.grey_black));
        submitText.setBackground(getResources().getDrawable((R.color.light_gray)));
    }
    void submitTextEnabled(){
        submitText.setEnabled(true);
        submitText.setTextColor(getResources().getColor(R.color.white));
        submitText.setBackground(getResources().getDrawable((R.color.colorPrimary)));
    }
}

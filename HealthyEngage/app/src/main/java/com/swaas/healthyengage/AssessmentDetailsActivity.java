package com.swaas.healthyengage;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import models.CarePlanModels;
import utils.Constants;

public class AssessmentDetailsActivity extends AppCompatActivity {


    TextView name,description,question,booleanquestion,pickerquestion;
    CarePlanModels.CarePlanAssessment carePlanAssessment;
    RelativeLayout textParentLayout,booleanParentView,pickerParentView;
    EditText textEditText;
    RadioGroup radioGroup;
    RadioButton rpositiveButton,rnegativeButton;
    Spinner pickerSpinner;
    List<String> pickerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);
        initializeView();
        getIntentData();

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




        if(carePlanAssessment.getInput_type().equalsIgnoreCase(Constants.INPUT_TEXT)){
            textParentLayout.setVisibility(View.VISIBLE);
            booleanParentView.setVisibility(View.GONE);
            pickerParentView.setVisibility(View.GONE);
            onBindTextDetails();
        }else if(carePlanAssessment.getInput_type().equalsIgnoreCase(Constants.INPUT_BOOLEAN)){
            textParentLayout.setVisibility(View.GONE);
            booleanParentView.setVisibility(View.VISIBLE);
            pickerParentView.setVisibility(View.GONE);
            onBindBooleanDetails();
        }else if(carePlanAssessment.getInput_type().equalsIgnoreCase(Constants.INPUT_PICKER)){
            textParentLayout.setVisibility(View.GONE);
            booleanParentView.setVisibility(View.GONE);
            pickerParentView.setVisibility(View.VISIBLE);
            onBindPickerDetails();
        }else if(carePlanAssessment.getInput_type().equalsIgnoreCase(Constants.INPUT_SLIDER)){
            textParentLayout.setVisibility(View.GONE);
            pickerParentView.setVisibility(View.GONE);
            booleanParentView.setVisibility(View.GONE);
        }

    }

    private void onBindPickerDetails() {

        if(!TextUtils.isEmpty(carePlanAssessment.getQuestion())){
            pickerquestion.setText(carePlanAssessment.getQuestion());
        }


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
        }


    }

    private void onBindTextDetails() {
        if(!TextUtils.isEmpty(carePlanAssessment.getQuestion())){
            question.setText(carePlanAssessment.getQuestion());
        }

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
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);

            }
        });



    }

    private void initializeView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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

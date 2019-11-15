package com.swaas.healthyengage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Alerts.IOSDialog;
import Alerts.IOSDialogBuilder;
import Alerts.IOSDialogClickListener;
import Repositories.APIRepository;
import Services.SessionExpiredServices;
import models.APIResponseModels;
import models.CarePlanModels;
import models.InterventionElements;
import utils.Constants;
import utils.NetworkUtils;
import utils.PreferenceUtils;

/**
 * Created by Adib on 13-Apr-17.
 */

public class CarePlanFragment extends Fragment implements  CarePlanAdapter.OnTpDateClickListener, View.OnClickListener {

    private OnFragmentInteractionListener listener;
    private RecyclerView carePlanCalenderRecyclerView;
    View mView;
    List<CarePlanModels> tpHeaderModelList;
    CarePlanAdapter carePlanAdapter;
    TextView percentIndicatior,dateText,today,intername,interdosage,instruction,back;
    CircularProgressBar donut_progress;
    boolean imageonebool,imagetwobool,isdateAlreadySelect;
    int selectedItemposition = -1;
    APIRepository carePlanRepository;
    List<CarePlanModels> carePlanList;
    LinearLayout interventionMainLayout,assessmentMainLayout,readonlyMainLayout,detailsView,calenderView;
    ProgressDialog progressBar;
    double completionPercentage;
    double interventionValue;
    String calenderStartDate,calenderEndDate;
    int currentDatePosition=0;
    String selectedDate;
    List<CarePlanModels> offlineCarePlaneList;
    public static CarePlanFragment newInstance() {
        return new CarePlanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_careplan, container, false);
        intializeViews();
        selectedDate = DateHelper.getCurrentDate();
        getCarePlanDetailsFromAPI(DateHelper.getCurrentDate());
        //SessionExpiredServices.timer.start();
        return mView;
    }

    private void getCarePlanDetailsFromAPI(String date) {

        carePlanRepository = new APIRepository(getActivity());
        dateText.setText(DateHelper.getDisplayFormat(date,"yyyy-MM-dd"));
        //carePlanList = new ArrayList<>();
        carePlanRepository.setGetCarePlanDetails(new APIRepository.GetCarePlanModelDetails() {
            @Override
            public void getCarePlanSuccess(List<CarePlanModels> carePlanModels,String lastSyncDate) {
                carePlanList = new ArrayList<>(carePlanModels);
                PreferenceUtils.setCarePlanList(getActivity(),null  );
                PreferenceUtils.setCarePlanList(getActivity(),carePlanList);
               // PreferenceUtils.setLastSyncDate(getActivity(),null);
               // PreferenceUtils.setLastSyncDate(getActivity(),lastSyncDate);
                calenderStartDate = carePlanList.get(0).getCurrent_cycle_start_date().split("T")[0];
                calenderEndDate = carePlanList.get(0).getCurrent_cycle_end_date().split("T")[0];
                bindCalenderDate(carePlanList);

            }

            @Override
            public void getCarePlanFailure(String s) {
                if(s.equalsIgnoreCase("No Care plan is assigned to this patient")){
                    showAlertMessage("No Care plan is assigned to this patient");
                }else{
                    showAlertMessage(s);
                    hideProgress();
                }
            }
        });
        CarePlanModels carePlanModels = new CarePlanModels();
        carePlanModels.setCarePlanId(PreferenceUtils.getCarePlanId(getActivity()));
        if(!TextUtils.isEmpty(PreferenceUtils.getLastSyncDate(getActivity()))){
            carePlanModels.setDay(PreferenceUtils.getLastSyncDate(getActivity()));
        }else{
            carePlanModels.setDay(Constants.DEFAULT_SERVER_DATE);
        }
        carePlanModels.setPatientId(PreferenceUtils.getPatientId(getActivity()));
        if(NetworkUtils.isNetworkAvailable(getActivity())){
            showProgressBar("Fetching care plan details.");
            carePlanRepository.getCarePlanDetails(carePlanModels);
        }

    }

    private void onBindOverAllDetails() {
        completionPercentage = 0;
        interventionValue = 0;
        String localListValue = PreferenceUtils.getCarePlanList(getActivity());
        if(localListValue != null){
        carePlanList = DeserializeCarePlan(localListValue);
        }
        onBindInterventionData(carePlanList.get(0).getCareplanIntervention());
        onBindAssessmentData(carePlanList.get(0).getCareplanAssessment());
        onBindReadOnly(carePlanList.get(0).getCareplanInstruction());
        setCompletion((int)tpHeaderModelList.get(selectedItemposition).getProgressValue());
        hideProgress();
    }

    private List<CarePlanModels> DeserializeCarePlan(String careplanList) {
        Gson gson = new Gson();
        List<CarePlanModels> sampleProductses = new ArrayList<CarePlanModels>();
        CarePlanModels[] sampleProductsArrayItems = gson.fromJson(careplanList, CarePlanModels[].class);
        sampleProductses = Arrays.asList(sampleProductsArrayItems);
        sampleProductses = new ArrayList<CarePlanModels>(sampleProductses);

        return (ArrayList<CarePlanModels>) sampleProductses;
    }


    private void onBindReadOnly(List<CarePlanModels.CarePlanInstruction> careplanInstruction) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        readonlyMainLayout.removeAllViews();
        for(final CarePlanModels.CarePlanInstruction carePlanInstruction : careplanInstruction){
            final View view = inflater.inflate(R.layout.instruction_items_view,null);
            final TextView instrName = (TextView)view.findViewById(R.id.instructionname);
            final TextView lable = (TextView)view.findViewById(R.id.instructionlabel);
            instrName.setText(carePlanInstruction.getTitle());
            lable.setText(carePlanInstruction.getLabel());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calenderView.setVisibility(View.GONE);
                    detailsView.setVisibility(View.VISIBLE);
                    intername.setText(carePlanInstruction.getTitle());
                    interdosage.setText(carePlanInstruction.getLabel());
                    instruction.setText(carePlanInstruction.getInstructions());
                }
            });
            readonlyMainLayout.addView(view);
        }
    }

    private void onBindAssessmentData(List<CarePlanModels.CarePlanAssessment> careplanAssessment) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assessmentMainLayout.removeAllViews();

        List<CarePlanModels.CarePlanAssessment> planAssessmentList = new ArrayList<>();

        /*Ascending Assessment Name*/
        ArrayList<String> assessmentNameSorting = new ArrayList<>();
        for(CarePlanModels.CarePlanAssessment name : careplanAssessment){
            assessmentNameSorting.add(name.getName());
        }
        Collections.sort(assessmentNameSorting);
        for(String nameTemp : assessmentNameSorting){
            for(CarePlanModels.CarePlanAssessment nameInter : careplanAssessment){
                if(nameTemp.equalsIgnoreCase(nameInter.getName())){
                    planAssessmentList.add(nameInter);
                }
            }
        }
        if(planAssessmentList != null && planAssessmentList.size() > 0){
            careplanAssessment = new ArrayList<>(planAssessmentList);
        }



        for(final CarePlanModels.CarePlanAssessment assessment : careplanAssessment){
            final View view = inflater.inflate(R.layout.assesment_items_view,null);
            final TextView assName = (TextView)view.findViewById(R.id.assesmentname);
            final TextView assStartTime = (TextView)view.findViewById(R.id.assesmentnameSubName);
            final TextView valueText = (TextView)view.findViewById(R.id.valueText);
            valueText.setVisibility(View.INVISIBLE);
            assName.setText(assessment.getName());
            if(!TextUtils.isEmpty(assessment.getTarget_or_time())){
                assStartTime.setText(assessment.getTarget_or_time());
            }else{
                assStartTime.setText(assessment.getStart_time());
            }
            if(assessment.getPatientAssessment() != null && assessment.getPatientAssessment().size() > 0){
                for(CarePlanModels.CarePlanAssessment.PatientAssessment patientAssessment : assessment.getPatientAssessment()){
                    if(patientAssessment.getAssessment_date().split("T")[0].equalsIgnoreCase(selectedDate)){
                            if(!TextUtils.isEmpty(patientAssessment.getValue())){
                                valueText.setVisibility(View.VISIBLE);
                                valueText.setText(patientAssessment.getValue());
                            }
                            break;
                        }
                    }
                }


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent = new Intent(getActivity(),AssessmentDetailsActivity.class);
                        assessment.setAssessmentDate(selectedDate);
                        assessment.setCareplanAssessmentId(assessment.getId());
                        intent.putExtra(Constants.INTENT_PARM,assessment);
                        startActivityForResult(intent,100);
                    }catch (Exception e){
                        Log.d("parm",e.getMessage());
                    }

                }
            });
            assessmentMainLayout.addView(view);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            getCarePlanDetailsFromAPI(selectedDate);
        }


    }

    private void onBindInterventionData(List<CarePlanModels.CarePlanIntervention> careplanIntervention) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        interventionMainLayout.removeAllViews();
        int totalFrequency = 0;
        List<CarePlanModels.CarePlanIntervention> interventionList = new ArrayList<>();

        /*Ascending Intervention Name*/
        ArrayList<String> interventionNameSorting = new ArrayList<>();
        for(CarePlanModels.CarePlanIntervention name : careplanIntervention){
            interventionNameSorting.add(name.getName());
        }
        Collections.sort(interventionNameSorting);
        for(String nameTemp : interventionNameSorting){
            for(CarePlanModels.CarePlanIntervention nameInter : careplanIntervention){
                if(nameTemp.equalsIgnoreCase(nameInter.getName())){
                    interventionList.add(nameInter);
                }
            }
        }
        if(interventionList != null && interventionList.size() > 0){
            careplanIntervention = new ArrayList<>(interventionList);
        }

        /*Ascending the Frequency*/
        for(CarePlanModels.CarePlanIntervention intervention : careplanIntervention){
            List<CarePlanModels.CarePlanIntervention.InterventionFrequency> frequencyList = new ArrayList<>();
            List<String> integerList = new ArrayList<>();
            for(CarePlanModels.CarePlanIntervention.InterventionFrequency frequency : intervention.getInterventionFrequency()){
                integerList.add(frequency.getReminder());
            }
            Collections.sort(integerList);
            for(String integer : integerList){
                for(CarePlanModels.CarePlanIntervention.InterventionFrequency frequency : intervention.getInterventionFrequency()){
                    if(integer.equalsIgnoreCase(frequency.getReminder())){
                        frequencyList.add(frequency);
                    }
                }
            }
            intervention.setInterventionFrequency(new ArrayList<CarePlanModels.CarePlanIntervention.InterventionFrequency>(frequencyList));
        }


        for(final CarePlanModels.CarePlanIntervention intervention : careplanIntervention){
            final View view = inflater.inflate(R.layout.intervenation_items,null);
            final TextView interventionName = (TextView)view.findViewById(R.id.interventionName);
            final ImageView arrow = (ImageView)view.findViewById(R.id.arrow);
            final TextView interventionAlisName = (TextView)view.findViewById(R.id.interventionAlisName);
            final LinearLayout interventionImageMainLay = (LinearLayout)view.findViewById(R.id.interventionImageMainLay);
            interventionName.setText(intervention.getName());
            interventionAlisName.setText(intervention.getDosage());
            totalFrequency = intervention.getFrequency() + totalFrequency;
            interventionValue = (double) 100/totalFrequency;


            for (final CarePlanModels.CarePlanIntervention.InterventionFrequency frequency :intervention.getInterventionFrequency()){
                frequency.setIs_completed(false);
                frequency.setValue(false);
                for(CarePlanModels.CarePlanIntervention.InterventionDay interventionDay : intervention.getInterventionDay()){
                    if(interventionDay.getDay().split("T")[0].equalsIgnoreCase(selectedDate)){

                        if(interventionDay.getPatientIntervention() != null && interventionDay.getPatientIntervention().size() > 0){
                            for(CarePlanModels.CarePlanIntervention.InterventionDay.PatientIntervention patientIntervention : interventionDay.getPatientIntervention()){
                                if(frequency.getId().equalsIgnoreCase(patientIntervention.getIntervention_frequency_id())){
                                    frequency.setIs_completed(true);

                                }
                            }
                        }

                    }
                }
            }
            int index = 0;
            for (final CarePlanModels.CarePlanIntervention.InterventionFrequency frequency :intervention.getInterventionFrequency()){
                LayoutInflater inflaterFreq = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View viewFreq = inflaterFreq.inflate(R.layout.intervenation_freq_items,null);
                final ImageView imagefreq = (ImageView)viewFreq.findViewById(R.id.imagefreq);
                frequency.setInterventionIndex(String.valueOf(index++));
                frequency.setCareplanInterventionId(frequency.getCareplan_intervention_id());
                frequency.setDay(DateHelper.getCurrentDate());
                if(frequency.isIs_completed()){
                    if(intervention.getColor().equalsIgnoreCase(Constants.COLOR_ORANGE)){
                        imagefreq.setBackground(getResources().getDrawable(R.drawable.orangecircle));
                    } else if(intervention.getColor().equalsIgnoreCase(Constants.COLOR_PURPLE)){
                        imagefreq.setBackground(getResources().getDrawable(R.drawable.purplecircle));
                    }else{
                        imagefreq.setBackground(getResources().getDrawable(R.drawable.bluecircle));
                    }
                }else{
                    if(intervention.getColor().equalsIgnoreCase(Constants.COLOR_ORANGE)){
                        imagefreq.setBackground(getResources().getDrawable(R.drawable.orangeborder));
                    } else if(intervention.getColor().equalsIgnoreCase(Constants.COLOR_PURPLE)){
                        imagefreq.setBackground(getResources().getDrawable(R.drawable.purpleborder));
                    }else{
                        imagefreq.setBackground(getResources().getDrawable(R.drawable.blueborder));
                    }
                }

                imagefreq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NetworkUtils.isNetworkAvailable(getActivity())){
                            if(frequency.isIs_completed()){
                                imagefreq.setBackground(getResources().getDrawable(R.drawable.blueborder));
                                frequency.setIs_completed(false);
                                frequency.setValue(false);
                                if(intervention.getColor().equalsIgnoreCase(Constants.COLOR_ORANGE)){
                                    imagefreq.setBackground(getResources().getDrawable(R.drawable.orangeborder));
                                } else if(intervention.getColor().equalsIgnoreCase(Constants.COLOR_PURPLE)){
                                    imagefreq.setBackground(getResources().getDrawable(R.drawable.purpleborder));
                                }else{
                                    imagefreq.setBackground(getResources().getDrawable(R.drawable.blueborder));
                                }

                            }else{
                                imagefreq.setBackground(getResources().getDrawable(R.drawable.bluecircle));
                                frequency.setIs_completed(true);
                                frequency.setValue(true);
                                if(intervention.getColor().equalsIgnoreCase(Constants.COLOR_ORANGE)){
                                    imagefreq.setBackground(getResources().getDrawable(R.drawable.orangecircle));
                                } else if(intervention.getColor().equalsIgnoreCase(Constants.COLOR_PURPLE)){
                                    imagefreq.setBackground(getResources().getDrawable(R.drawable.purplecircle));
                                }else{
                                    imagefreq.setBackground(getResources().getDrawable(R.drawable.bluecircle));
                                }

                            }
                            sendUpdateInterventionDetails(frequency);
                        }


                    }
                });
                interventionImageMainLay.addView(viewFreq);
            }






            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calenderView.setVisibility(View.GONE);
                    detailsView.setVisibility(View.VISIBLE);
                    intername.setText(intervention.getName());
                    interdosage.setText(intervention.getDosage());
                    instruction.setText(intervention.getInstructions());
                }
            });
            interventionMainLayout.addView(view);
        }

       /* for(CarePlanModels.CarePlanIntervention intervention : careplanIntervention){
            for(CarePlanModels.CarePlanIntervention.InterventionFrequency frequency : intervention.getInterventionFrequency()){
                if(frequency.isIs_completed()){
                    completionPercentage = completionPercentage+interventionValue;
                }
            }
        }*/


    }

    private void sendUpdateInterventionDetails(CarePlanModels.CarePlanIntervention.InterventionFrequency frequency) {

        APIRepository apiRepository = new APIRepository(getActivity());
        showProgressBar("please wait..");
        apiRepository.setGetAPIResponseModel(new APIRepository.GetAPIResponseModel() {
            @Override
            public void getAPIResponseModelSuccess(APIResponseModels apiResponseModels) {
                if(apiResponseModels != null){
                    if(apiResponseModels.isStatus()){
                        //PreferenceUtils.setCarePlanList(getActivity(),null);
                        //PreferenceUtils.setCarePlanList(getActivity(),carePlanList);
                        getCarePlanDetailsFromAPI(selectedDate);
                        //onBindOverAllDetails();
                    }else if(apiResponseModels.getError() != null){
                        Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT);
                        getCarePlanDetailsFromAPI(selectedDate);
                    }
                }
            }

            @Override
            public void getAPIResponseModelFailure(String s) {
                hideProgress();
                Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_SHORT);
            }
        });
        frequency.setLastSyncDate(PreferenceUtils.getLastSyncDate(getActivity()));
        frequency.setPatientId(PreferenceUtils.getPatientId(getActivity()));
        frequency.setUserId(PreferenceUtils.getUserId(getActivity()));
        frequency.setDay(selectedDate);

/*        CarePlanModels carePlanModels = new CarePlanModels();
        carePlanModels.setCareplanInterventionId(frequency.getCareplanInterventionId());
        carePlanModels.setDay(DateHelper.getCurrentDate());
        carePlanModels.setInterventionIndex(frequency.getInterventionIndex());
        carePlanModels.setLastSyncDate(PreferenceUtils.getLastSyncDate(getActivity()));
        carePlanModels.setPatientId(PreferenceUtils.getPatientId(getActivity()));
        carePlanModels.setUserId(PreferenceUtils.getUserId(getActivity()));
        carePlanModels.setValue(frequency.isValue());*/

        apiRepository.updateIntervention(frequency);


    }


    private void intializeViews() {
        tpHeaderModelList = new ArrayList<>();
        progressBar = new ProgressDialog(getActivity());
        carePlanCalenderRecyclerView = (RecyclerView)mView.findViewById(R.id.careplancalender);
        percentIndicatior = (TextView)mView.findViewById(R.id.percentIndicatior);
        dateText = (TextView)mView.findViewById(R.id.date);
        today = (TextView)mView.findViewById(R.id.today);
        donut_progress = (CircularProgressBar)mView.findViewById(R.id.donut_progress);
        interventionMainLayout = (LinearLayout)mView.findViewById(R.id.interventionMainLayout);
        assessmentMainLayout = (LinearLayout)mView.findViewById(R.id.assessmentMainLayout);
        readonlyMainLayout = (LinearLayout)mView.findViewById(R.id.readonlyMainLayout);
        detailsView = (LinearLayout)mView.findViewById(R.id.detailsView);
        calenderView = (LinearLayout)mView.findViewById(R.id.calenderView);
        intername = (TextView)mView.findViewById(R.id.intername);
        interdosage = (TextView)mView.findViewById(R.id.interdosage);
        instruction = (TextView)mView.findViewById(R.id.instruction);
        back = (TextView)mView.findViewById(R.id.back);
        today.setOnClickListener(this);
        back.setOnClickListener(this);


    }
    private void bindCalenderDate(List<CarePlanModels> carePlanList) {

        try {
            //To get start and end date of the dcr calendar dates.
            Calendar startDate =  Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            CarePlanModels tpHeader = new CarePlanModels();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            startDate  = Calendar.getInstance();
            startDate.setTime(df.parse(calenderStartDate));
            endDate = Calendar.getInstance();
            endDate.setTime(df.parse(calenderEndDate));
            int position = 0;
            tpHeaderModelList = new ArrayList<>();
            while (startDate.getTime().before(endDate.getTime())) {
                startDate.add(Calendar.DATE, 1);
                tpHeader = new CarePlanModels();
                tpHeader.setDates(DateHelper.convertDateToString(startDate.getTime(), "yyyy-MM-dd"));
                tpHeader.setPosition(position++);
                    if(selectedDate.equalsIgnoreCase(tpHeader.getDates())) {
                        selectedItemposition = tpHeader.getPosition();
                        currentDatePosition = tpHeader.getPosition();
                        tpHeader.setSelected(true);
                        tpHeader.setToday(true);
                    }
                 tpHeader.setProgressValue(calculateCompletionValue(tpHeader.getDates()));
                tpHeaderModelList.add(tpHeader);
            }
            if (tpHeaderModelList != null && tpHeaderModelList.size() > 0) {
                toSetMonthAndDay(tpHeaderModelList);
                bindCalenderAdapter();
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "getCalendaerDate: " + ex.getMessage(), Toast.LENGTH_LONG);
        }

        onBindOverAllDetails();
    }



    private void bindCalenderAdapter() {
        carePlanCalenderRecyclerView.setLayoutManager(new LinearLayoutManager(carePlanCalenderRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        carePlanAdapter = new CarePlanAdapter(CarePlanFragment.this, tpHeaderModelList);
        carePlanAdapter.setOnTpDateClickListener(this);
        carePlanCalenderRecyclerView.setAdapter(carePlanAdapter);
        moveToToday(selectedItemposition);

    }

    void moveToToday(int selectedItemposition){
        try {
            carePlanCalenderRecyclerView.scrollToPosition(selectedItemposition);
        }catch (Exception e){
         Log.d("parm ",e.getMessage());
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDateClick(int position) {
        imagetwobool = false;
        imageonebool = false;
        isdateAlreadySelect = true;
        selectedItemposition = position;
        tpHeaderModelList.get(position).setSelected(true);
        carePlanAdapter.notifyDataSetChanged();
        dateText.setText( DateHelper.getDisplayFormat(tpHeaderModelList.get(position).getDates(),"yyyy-MM-dd"));
        selectedDate = tpHeaderModelList.get(position).getDates();
        if(carePlanList != null){
            onBindOverAllDetails();
        }
        //setCompletion((int)tpHeaderModelList.get(position).getProgressValue());
        //getCarePlanDetailsFromAPI(tpHeaderModelList.get(position).getDates());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.today:
                selectedDate = DateHelper.getCurrentDate();
                getCarePlanDetailsFromAPI(DateHelper.getCurrentDate());
                break;

            case R.id.back:
                calenderView.setVisibility(View.VISIBLE);
                detailsView.setVisibility(View.GONE);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
    }




    public void toSetMonthAndDay(List<CarePlanModels> tpHeaderModelList){
        for(CarePlanModels tpHeaderModel : tpHeaderModelList){
            try {
                //  Date date = NewTPActivity.convertStringToDate(tpHeaderModel.getTP_Date(), "yyyy-MM-dd");
                Date date = convertStringToDate(tpHeaderModel.getDates(),"yyyy-MM-dd");
                String formattedDate =  formatDate(date, "yyyy-MMM-dd-EEE");
                String formatDay = formatDate(date, "yyyy-MMM-dd-EEEE");
                String[] formattedString = formattedDate.split("-");
                tpHeaderModel.setYearString(formattedString[0]);
                tpHeaderModel.setMonthString(formattedString[1]);
                tpHeaderModel.setDayInt(Integer.parseInt(formattedString[2]));
                tpHeaderModel.setDayString(formattedString[3]);
                tpHeaderModel.setTP_Full_Day(formatDay.split("-")[3]);
            }catch (Exception e){
                Log.e("TpPlanner", "" + e);
            }

        }
    }
    public static Date convertStringToDate(String dateInString, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = formatter.parse(dateInString);
        return date;
    }

    public static String formatDate(Date date, String dateFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String sDate = sdf.format(date);
        return sDate;
    }

    void showProgressBar(String message){
        progressBar.setMessage(message);
        progressBar.setCancelable(false);
        progressBar.show();
    }
    void hideProgress(){
        progressBar.hide();
    }

    void setCompletion(float value){
        donut_progress.setProgress((int) Math.round(value));
        percentIndicatior.setText("Your care overview is "+String.valueOf(Math.round(value))+"% complete");

/*        tpHeaderModelList.get(selectedItemposition).setProgressValue((int) Math.round(value));
        carePlanAdapter.notifyItemChanged(selectedItemposition);*/

    }
    int  calculateCompletionValue(String date){
        int totalFrequencyHeader = 0;
        double completionPercentageHeader=0;
        double interventionValueHeader = 0;

        for(final CarePlanModels.CarePlanIntervention intervention : carePlanList.get(0).getCareplanIntervention()) {
            totalFrequencyHeader = intervention.getFrequency() + totalFrequencyHeader;
        }
        if(carePlanList.get(0).getCareplanAssessment() != null) {
            totalFrequencyHeader = totalFrequencyHeader + carePlanList.get(0).getCareplanAssessment().size();
        }

        for(final CarePlanModels.CarePlanIntervention intervention : carePlanList.get(0).getCareplanIntervention()) {
            interventionValueHeader = (double) 100 / totalFrequencyHeader;
            for (final CarePlanModels.CarePlanIntervention.InterventionFrequency frequency : intervention.getInterventionFrequency()) {
                for (CarePlanModels.CarePlanIntervention.InterventionDay interventionDay : intervention.getInterventionDay()) {
                    if (interventionDay.getDay().split("T")[0].equalsIgnoreCase(date)) {
                        if (interventionDay.getPatientIntervention() != null && interventionDay.getPatientIntervention().size() > 0) {
                            for (CarePlanModels.CarePlanIntervention.InterventionDay.PatientIntervention patientIntervention : interventionDay.getPatientIntervention()) {
                                if (frequency.getId().equalsIgnoreCase(patientIntervention.getIntervention_frequency_id())) {
                                    completionPercentageHeader = completionPercentageHeader+interventionValueHeader;

                                }
                            }
                        }

                    }
                }
            }
        }

        for(final CarePlanModels.CarePlanAssessment assessment : carePlanList.get(0).getCareplanAssessment()) {
            for(CarePlanModels.CarePlanAssessment.PatientAssessment patientAssessment : assessment.getPatientAssessment()){
                if(patientAssessment.getAssessment_date().split("T")[0].equalsIgnoreCase(date)){
                    completionPercentageHeader = completionPercentageHeader+interventionValueHeader;
                }
            }

        }
        if((int) completionPercentageHeader > 100){
            return 100;
        }else if((int) completionPercentageHeader <0){
            return 0;
        }

        return (int) completionPercentageHeader;
    }
    void showAlertMessage(final String message){
        new IOSDialogBuilder(getActivity())
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
                        if(message.equalsIgnoreCase("No Care plan is assigned to this patient")){
                            Intent intent = new Intent(getActivity(),TimeOutActivity.class);
                            PreferenceUtils.clearAllData(getActivity());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                    }
                })
                .build().show();

    }

}

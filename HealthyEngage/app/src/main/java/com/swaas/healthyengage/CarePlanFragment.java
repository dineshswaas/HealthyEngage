package com.swaas.healthyengage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Repositories.APIRepository;
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

/*        PreferenceUtils.setAuthorizationKey(getActivity(),"LpWno3DZs4dnmcoejrGPaWBFlk5DwSxETal0DMMJJmkJQEME8zWzDrtIBM8wU6Ea");
        PreferenceUtils.setCarePlanId(getActivity(),"60b0d2ff-8fde-401e-8fc3-9454ca46702b");
        PreferenceUtils.setPatientId(getActivity(),"52575534-bcee-40f0-b6b5-0612bfe7db06");
        PreferenceUtils.setUserId(getActivity(),"e031d072-14db-4b10-abd6-0e18e5daf0c4");
        PreferenceUtils.setLastSyncDate(getActivity(),"2019-11-07T00:00:00.000Z");*/

        //ProductionUser
/*        PreferenceUtils.setAuthorizationKey(getActivity(),"OJkUbcTXbGLTXeiiBV0yw1RadXZ9KWeojMFjn9P4X2iDc4MCnqMQ4oVzCwentCV7");
        PreferenceUtils.setCarePlanId(getActivity(),"694401f8-eb1e-4896-b2bb-3f2ebcf3d957");
        PreferenceUtils.setPatientId(getActivity(),"3f6e4590-cf2f-41bd-b1e2-d301d8108cbf");*/


        selectedDate = DateHelper.getCurrentDate();
        getCarePlanDetailsFromAPI(DateHelper.getCurrentDate());

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
                PreferenceUtils.setLastSyncDate(getActivity(),null);
                PreferenceUtils.setLastSyncDate(getActivity(),lastSyncDate);
                calenderStartDate = carePlanList.get(0).getCurrent_cycle_start_date().split("T")[0];
                calenderEndDate = carePlanList.get(0).getCurrent_cycle_end_date().split("T")[0];
                bindCalenderDate();
                onBindOverAllDetails();

            }

            @Override
            public void getCarePlanFailure(String s) {
                Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                hideProgress();
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
        setCompletion(0);
        completionPercentage = 0;
        interventionValue = 0;
        String localListValue = PreferenceUtils.getCarePlanList(getActivity());
        if(localListValue != null){
        carePlanList = DeserializeCarePlan(localListValue);
        }
        onBindInterventionData(carePlanList.get(0).getCareplanIntervention());
        onBindAssessmentData(carePlanList.get(0).getCareplanAssessment());
        onBindReadOnly(carePlanList.get(0).getCareplanInstruction());
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
        for(final CarePlanModels.CarePlanAssessment assessment : careplanAssessment){
            final View view = inflater.inflate(R.layout.assesment_items_view,null);
            final TextView assName = (TextView)view.findViewById(R.id.assesmentname);
            final TextView assStartTime = (TextView)view.findViewById(R.id.assesmentnameSubName);
            assName.setText(assessment.getName());
            if(!TextUtils.isEmpty(assessment.getTarget_or_time())){
                assStartTime.setText(assessment.getTarget_or_time());
            }else{
                assStartTime.setText(assessment.getStart_time());
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent = new Intent(getActivity(),AssessmentDetailsActivity.class);
                        intent.putExtra(Constants.INTENT_PARM,assessment);
                        startActivity(intent);
                    }catch (Exception e){
                        Log.d("parm",e.getMessage());
                    }

                }
            });
            assessmentMainLayout.addView(view);
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
            interventionValue = (double) 50/totalFrequency;


            for (final CarePlanModels.CarePlanIntervention.InterventionFrequency frequency :intervention.getInterventionFrequency()){

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
                    imagefreq.setBackground(getResources().getDrawable(R.drawable.bluecircle));
                }else{
                    imagefreq.setBackground(getResources().getDrawable(R.drawable.blueborder));
                }

                imagefreq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NetworkUtils.isNetworkAvailable(getActivity())){
                            if(frequency.isIs_completed()){
                                completionPercentage = completionPercentage - interventionValue;
                                imagefreq.setBackground(getResources().getDrawable(R.drawable.blueborder));
                                frequency.setIs_completed(false);
                                frequency.setValue(false);
                                setCompletion((int)completionPercentage);
                            }else{
                                completionPercentage = completionPercentage+interventionValue;
                                imagefreq.setBackground(getResources().getDrawable(R.drawable.bluecircle));
                                frequency.setIs_completed(true);
                                frequency.setValue(true);
                                setCompletion((int)completionPercentage);
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

        for(CarePlanModels.CarePlanIntervention intervention : careplanIntervention){
            for(CarePlanModels.CarePlanIntervention.InterventionFrequency frequency : intervention.getInterventionFrequency()){
                if(frequency.isIs_completed()){
                    completionPercentage = completionPercentage+interventionValue;
                }
            }
        }
        setCompletion((int)completionPercentage);

    }

    private void sendUpdateInterventionDetails(CarePlanModels.CarePlanIntervention.InterventionFrequency frequency) {

        APIRepository apiRepository = new APIRepository(getActivity());

        apiRepository.setGetAPIResponseModel(new APIRepository.GetAPIResponseModel() {
            @Override
            public void getAPIResponseModelSuccess(APIResponseModels apiResponseModels) {
                if(apiResponseModels != null){
                    if(apiResponseModels.isStatus()){
                        PreferenceUtils.setCarePlanList(getActivity(),null);
                        PreferenceUtils.setCarePlanList(getActivity(),carePlanList);
                        onBindOverAllDetails();
                    }else if(apiResponseModels.getError() != null){

                    }
                }
            }

            @Override
            public void getAPIResponseModelFailure(String s) {

            }
        });
        frequency.setLastSyncDate(PreferenceUtils.getLastSyncDate(getActivity()));
        frequency.setPatientId(PreferenceUtils.getPatientId(getActivity()));
        frequency.setUserId(PreferenceUtils.getUserId(getActivity()));
        frequency.setDay(DateHelper.getCurrentDate());

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
    private void bindCalenderDate() {

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
                if(DateHelper.getCurrentDate().equalsIgnoreCase(tpHeader.getDates())){
                    currentDatePosition = position;
                    tpHeader.setSelected(true);
                    tpHeader.setToday(true);
                }

                tpHeaderModelList.add(tpHeader);
            }
            if (tpHeaderModelList != null && tpHeaderModelList.size() > 0) {
                toSetMonthAndDay(tpHeaderModelList);
                bindCalenderAdapter();
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "getCalendaerDate: " + ex.getMessage(), Toast.LENGTH_LONG);
        }


    }

    private void bindCalenderAdapter() {
        carePlanCalenderRecyclerView.setLayoutManager(new LinearLayoutManager(carePlanCalenderRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        carePlanAdapter = new CarePlanAdapter(CarePlanFragment.this, tpHeaderModelList);
        carePlanAdapter.setOnTpDateClickListener(this);
        carePlanCalenderRecyclerView.setAdapter(carePlanAdapter);
        carePlanCalenderRecyclerView.scrollToPosition(currentDatePosition);
    }

    void moveToToday(){
        carePlanCalenderRecyclerView.scrollToPosition(currentDatePosition);
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
    }

}

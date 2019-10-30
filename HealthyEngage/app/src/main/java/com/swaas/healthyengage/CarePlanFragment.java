package com.swaas.healthyengage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.circularprogressbar.CircularProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Repositories.CarePlanRepository;
import models.CarePlanModels;
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
    CarePlanRepository carePlanRepository;
    List<CarePlanModels> carePlanList;
    LinearLayout interventionMainLayout,assessmentMainLayout,readonlyMainLayout,detailsView,calenderView;
    ProgressDialog progressBar;
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
        bindCalenderDate();
        PreferenceUtils.setAuthorizationKey(getActivity(),"OJkUbcTXbGLTXeiiBV0yw1RadXZ9KWeojMFjn9P4X2iDc4MCnqMQ4oVzCwentCV7");
        PreferenceUtils.setCarePlanId(getActivity(),"b5948dee-e8b7-4c89-8366-f9f392fc0aeb");
        PreferenceUtils.setPatientId(getActivity(),"3f6e4590-cf2f-41bd-b1e2-d301d8108cbf");
        getCarePlanDetailsFromAPI(DateHelper.getCurrentDate());

        return mView;
    }

    private void getCarePlanDetailsFromAPI(String date) {

        carePlanRepository = new CarePlanRepository(getActivity());
        dateText.setText(DateHelper.getDisplayFormat(date,"yyyy-MM-dd"));
        carePlanList = new ArrayList<>();
        carePlanRepository.setGetCarePlanDetails(new CarePlanRepository.GetCarePlanModelDetails() {
            @Override
            public void getCarePlanSuccess(List<CarePlanModels> carePlanModels) {
                carePlanList = new ArrayList<>(carePlanModels);
                onBindInterventionData(carePlanList.get(0).getCareplanIntervention());
                onBindAssessmentData(carePlanList.get(0).getCareplanAssessment());
                onBindReadOnly(carePlanList.get(0).getCareplanInstruction());
                hideProgress();
            }

            @Override
            public void getCarePlanFailure(String s) {
                hideProgress();
            }
        });
        CarePlanModels carePlanModels = new CarePlanModels();
        carePlanModels.setCarePlanId(PreferenceUtils.getCarePlanId(getActivity()));
        carePlanModels.setDay(date);
        carePlanModels.setPatientId(PreferenceUtils.getPatientId(getActivity()));
        if(NetworkUtils.isNetworkAvailable(getActivity())){
            showProgressBar("Fetching care plan details for "+DateHelper.getDisplayFormat(date,"yyyy-MM-dd"));
            carePlanRepository.getCarePlanDetails(carePlanModels);
        }

    }

    private void onBindReadOnly(List<CarePlanModels.CarePlanInstruction> careplanInstruction) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        readonlyMainLayout.removeAllViews();
        for(CarePlanModels.CarePlanInstruction carePlanInstruction : careplanInstruction){
            final View view = inflater.inflate(R.layout.instruction_items_view,null);
            final TextView instrName = (TextView)view.findViewById(R.id.instructionname);
            final TextView lable = (TextView)view.findViewById(R.id.instructionlabel);
            instrName.setText(carePlanInstruction.getTitle());
            lable.setText(carePlanInstruction.getLabel());
            readonlyMainLayout.addView(view);
        }
    }

    private void onBindAssessmentData(List<CarePlanModels.CarePlanAssessment> careplanAssessment) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assessmentMainLayout.removeAllViews();
        for(CarePlanModels.CarePlanAssessment assessment : careplanAssessment){
            final View view = inflater.inflate(R.layout.assesment_items_view,null);
            final TextView assName = (TextView)view.findViewById(R.id.assesmentname);
            final TextView assStartTime = (TextView)view.findViewById(R.id.assesmentnameSubName);
            assName.setText(assessment.getName());
            assStartTime.setText(assessment.getStart_time());
            assessmentMainLayout.addView(view);
        }

    }

    private void onBindInterventionData(List<CarePlanModels.CarePlanIntervention> careplanIntervention) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        interventionMainLayout.removeAllViews();
        for(final CarePlanModels.CarePlanIntervention intervention : careplanIntervention){
            final View view = inflater.inflate(R.layout.intervenation_items,null);
            final TextView interventionName = (TextView)view.findViewById(R.id.interventionName);
            final TextView interventionAlisName = (TextView)view.findViewById(R.id.interventionAlisName);
            final LinearLayout interventionImageMainLay = (LinearLayout)view.findViewById(R.id.interventionImageMainLay);
            interventionName.setText(intervention.getName());
            interventionAlisName.setText(intervention.getDosage());

            LayoutInflater inflaterFreq = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for(int i = 1;i<=intervention.getFrequency();i++){
                final View viewFreq = inflaterFreq.inflate(R.layout.intervenation_freq_items,null);
                final ImageView imagefreq = (ImageView)viewFreq.findViewById(R.id.imagefreq);
                interventionImageMainLay.addView(viewFreq);
            }
            view.setOnClickListener(new View.OnClickListener() {
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
            Calendar currentDate =  Calendar.getInstance();
            currentDate.add(Calendar.MONTH, -2);
            currentDate.set(Calendar.DAY_OF_MONTH, 1);
            Calendar startDate =  Calendar.getInstance();
            startDate.setTime(currentDate.getTime());
            startDate.set(Calendar.HOUR_OF_DAY, 0);
            startDate.set(Calendar.MINUTE, 0);
            startDate.set(Calendar.SECOND, 0);
            startDate.set(Calendar.MILLISECOND, 0);
            CarePlanModels tpHeader = new CarePlanModels();
            tpHeader.setDates(DateHelper.convertDateToString(startDate.getTime(), "yyyy-MM-dd"));


            currentDate.add(Calendar.MONTH, 4);
            currentDate.set(Calendar.DAY_OF_MONTH, currentDate.getActualMaximum(Calendar.DATE));
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(currentDate.getTime());
            endDate.set(Calendar.HOUR_OF_DAY, 0);
            endDate.set(Calendar.MINUTE, 0);
            endDate.set(Calendar.SECOND, 0);
            endDate.set(Calendar.MILLISECOND, 0);
            tpHeaderModelList.add(tpHeader);
            while (startDate.getTime().before(endDate.getTime())) {
                startDate.add(Calendar.DATE, 1);
                tpHeader = new CarePlanModels();
                tpHeader.setDates(DateHelper.convertDateToString(startDate.getTime(), "yyyy-MM-dd"));


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
        getCarePlanDetailsFromAPI(tpHeaderModelList.get(position).getDates());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.today:
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
        progressBar.show();
    }
    void hideProgress(){
        progressBar.hide();
    }
}

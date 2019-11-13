package com.swaas.healthyengage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Repositories.APIRepository;
import models.ConnectAPIModel;
import models.Delegates;
import utils.Constants;
import utils.PreferenceUtils;

/**
 * Created by Adib on 13-Apr-17.
 */

public class ConnectFragment extends Fragment implements ConnectAdapter.OnCareClick{

    ProgressDialog progressBar;
    APIRepository apiRepository;
    ConnectAPIModel connectAPIModel;
    TextView firstletter,loginname;
    RecyclerView connectRecycler;
    ConnectAdapter connectAdapter;
    List<ConnectAPIModel> connectAPIModelList;
    View mView;
    LinearLayout addDelegate;
    public static ConnectFragment newInstance() {
        return new ConnectFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView =  inflater.inflate(R.layout.fragment_connect, container, false);
        initializeViews();
        getConnectDetails();
        return mView;

    }

    private void initializeViews() {
        firstletter = (TextView)mView.findViewById(R.id.firstletter);
        loginname = (TextView)mView.findViewById(R.id.loginname);
        addDelegate = (LinearLayout)mView.findViewById(R.id.addDelegate);
        connectRecycler = (RecyclerView)mView.findViewById(R.id.connectRecycler);
        if(!TextUtils.isEmpty(PreferenceUtils.getDelegateId(getActivity()))){
            addDelegate.setVisibility(View.INVISIBLE);
        }

        addDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddDelegateActivity.class));
            }
        });
    }

    private void getConnectDetails() {
        showProgressBar("Fetching caretakers details");
        apiRepository = new APIRepository(getActivity());
        apiRepository.setGetCareTakersDetails(new APIRepository.GetCareTakersDetails() {
            @Override
            public void getCareTakersSuccess(ConnectAPIModel connectAPI) {
                hideProgress();
                onBindConnectList(connectAPI);

            }

            @Override
            public void getCareTakersFailure(String s) {
                hideProgress();
                Log.d("parmconnect",s);
            }
        });
        ConnectAPIModel connectAPIModel = new ConnectAPIModel();
        if(!TextUtils.isEmpty(PreferenceUtils.getDelegateId(getActivity()))){
            connectAPIModel.setIs_patient(false);
        }else{
            connectAPIModel.setIs_patient(true);
        }

        apiRepository.getCareTakers(connectAPIModel);

    }

    private void onBindConnectList(ConnectAPIModel connectAPI) {
        connectAPIModelList = new ArrayList<>();
        ConnectAPIModel header_1 = new ConnectAPIModel();
        header_1.setHeaderText("CARE TEAM");
        connectAPIModelList.add(header_1);
        ConnectAPIModel.Author author = connectAPI.getAuthor();
        if(author != null){
            ConnectAPIModel connectAPIModel = new ConnectAPIModel();
            connectAPIModel.setId(author.getId());;
            connectAPIModel.setFirst_name(author.getFirst_name());
            connectAPIModel.setLast_name(author.getLast_name());
            connectAPIModel.setEmail(author.getEmail());
            connectAPIModel.setMobile_no(author.getMobile_no());
            connectAPIModel.setGender(author.getGender());
            connectAPIModel.setCountry_code(author.getCountry_code());
            connectAPIModel.setFromPatient(false);
            connectAPIModelList.add(connectAPIModel);
        }


        if(connectAPI.getNavigator() != null){
            ConnectAPIModel.Navigator navigator = connectAPI.getNavigator();
            ConnectAPIModel connectAPIModel = new ConnectAPIModel();
            connectAPIModel.setId(navigator.getId());;
            connectAPIModel.setFirst_name(navigator.getFirst_name());
            connectAPIModel.setLast_name(navigator.getLast_name());
            connectAPIModel.setEmail(navigator.getEmail());
            connectAPIModel.setMobile_no(navigator.getMobile_no());
            connectAPIModel.setGender(navigator.getGender());
            connectAPIModel.setFromPatient(false);
            connectAPIModel.setCountry_code(navigator.getCountry_code());
            connectAPIModelList.add(connectAPIModel);
        }


        if(connectAPI.getOrganisation() != null){
            ConnectAPIModel.Organisation organisation = connectAPI.getOrganisation();
            ConnectAPIModel connectAPIModel = new ConnectAPIModel();
            connectAPIModel.setId(organisation.getId());;
            connectAPIModel.setFirst_name("Emergency");
            connectAPIModel.setLast_name("Care");
            connectAPIModel.setTime_zone(organisation.getTime_zone());
            connectAPIModel.setEmergency_number(organisation.getEmergency_number());
            connectAPIModel.setShift_start_time(organisation.getShift_start_time());
            connectAPIModel.setShift_end_time(organisation.getShift_end_time());
            connectAPIModel.setFromPatient(false);
            connectAPIModelList.add(connectAPIModel);
        }


        if(connectAPI.getPatient() != null && !TextUtils.isEmpty(connectAPI.getMobile_no()) && !TextUtils.isEmpty(connectAPI.getFirst_name())){
            ConnectAPIModel header_2 = new ConnectAPIModel();
            header_2.setHeaderText("MY PATIENT");
            connectAPIModelList.add(header_2);
            ConnectAPIModel.Patient patient = connectAPI.getPatient();
            ConnectAPIModel connectAPIModel = new ConnectAPIModel();
            connectAPIModel.setId(patient.getId());;
            connectAPIModel.setFirst_name(patient.getFirst_name());
            connectAPIModel.setLast_name(patient.getLast_name());
            connectAPIModel.setEmail(patient.getEmail());
            connectAPIModel.setMobile_no(patient.getMobile_no());
            connectAPIModel.setGender(patient.getGender());
            connectAPIModel.setCountry_code(patient.getCountry_code());
            connectAPIModel.setFromPatient(true);
            connectAPIModelList.add(connectAPIModel);
        }

        if(connectAPI.getDelegates() != null && connectAPI.getDelegates().size() > 0){
            List<ConnectAPIModel.Delegates> delegatesList = new ArrayList<>(connectAPI.getDelegates());
            ConnectAPIModel header_3 = new ConnectAPIModel();
            header_3.setHeaderText("MY DELEGATES");
            connectAPIModelList.add(header_3);
            for(ConnectAPIModel.Delegates delegates :delegatesList){
                    ConnectAPIModel connectAPIModel = new ConnectAPIModel();
                    connectAPIModel.setId(delegates.getId());;
                    connectAPIModel.setFirst_name(delegates.getFirst_name());
                    connectAPIModel.setLast_name(delegates.getLast_name());
                    ConnectAPIModel.Delegates.RelationshipCategory relationshipCategories = delegates.getRelationshipCategory();
                    if(relationshipCategories != null){
                        if(!delegates.isIs_active()){
                            connectAPIModel.setRelationName(relationshipCategories.getName() +" (Awaiting Confirmation)");
                        }

                    }
                    Delegates delegateForMobile = delegates.getDelegates();
                    connectAPIModel.setMobile_no(delegateForMobile.getUser().getMobile_no());
                    connectAPIModel.setCountry_code(delegateForMobile.getUser().getCountry_code());
                    connectAPIModel.setIs_Delegate(true);
                    connectAPIModel.setFromPatient(false);
                    connectAPIModelList.add(connectAPIModel);

            }

        }




        connectRecycler.setLayoutManager(new LinearLayoutManager(connectRecycler.getContext(), LinearLayoutManager.VERTICAL, false));
        connectAdapter = new ConnectAdapter(ConnectFragment.this,connectAPIModelList);
        connectAdapter.setOnCareClickClickListener(this);
        connectRecycler.addItemDecoration(new DividerItemDecoration(connectRecycler.getContext(), DividerItemDecoration.VERTICAL));
        connectRecycler.setAdapter(connectAdapter);
    }


    void showProgressBar(String message){
        progressBar = new ProgressDialog(getActivity());
        progressBar.setMessage(message);
        progressBar.show();
    }
    void hideProgress(){
        progressBar.hide();
    }

    @Override
    public void onCareClick(int position) {

        Intent intent = new Intent(getActivity(),CareTakersDetailsActivity.class);
        intent.putExtra(Constants.INTENT_PARM, (Serializable) connectAPIModelList.get(position));
        startActivity(intent);

    }
}

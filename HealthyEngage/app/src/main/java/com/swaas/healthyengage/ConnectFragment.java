package com.swaas.healthyengage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Repositories.APIRepository;
import models.ConnectAPIModel;

/**
 * Created by Adib on 13-Apr-17.
 */

public class ConnectFragment extends Fragment{

    ProgressDialog progressBar;
    APIRepository apiRepository;
    ConnectAPIModel connectAPIModel;
    TextView firstletter,loginname;
    RecyclerView connectRecycler;
    ConnectAdapter connectAdapter;
    List<ConnectAPIModel> connectAPIModelList;
    View mView;
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
        connectRecycler = (RecyclerView)mView.findViewById(R.id.connectRecycler);
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
        apiRepository.getCareTakers();

    }

    private void onBindConnectList(ConnectAPIModel connectAPI) {

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
            if(connectAPI.getOrganisation() != null){
                connectAPIModel.setShift_start_time(connectAPI.getShift_start_time());
                connectAPIModel.setShift_end_time(connectAPI.getShift_end_time());
            }

            connectAPIModel.setCountry_code(navigator.getCountry_code());
            connectAPIModelList.add(connectAPIModel);
        }


        if(connectAPI.getOrganisation() != null){
            ConnectAPIModel.Organisation organisation = connectAPI.getOrganisation();
            ConnectAPIModel connectAPIModel = new ConnectAPIModel();
            connectAPIModel.setId(organisation.getId());;
            connectAPIModel.setFirst_name("Emergency Care");
            connectAPIModel.setTime_zone(organisation.getTime_zone());
            connectAPIModel.setEmergency_number(organisation.getEmergency_number());
            connectAPIModel.setFromPatient(false);
            connectAPIModelList.add(connectAPIModel);
        }


        if(connectAPI.getPatient() != null){
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

        connectAdapter = new ConnectAdapter(ConnectFragment.this,connectAPIModelList);

    }


    void showProgressBar(String message){
        progressBar = new ProgressDialog(getActivity());
        progressBar.setMessage(message);
        progressBar.show();
    }
    void hideProgress(){
        progressBar.hide();
    }

}

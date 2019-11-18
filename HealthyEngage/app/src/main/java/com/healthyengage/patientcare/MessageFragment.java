package com.healthyengage.patientcare;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import Repositories.APIRepository;
import models.PatientMessageModels;
import utils.NetworkUtils;

/**
 * Created by Adib on 13-Apr-17.
 */

public class MessageFragment extends Fragment {


    private RecyclerView recyclerView;
    View view;
    List<PatientMessageModels> patientMessageModelsList;
    APIRepository apiRepository;
    ProgressDialog progressBar;
    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_message, container, false);
        intializeViews();
        getPatientMessage();
        return view;
    }

    private void intializeViews() {
        recyclerView = (RecyclerView)view.findViewById(R.id.messagesRecycler);

    }

    private void getPatientMessage() {

        apiRepository = new APIRepository(getActivity());
        patientMessageModelsList = new ArrayList<>();
        apiRepository.setGetPatientMessage(new APIRepository.GerPatientMessages() {
            @Override
            public void getPatientMessages(List<PatientMessageModels> patientMessageModels) {
                patientMessageModelsList = new ArrayList<>(patientMessageModels);
                onBindPatientAdapter();
                hideProgress();
            }

            @Override
            public void getPatientMessagesFailure(String s) {
                hideProgress();
            }
        });

        PatientMessageModels patientMessageModels = new PatientMessageModels();
        patientMessageModels.setPageNo(1);
        patientMessageModels.setLimit(10);
        patientMessageModels.setSearchValue("");
        if(NetworkUtils.isNetworkAvailable(getActivity())){
            showProgressBar("Fetching patient messages");
            apiRepository.getPatientMessages(patientMessageModels);
        }

    }

    private void onBindPatientAdapter() {

        PatientMessageAdapter patientMessageAdapter;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        patientMessageAdapter = new PatientMessageAdapter(MessageFragment.this,patientMessageModelsList);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(patientMessageAdapter);


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

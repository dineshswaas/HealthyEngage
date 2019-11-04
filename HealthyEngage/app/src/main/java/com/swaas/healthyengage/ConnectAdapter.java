package com.swaas.healthyengage;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import models.ConnectAPIModel;

class ConnectAdapter extends RecyclerView.Adapter<ConnectAdapter.ViewHolder> {

    List<ConnectAPIModel> connectAPIModelList;
    ConnectFragment context;

    public ConnectAdapter (ConnectFragment context , List<ConnectAPIModel> connectAPIModel){
        this.context = context;
        this.connectAPIModelList = connectAPIModel;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connect_list_items,null);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return connectAPIModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        public ViewHolder(View itemView) {
            super(itemView);
        }


    }
}

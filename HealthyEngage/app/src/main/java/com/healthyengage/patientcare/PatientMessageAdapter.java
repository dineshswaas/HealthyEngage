package com.healthyengage.patientcare;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import models.PatientMessageModels;

class PatientMessageAdapter extends RecyclerView.Adapter<PatientMessageAdapter.ViewHolder> {

    List<PatientMessageModels> patientMessageModels;
    MessageFragment mContext;


    public PatientMessageAdapter(MessageFragment mContext,List<PatientMessageModels> patientMessageModels){
        this.mContext = mContext;
        this.patientMessageModels =patientMessageModels;
    }


    @NonNull@Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patientmessage_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PatientMessageModels messageModels = patientMessageModels.get(position);
        holder.message.setText(messageModels.getDescription());
        holder.date.setText(DateHelper.getDisplayFormat(messageModels.getCreated_at().split("T")[0],"yyyy-MM-dd"));


    }



    @Override
    public int getItemCount() {
        return patientMessageModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView message,date;

        public ViewHolder(View itemView) {
            super(itemView);

            message = (TextView)itemView.findViewById(R.id.messageText);
            date = (TextView)itemView.findViewById(R.id.date);
        }
    }
}

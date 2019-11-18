package com.healthyengage.patientcare;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import models.ConnectAPIModel;

class ConnectAdapter extends RecyclerView.Adapter<ConnectAdapter.ViewHolder> {

    List<ConnectAPIModel> connectAPIModelList;
    ConnectFragment context;
    OnCareClick onCareClick;

    public ConnectAdapter (ConnectFragment context , List<ConnectAPIModel> connectAPIModel){
        this.context = context;
        this.connectAPIModelList = connectAPIModel;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connect_list_items,null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ConnectAPIModel connectAPIModel = connectAPIModelList.get(position);
        if(!TextUtils.isEmpty(connectAPIModel.getHeaderText())){
            holder.divider.setVisibility(View.GONE);
            holder.headerLayout.setVisibility(View.VISIBLE);
            holder.headerText.setText(connectAPIModel.getHeaderText());
            holder.assessmentMainLayout.setVisibility(View.GONE);

        }else{
                holder.divider.setVisibility(View.VISIBLE);
                holder.headerLayout.setVisibility(View.GONE);
                holder.assessmentMainLayout.setVisibility(View.VISIBLE);
                if(TextUtils.isEmpty(connectAPIModel.getLast_name())){
                    String trimLetter = connectAPIModel.getFirst_name().trim().charAt(0)+"";
                    holder.firstletter.setText(trimLetter);
                }else{
                    String trimLetter = connectAPIModel.getFirst_name().trim().charAt(0) +""+ connectAPIModel.getLast_name().trim().charAt(0);
                    holder.firstletter.setText(trimLetter);
                }

                holder.assesmentname.setText(connectAPIModel.getFirst_name() +"  " +connectAPIModel.getLast_name());
                holder.assesmentnameSubName.setText(connectAPIModel.getMobile_no());
                if(!TextUtils.isEmpty(connectAPIModel.getShift_start_time()) && !TextUtils.isEmpty(connectAPIModel.getShift_end_time())){
                    holder.assesmentnameSubName.setText(connectAPIModel.getShift_start_time() +" - "+connectAPIModel.getShift_end_time());
                }
                if(!TextUtils.isEmpty(connectAPIModel.getRelationName())){
                    holder.assesmentnameSubName.setText(connectAPIModel.getRelationName());
                }

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(connectAPIModel.getHeaderText())){
                    if(onCareClick != null){
                        onCareClick.onCareClick(position);
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return connectAPIModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView headerText,assesmentname,assesmentnameSubName,firstletter;
        RelativeLayout assessmentMainLayout;
        LinearLayout headerLayout;
        View divider;
        public ViewHolder(View itemView) {
            super(itemView);
            assesmentname = (TextView)itemView.findViewById(R.id.assesmentname);
            assesmentnameSubName = (TextView)itemView.findViewById(R.id.assesmentnameSubName);
            firstletter = (TextView)itemView.findViewById(R.id.firstletter);
            headerText = (TextView)itemView.findViewById(R.id.headerText);
            assessmentMainLayout = (RelativeLayout)itemView.findViewById(R.id.assessmentMainLayout);
            headerLayout = (LinearLayout)itemView.findViewById(R.id.headerLayout);
            divider= (View)itemView.findViewById(R.id.divider);
        }
    }




    public interface OnCareClick{
        public void onCareClick(final int position);
    }


    public void setOnCareClickClickListener(OnCareClick onCareClick) {
        this.onCareClick = onCareClick;
    }
}

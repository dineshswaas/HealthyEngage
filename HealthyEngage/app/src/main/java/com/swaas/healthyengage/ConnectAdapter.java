package com.swaas.healthyengage;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        ConnectAPIModel connectAPIModel = connectAPIModelList.get(position);
        if(!TextUtils.isEmpty(connectAPIModel.getHeaderText())){
            holder.headerText.setVisibility(View.VISIBLE);
            holder.headerText.setText(connectAPIModel.getHeaderText());
            if(!connectAPIModel.getFirst_name().equalsIgnoreCase("Emergency Care")){
                String trimLetter = connectAPIModel.getFirst_name().trim().charAt(0) +""+connectAPIModel.getLast_name().trim().charAt(0);
                holder.firstletter.setText(trimLetter);
                holder.assesmentname.setText(connectAPIModel.getFirst_name() +" " +connectAPIModel.getLast_name());
                holder.assesmentnameSubName.setText(connectAPIModel.getMobile_no());
            }else{
                holder.firstletter.setText("EC");
                holder.assesmentname.setText(connectAPIModel.getFirst_name());
                holder.assesmentnameSubName.setText(connectAPIModel.getShift_start_time() +" - "+connectAPIModel.getShift_end_time());
            }

        }else{
            holder.headerText.setVisibility(View.GONE);
            if(!connectAPIModel.getFirst_name().equalsIgnoreCase("Emergency Care")){
                String trimLetter = connectAPIModel.getFirst_name().trim().charAt(0) +""+ connectAPIModel.getLast_name().trim().charAt(0);
                holder.firstletter.setText(trimLetter);
                holder.assesmentname.setText(connectAPIModel.getFirst_name() +"  " +connectAPIModel.getLast_name());
                holder.assesmentnameSubName.setText(connectAPIModel.getMobile_no());
            }else{
                holder.firstletter.setText("EC");
                holder.assesmentname.setText(connectAPIModel.getFirst_name());
                holder.assesmentnameSubName.setText(connectAPIModel.getShift_start_time() +" - "+connectAPIModel.getShift_end_time());
            }
        }


    }

    @Override
    public int getItemCount() {
        return connectAPIModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView headerText,assesmentname,assesmentnameSubName,firstletter;

        public ViewHolder(View itemView) {
            super(itemView);
            assesmentname = (TextView)itemView.findViewById(R.id.assesmentname);
            assesmentnameSubName = (TextView)itemView.findViewById(R.id.assesmentnameSubName);
            firstletter = (TextView)itemView.findViewById(R.id.firstletter);
            headerText = (TextView)itemView.findViewById(R.id.headerText);
        }


    }
}

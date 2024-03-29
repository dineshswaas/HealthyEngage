package com.healthyengage.patientcare;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;

import java.util.List;

import models.CarePlanModels;

class CarePlanAdapter extends RecyclerView.Adapter<CarePlanAdapter.ViewHolder>{


    CarePlanFragment activity;
    List<CarePlanModels> carePlanModelsList;
    OnTpDateClickListener onTpDateClickListener;
    public CarePlanAdapter(CarePlanFragment activity, List<CarePlanModels> carePlanModelsList) {

    this.activity = activity;
    this.carePlanModelsList = carePlanModelsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.care_plan_date_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final CarePlanModels tpHeaderModel = carePlanModelsList.get(position);
        holder.day.setText(tpHeaderModel.getDayString().toUpperCase().substring(0, 1));
        holder.date.setText(String.valueOf(tpHeaderModel.getDayInt()));
        holder.circularProgressBar.setProgress(tpHeaderModel.getProgressValue());
        if(tpHeaderModel.getProgressValue() >= 100){
            holder.circularProgressBar.setVisibility(View.GONE);
            holder.completeImage.setVisibility(View.VISIBLE);
        }else{
            holder.circularProgressBar.setVisibility(View.VISIBLE);
            holder.completeImage.setVisibility(View.GONE);
        }
        if(!tpHeaderModel.isSelected()){
            holder.day.setTextColor(activity.getResources().getColor(R.color.grey_black));
            holder.day.setBackground(activity.getResources().getDrawable(R.drawable.rectangleborderwhite));
        }else{
            tpHeaderModel.setSelected(false);
            holder.day.setTextColor(activity.getResources().getColor(R.color.white));
            holder.day.setBackground(activity.getResources().getDrawable(R.drawable.brownborderred));
        }
       /* if(!tpHeaderModel.isIndicator()){
            holder.day.setTextColor(activity.getResources().getColor(R.color.black));
            holder.date.setTextColor(activity.getResources().getColor(R.color.black));
            holder.date.setBackground(activity.getResources().getDrawable(R.drawable.tp_default_date_bg));
        }else{
            holder.day.setTextColor(activity.getResources().getColor(R.color.black));
            holder.date.setTextColor(activity.getResources().getColor(R.color.white));
            holder.date.setBackground(activity.getResources().getDrawable(R.drawable.circleborderbrown));
            tpHeaderModel.setIndicator(false);
        }*/
        holder.dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTpDateClickListener != null) {
                    onTpDateClickListener.onDateClick(position);
                }
            }
        });



        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTpDateClickListener != null) {
                    onTpDateClickListener.onDateClick(position);
                }
            }
        });

        holder.day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTpDateClickListener != null) {
                    onTpDateClickListener.onDateClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return carePlanModelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView date, day;
        public final LinearLayout dateLayout;
        public final CircularProgressBar circularProgressBar;
        ImageView completeImage;
        public ImageView indicator;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            dateLayout = (LinearLayout) view.findViewById(R.id.date_layout);
            date = (TextView) view.findViewById(R.id.date);
            day = (TextView) view.findViewById(R.id.day);
            circularProgressBar = (CircularProgressBar)view.findViewById(R.id.donut_progress);
            completeImage = (ImageView)view.findViewById(R.id.completeImage);
        }
    }


    public interface OnTpDateClickListener{
        public void onDateClick(final int position);
    }


    public OnTpDateClickListener getOnTpDateClickListener() {
        return onTpDateClickListener;
    }

    public void setOnTpDateClickListener(OnTpDateClickListener onTpDateClickListener) {
        this.onTpDateClickListener = onTpDateClickListener;
    }

}

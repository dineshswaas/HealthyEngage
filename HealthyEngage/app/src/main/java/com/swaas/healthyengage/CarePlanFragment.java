package com.swaas.healthyengage;

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
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.CarePlanModels;

/**
 * Created by Adib on 13-Apr-17.
 */

public class CarePlanFragment extends Fragment implements  CarePlanAdapter.OnTpDateClickListener {

    private OnFragmentInteractionListener listener;
    private RecyclerView carePlanCalenderRecyclerView;
    View mView;
    List<CarePlanModels> tpHeaderModelList;
    CarePlanAdapter carePlanAdapter;
    TextView percentIndicatior,dateText;
    DonutProgress donut_progress;
    ImageView imageView2,imageView3;
    boolean imageonebool,imagetwobool,isdateAlreadySelect;
    int selectedItemposition = -1;
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


        return mView;
    }


    private void intializeViews() {
        tpHeaderModelList = new ArrayList<>();
        carePlanCalenderRecyclerView = (RecyclerView)mView.findViewById(R.id.careplancalender);
        percentIndicatior = (TextView)mView.findViewById(R.id.percentIndicatior);
        dateText = (TextView)mView.findViewById(R.id.date);
        donut_progress = (DonutProgress)mView.findViewById(R.id.donut_progress);
        imageView2 = (ImageView)mView.findViewById(R.id.imageView2);
        imageView3 = (ImageView)mView.findViewById(R.id.imageView3);


        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!imageonebool){
                    if(imagetwobool){
                        donut_progress.setProgress(100);
                        percentIndicatior.setText("Your care overview is 100% complete");
                        completedStory(true);
                    }else{
                        donut_progress.setProgress(50);
                        percentIndicatior.setText("Your care overview is 50% complete");
                    }
                    imageonebool = true;
                    imageView2.setBackground(getResources().getDrawable(R.drawable.circleborderblue));

                }else{
                    if(imagetwobool){
                        donut_progress.setProgress(50);
                        percentIndicatior.setText("Your care overview is 50% complete");
                    }else{
                        donut_progress.setProgress(0);
                        percentIndicatior.setText("Your care overview is 0% complete");
                    }
                    imageonebool = false;
                    imageView2.setBackground(getResources().getDrawable(R.drawable.circleborderwhite));
                    completedStory(false);
                }

            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!imagetwobool){
                    if(imageonebool){
                        donut_progress.setProgress(100);
                        percentIndicatior.setText("Your care overview is 100% complete");
                        completedStory(true);
                    }else{
                        donut_progress.setProgress(50);
                        percentIndicatior.setText("Your care overview is 50% complete");
                    }
                    imagetwobool = true;
                    imageView3.setBackground(getResources().getDrawable(R.drawable.circleborderblue));

                }else{
                    if(imageonebool){
                        donut_progress.setProgress(50);
                        percentIndicatior.setText("Your care overview is 50% complete");
                    }else{
                        donut_progress.setProgress(0);
                        percentIndicatior.setText("Your care overview is 0% complete");
                    }
                    imagetwobool = false;
                    imageView3.setBackground(getResources().getDrawable(R.drawable.circleborderwhite));
                    completedStory(false);
                }
            }
        });

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

          /*  Date date = convertStringToDate(tpHeader.getDates(), "yyyy-MM-dd");
            String formattedDate = formatDate(date, "yyyy-MMM-dd-EEE");
            String formatDay = formatDate(date, "yyyy-MM-dd-EEEE");
            String[] formattedString = formattedDate.split("-");
            tpHeader.setYearString(formattedString[0]);
            tpHeader.setMonthString(formattedString[1]);
            tpHeader.setDay(Integer.parseInt(formattedString[2]));
            tpHeader.setDayString(formattedString[3]);
            tpHeader.setTP_Full_Day(formatDay.split("-")[3]);*/

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
        donut_progress.setProgress(0);
        imageView3.setBackground(getResources().getDrawable(R.drawable.circleborderwhite));
        imageView2.setBackground(getResources().getDrawable(R.drawable.circleborderwhite));

    }

    public interface OnFragmentInteractionListener {
    }


    void completedStory(boolean status){

        if(isdateAlreadySelect && selectedItemposition > 0){
            tpHeaderModelList.get(selectedItemposition).setIndicator(status);
            carePlanAdapter.notifyItemChanged(selectedItemposition);
        }

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
                tpHeaderModel.setDay(Integer.parseInt(formattedString[2]));
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

}

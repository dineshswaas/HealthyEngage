package com.healthyengage.patientcare;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.healthyengage.library.BottomBarHolderActivity;
import com.healthyengage.library.NavigationPage;

public class HomePageActivity extends BottomBarHolderActivity implements
        CarePlanFragment.OnFragmentInteractionListener, InsightFragment.OnFragmentInteractionListener {

    int backButtonCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationPage page1 = new NavigationPage("Care Plan", ContextCompat.getDrawable(this, R.drawable.icon_care_plan_unselected), CarePlanFragment.newInstance());
        NavigationPage page2 = new NavigationPage("Insights", ContextCompat.getDrawable(this, R.drawable.icon_insight_unselected), InsightFragment.newInstance());
        NavigationPage page3 = new NavigationPage("Messages", ContextCompat.getDrawable(this, R.drawable.messageunselect), MessageFragment.newInstance());
        NavigationPage page4 = new NavigationPage("Connect", ContextCompat.getDrawable(this, R.drawable.icon_connect_unselected), ConnectFragment.newInstance());

        List<NavigationPage> navigationPages = new ArrayList<>();
        navigationPages.add(page1);
        navigationPages.add(page2);
        navigationPages.add(page3);
        navigationPages.add(page4);

        super.setupBottomBarHolderActivity(navigationPages);
    }

    @Override
    public void onClicked() {
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            backButtonCount = 0;
            moveTaskToBack(true);
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

/*    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        GlobalApplication.sThis.touch();
    }

    public GlobalApplication getApp() {
        return (GlobalApplication) getApplication();
    }*/
}
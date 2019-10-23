package com.swaas.healthyengage;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.swaas.library.BottomBarHolderActivity;
import com.swaas.library.NavigationPage;

public class HomePageActivity extends BottomBarHolderActivity implements CarePlanFragment.OnFragmentInteractionListener, InsightFragment.OnFragmentInteractionListener {

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
}
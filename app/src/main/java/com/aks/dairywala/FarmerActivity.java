package com.aks.dairywala;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
public class FarmerActivity extends FragmentActivity
{
    TabLayout tl;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);
        tl = findViewById(R.id.tl1);
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (pos) {
                    case 0:
                    {
                        ft.add(R.id.farmer_fg, new AddFarmerFragment(),"AddFarmerFragment");
                        break;
                    }
                    case 1:
                    {
                        ft.add(R.id.farmer_fg, new ViewFarmerFragment(), "ViewFarmerFragment");
                        break;
                    }
                }
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}

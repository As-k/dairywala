package com.aks.dairywala;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class BillingActivity extends FragmentActivity
{
    String id,name;
    TabLayout tl;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        //  Bundle b = getIntent().getExtras();

        tl = findViewById(R.id.farmer_billing_tab);
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                int pos = tab.getPosition();
                switch (pos)
                {
                    case 0:
                    {
                        ft.add(R.id.farmer_billing_fragment,new MilkFragment(),"MilkFragment");
                        ft.commit();
                        break;
                    }
                    case 1:
                    {
                        ft.add(R.id.farmer_billing_fragment,new PaymentFragment(),"MilkFragment");
                        ft.commit();
                        break;
                    }
                }
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


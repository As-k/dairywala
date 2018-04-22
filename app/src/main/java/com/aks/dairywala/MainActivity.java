package com.aks.dairywala;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

public class MainActivity extends Activity
{
    ProgressBar pb;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    boolean res;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = findViewById(R.id.pb);
        sp = getSharedPreferences("registered_status",MODE_PRIVATE);
        spe = sp.edit();

        res = sp.getBoolean("status",false);


        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    for (int i=10;i<=100;i+=10)
                    {
                        Thread.sleep(300);
                        pb.setProgress(i);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            finish();
                            if (res)
                            {
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            }
                            else
                            {
                                startActivity(new Intent(MainActivity.this, UserRegisterActivity.class));
                            }
                        }
                    });
                }catch (Exception e)
                {
                    Log.e("Exception",""+e);
                }
            }
        }).start();

        //SystemClock.sleep(1500);

        //startActivity(new Intent(MainActivity.this,UserRegisterActivity.class));
    }
}
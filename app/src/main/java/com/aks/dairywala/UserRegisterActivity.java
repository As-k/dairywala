package com.aks.dairywala;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aks.db.MilkDatabase;

import java.util.Calendar;

public class UserRegisterActivity extends Activity
{
    EditText user_name,user_cno,user_email,user_center;
    TextInputLayout til_user_name,til_user_cno,til_user_email,til_user_center;
    Button b;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);


        user_name = findViewById(R.id.user_name);
        user_cno = findViewById(R.id.user_cno);
        user_email =findViewById(R.id.user_email);
        user_center =findViewById(R.id.user_center);
        b = findViewById(R.id.user_registration);

        til_user_name = findViewById(R.id.til_user_name);
        til_user_cno = findViewById(R.id.til_user_cno);
        til_user_email = findViewById(R.id.til_user_email);
        til_user_center = findViewById(R.id.til_user_center);

        showUserRegistration();
    }

    public void showUserRegistration()
    {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String username = user_name.getText().toString().trim();
                String usercno = user_cno.getText().toString().trim();
                String useremail = user_email.getText().toString().trim();
                String usercenter = user_center.getText().toString().trim();

                if (username.isEmpty())
                {
                    til_user_name.setError("Empty");
                    user_name.requestFocus();
                }
                else
                {
                    til_user_name.setErrorEnabled(false);
                    if (usercno.isEmpty())
                    {
                        til_user_cno.setError("Empty");
                        user_cno.requestFocus();
                    }
                    else
                    {
                        til_user_cno.setErrorEnabled(false);
                        if (useremail.isEmpty())
                        {
                            til_user_email.setError("Empty");
                            user_email.requestFocus();
                        }
                        else
                        {
                            til_user_email.setErrorEnabled(false);
                            if (usercenter.isEmpty())
                            {
                                til_user_center.setError("Empty");
                                user_center.requestFocus();
                            }

                            int i;
                            for (i=0;i<username.length();i++)
                            {
                                if (!(((username.charAt(i)>=65)&&(username.charAt(i)<=90)) || ((username.charAt(i)>=97)&&(username.charAt(i)<=122)) ||(username.charAt(i)==' ')))
                                {
                                    break;
                                }
                            }
                            if (username.length() == i)
                            {
                                til_user_name.setErrorEnabled(false);
                                if ((usercno.charAt(0) == '9')||(usercno.charAt(0) == '8')||(usercno.charAt(0) == '7'))
                                {
                                    til_user_cno.setErrorEnabled(false);
                                    if (usercno.length() == 10)
                                    {
                                        til_user_cno.setErrorEnabled(false);

                                        int at_pos = useremail.indexOf('@');
                                        int dt_pos = useremail.lastIndexOf('.');

//                                        if (dt_pos>at_pos)
//                                        {
//                                            til_user_email.setErrorEnabled(false);
//                                            if ((dt_pos-at_pos)>2)
                                        if (useremail.matches("[a-zA-Z0-9._-]+@[a-z]{2,}+.[a-z]{2,4}+"))
                                        {
                                                til_user_email.setErrorEnabled(false);
                                                long cno = Long.parseLong(usercno);
                                                String gen_otp =  OTP(usercno);

                                                MilkDatabase mdb = new MilkDatabase(UserRegisterActivity.this);
                                                SQLiteDatabase db = mdb.getWritableDatabase();

                                                ContentValues cv = new ContentValues();
                                                cv.put(MilkDatabase.TABLE1_COL1,username);
                                                cv.put(MilkDatabase.TABLE1_COL2,cno);
                                                cv.put(MilkDatabase.TABLE1_COL3,useremail);
                                                cv.put(MilkDatabase.TABLE1_COL4,gen_otp);
                                                cv.put(MilkDatabase.TABLE1_COL5,usercenter);
                                                cv.put(MilkDatabase.TABLE1_COL6,0);

                                                db.insert(MilkDatabase.TABLE_NAME1,null,cv);

                                                Toast.makeText(UserRegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(UserRegisterActivity.this, gen_otp, Toast.LENGTH_SHORT).show();

                                                finish();

                                                Intent intent = new Intent(UserRegisterActivity.this,OTPValidationActivity.class);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                til_user_email.setError("Invalid email id");
                                                user_email.requestFocus();
                                            }
                                    }
                                    else
                                    {
                                        til_user_cno.setError("must 10 digit");
                                        user_cno.requestFocus();
                                    }
                                }
                                else
                                {
                                    til_user_cno.setError("Invalid contact number");
                                    user_cno.requestFocus();
                                }
                            }
                            else
                            {
                                til_user_name.setError("Invalid");
                                user_name.requestFocus();
                            }
                        }
                    }
                }
            }
        });
    }

    public String OTP(String cno)
    {
        int first = cno.charAt(0);
        int last = cno.charAt(5);

        Calendar c = Calendar.getInstance();
        int c_day = c.get(Calendar.DAY_OF_MONTH);
        int c_month = c.get(Calendar.MONTH);

        int c_hr = c.get(Calendar.HOUR);
        int c_sec = c.get(Calendar.SECOND);

        String otp = ""+(last*4*c_day+c_month*c_hr+first*c_sec)+"";

        return otp;
    }
}

package com.aks.dairywala;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.aks.db.MilkDatabase;

public class OTPValidationActivity extends Activity
{
    EditText et;
    TextInputLayout til_user_otp;
    SQLiteDatabase db;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpvalidation);

        et = findViewById(R.id.user_otp);
        til_user_otp = findViewById(R.id.til_user_otp);

        sp = getSharedPreferences("registered_status",MODE_PRIVATE);
        spe = sp.edit();


        MilkDatabase md = new MilkDatabase(this);
        db = md.getWritableDatabase();

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String otp = s.toString().trim();
                if (otp.length() == 4)
                {
                    til_user_otp.setErrorEnabled(false);

                    String sel = MilkDatabase.TABLE1_COL4+" = ?";
                    String sel_args[] = {otp};
                    Cursor c = db.query(MilkDatabase.TABLE_NAME1,null,sel,sel_args,null,null,null);
                    boolean res = c.moveToFirst();
                    if (res)
                    {
                        til_user_otp.setErrorEnabled(false);

                        spe.putBoolean("status",true);
                        spe.commit();
                        finish();
                        Intent i = new Intent(OTPValidationActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        til_user_otp.setError("Invalid");
                    }
                }
                else
                {
                    til_user_otp.setError("Incomplete");
                }
            }
        });
    }
}

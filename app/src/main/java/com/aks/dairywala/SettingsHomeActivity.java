package com.aks.dairywala;


import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aks.db.MilkDatabase;

public class SettingsHomeActivity extends Activity {
    Button b1,b2,b3,b4;
    EditText et1,et2,et3;
    TextInputLayout til1,til2,til3;
    SQLiteDatabase db;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_home);
        b1 = findViewById(R.id.addFatRate);
        b2 = findViewById(R.id.b1);
        b3 = findViewById(R.id.addMilkCenter);
        b4 = findViewById(R.id.b2);
        tv = findViewById(R.id.old_fat);
        et1 = findViewById(R.id.fatRate);
        et2 = findViewById(R.id.centerName);
        et3 = findViewById(R.id.contact_no);
        til1 = findViewById(R.id.til_fatrate);
        til2 = findViewById(R.id.til_ucn);
        til3 = findViewById(R.id.til_cno);

        MilkDatabase md = new MilkDatabase(this);
        db = md.getWritableDatabase();
    }

    public void addFat(View v) {
        b1.setVisibility(View.GONE);
        b3.setVisibility(View.GONE);

        String col[] = {MilkDatabase.TABLE1_COL6};
        Cursor c = db.query(MilkDatabase.TABLE_NAME1,col,null,null,null,null,null);
        if (c.moveToFirst()) {
            tv.setVisibility(View.VISIBLE);
            et1.setVisibility(View.VISIBLE);
            til1.setVisibility(View.VISIBLE);
            b2.setVisibility(View.VISIBLE);

            double d = c.getDouble(0);
            tv.setText("Old Fat Rate : "+d);
        } else {
            Toast.makeText(this, "Sorry", Toast.LENGTH_SHORT).show();
        }
    }

    public void addCenter(View v) {
        b1.setVisibility(View.GONE);
        b3.setVisibility(View.GONE);
        String col[] = {MilkDatabase.TABLE1_COL2,MilkDatabase.TABLE1_COL5};
        Cursor c = db.query(MilkDatabase.TABLE_NAME1,col,null,null,null,null,null);
        if (c.moveToFirst()) {
            et2.setVisibility(View.VISIBLE);
            et3.setVisibility(View.VISIBLE);
            til2.setVisibility(View.VISIBLE);
            til3.setVisibility(View.VISIBLE);
            b4.setVisibility(View.VISIBLE);

            long cno = c.getLong(0);
            String center_name = c.getString(1);

            et3.setText(String.valueOf(cno));
            et2.setText(center_name);
        } else {
            Toast.makeText(this, "Sorry", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveFat(View v) {
        String fat = et1.getText().toString().trim();
        if (fat.isEmpty()) {
            til1.setError("Empty");
        } else {
            til1.setErrorEnabled(false);
            float fat_per = Float.parseFloat(fat);
            ContentValues cv = new ContentValues();
            cv.put(MilkDatabase.TABLE1_COL6,fat_per);
            int res =  db.update(MilkDatabase.TABLE_NAME1,cv,null,null);
            if (res > 0) {
                Toast.makeText(this, "Fat Rate Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void saveCenter(View v) {
        String center_name = et2.getText().toString().trim();
        String cno = et3.getText().toString().trim();
        if (center_name.isEmpty()) {
            til2.setError("Empty");
            et2.requestFocus();
        } else {
            til2.setErrorEnabled(false);
            if (cno.isEmpty()) {
                til3.setError("Empty");
                et3.requestFocus();
            } else {
                til3.setErrorEnabled(false);
                if ((cno.charAt(0) == '9')||(cno.charAt(0) == '8')||(cno.charAt(0) == '7')) {
                    til3.setErrorEnabled(false);
                    if (cno.length() == 10) {
                        til3.setErrorEnabled(false);
                        ContentValues cv = new ContentValues();
                        cv.put(MilkDatabase.TABLE1_COL2,cno);
                        cv.put(MilkDatabase.TABLE1_COL5,center_name);

                        int res = db.update(MilkDatabase.TABLE_NAME1,cv,null,null);
                        if (res > 0) {
                            Toast.makeText(this, "Milk Center and Contact No is Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        til3.setError("Must 10 Digits Contact No");
                        et3.requestFocus();
                    }
                } else {
                    til3.setError("Invalid Contact No");
                    et3.requestFocus();
                }
            }
        }
    }
}


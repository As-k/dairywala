package com.aks.dairywala;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aks.db.MilkDatabase;

public class HomeActivity extends Activity
{
    public static String id,name;
    public static long cno;
    public static Bitmap bit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void buttonFarmer(View v)
    {
        startActivity(new Intent(this,FarmerActivity.class));
    }

    public void buttonFarmerBilling(View v)
    {
        final EditText et = new EditText(this);
        et.setHint("Id No.");
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(Color.WHITE);

        AlertDialog.Builder adb = new AlertDialog.Builder(this,android.R.style.Theme_Holo_Dialog);
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setTitle("Billing For Farmer");
        adb.setView(et);
        adb.setCancelable(false);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                id = et.getText().toString().trim();
                if (id.isEmpty()) {
                    et.setError("Empty");
                } else {
                    MilkDatabase md = new MilkDatabase(HomeActivity.this);
                    SQLiteDatabase db = md.getWritableDatabase();

                    String cols[] = {MilkDatabase.TABLE2_COL2,MilkDatabase.TABLE2_COL4};
                    String sel = MilkDatabase.TABLE2_COL1+" = ?";
                    String sel_args[] = {id};
                    Cursor c = db.query(MilkDatabase.TABLE_NAME2,cols,sel,sel_args,null,null,null,null);
                    if (c.moveToFirst()) {
                        name = c.getString(0);
                        byte b[] = c.getBlob(1);
                        bit = BitmapFactory.decodeByteArray(b,0,b.length);
                        Intent i = new Intent(HomeActivity.this,BillingActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(HomeActivity.this, "Wrong Idno", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.create().show();
    }

    public void buttonFarmerSettings(View v) {
        startActivity(new Intent(this,SettingsHomeActivity.class));
    }

    public void buttonFarmerReports(View v)
    {
        final EditText et = new EditText(this);
        et.setHint("Id No.");
        et.setTextColor(Color.WHITE);
        et.setHintTextColor(Color.WHITE);
        AlertDialog.Builder adb = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Dialog);
        adb.setIcon(R.mipmap.ic_launcher);
        adb.setTitle("Report Of Farmer");
        adb.setView(et);
        adb.setCancelable(false);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                id = et.getText().toString().trim();
                if (id.isEmpty()) {
                    et.setError("Empty");
                } else {
                    MilkDatabase md = new MilkDatabase(HomeActivity.this);
                    SQLiteDatabase db = md.getWritableDatabase();

                    String cols[] = {MilkDatabase.TABLE2_COL2, MilkDatabase.TABLE2_COL3, MilkDatabase.TABLE2_COL4};
                    String sel = MilkDatabase.TABLE2_COL1+" = ?";
                    String sel_args[] = {id};

                    Cursor c = db.query(MilkDatabase.TABLE_NAME2,cols,sel,sel_args,null,null,null,null);
                    if (c.moveToFirst()) {
                        name = c.getString(0);
                        cno = c.getLong(1);
                        byte b[] = c.getBlob(2);

                        bit = BitmapFactory.decodeByteArray(b,0, b.length);
                        Intent i = new Intent(HomeActivity.this,ReportActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(HomeActivity.this, "Wrong Idno", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.create().show();
    }
}
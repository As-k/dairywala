package com.aks.dairywala;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aks.db.MilkDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ReportActivity extends Activity {
    EditText et1, et2;
    TextInputLayout til1, til2;
    String keys[] = {"billno","date","price","fat","ltr","day","total"};
    int ids[] = {R.id.report_bill_no, R.id.report_bill_date, R.id.report_ltr_price, R.id.report_fat_con, R.id.report_ltr_con, R.id.report_farmer_day, R.id.report_total};
    ListView lv;
    int c_yr, c_month, c_day;
    ArrayList al;
    TextView tv9, tv10, tv11, tv12, tv13, tv14;
    ImageView iv;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        et1 = findViewById(R.id.fromdate);
        et2 = findViewById(R.id.todate);
        til1 = findViewById(R.id.til_from);
        til2 = findViewById(R.id.til_to);
        lv = findViewById(R.id.show_list);
        al = new ArrayList();
        db = new MilkDatabase(this).getWritableDatabase();
        Calendar c = Calendar.getInstance();
        c_yr = c.get(Calendar.YEAR);
        c_month = c.get(Calendar.MONTH);
        c_day = c.get(Calendar.DAY_OF_MONTH);
        fromDate();
        toDate();

        View v1 = getLayoutInflater().inflate(R.layout.header_report_design, null, false);
        tv9 = v1.findViewById(R.id.header_id_no);
        tv10 = v1.findViewById(R.id.header_name);
        tv11 = v1.findViewById(R.id.header_cno);
        iv = v1.findViewById(R.id.header_farmer_image);
        tv9.setText(HomeActivity.id);
        tv10.setText(HomeActivity.name);
        tv11.setText(""+HomeActivity.cno);
        iv.setImageBitmap(HomeActivity.bit);
        lv.addHeaderView(v1);

        View v2 = getLayoutInflater().inflate(R.layout.footer_report_design,null,false);
        tv12 = v2.findViewById(R.id.footer_total_amt);
        tv13 = v2.findViewById(R.id.footer_adv_amt);
        tv14 = v2.findViewById(R.id.footer_bal_amt);
        String col[] = {MilkDatabase.TABLE4_COL2,MilkDatabase.TABLE4_COL3,MilkDatabase.TABLE4_COL4};
        String sel = MilkDatabase.TABLE4_COL1+" = ? ";
        String sel_arg[] = {HomeActivity.id};
        Cursor foot = db.query(MilkDatabase.TABLE_NAME4,col, sel, sel_arg, null, null, null);
        if (foot.moveToFirst()) {
            double total = foot.getDouble(0);
            double adv = foot.getDouble(1);
            double bal = foot.getDouble(2);
            tv12.setText(total+"/-");
            tv13.setText(adv+"/-");
            tv14.setText(bal+"/-");
        } else {
            Toast.makeText(this, "Data not available", Toast.LENGTH_SHORT).show();
        }
        lv.addFooterView(v2);
    }

    public void fromDate() {
        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(ReportActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        et1.setText(dayOfMonth+"-"+(month+1)+"-"+year);

                    }
                },c_yr,c_month,c_day);
                DatePicker dp = dpd.getDatePicker();
                dp.setMinDate(System.currentTimeMillis()-10*24*60*60*1000);
                dp.setMaxDate(System.currentTimeMillis());
                dpd.show();
            }
        });
    }

    public void toDate() {
        et2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(ReportActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        et2.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },c_yr,c_month,c_day);
                DatePicker dp = dpd.getDatePicker();
                dp.setMinDate(System.currentTimeMillis()-10*24*60*60*1000);
                dp.setMaxDate(System.currentTimeMillis());
                dpd.show();
            }
        });
    }

    public void showList(View v) {
        String from = et1.getText().toString().trim();
        String to = et2.getText().toString().trim();
        if (from.isEmpty()) {
            til1.setError("Empty");
            et1.requestFocus();
        }
        else {
            til1.setErrorEnabled(false);
            if (to.isEmpty()) {
                til2.setError("Empty");
                et2.requestFocus();
            }  else {
                til2.setErrorEnabled(false);
                String qry = "select * from "+MilkDatabase.TABLE_NAME3+" where "+MilkDatabase.TABLE3_COL3+" = "+HomeActivity.id+" and "+MilkDatabase.TABLE3_COL1+" between '"+from+"' and '"+to+"'";
                Cursor c = db.rawQuery(qry,null);
                if (c.moveToFirst()) {
                    do{
                        String date = c.getString(0);
                        int billno = c.getInt(1);
                        double fat = c.getDouble(3);
                        double lrt = c.getDouble(4);
                        String day = c.getString(5);
                        double total = c.getDouble(6);
//                        double adv = c.getDouble(7);

//                        String qry1 = "select * from "+MilkDatabase.TABLE_NAME5+" where "+MilkDatabase.TABLE5_COL1+" = "+HomeActivity.id+" and "+MilkDatabase.TABLE5_COL2+" between '"+from+"' and '"+to+"'";
//                        Cursor c1 = db.rawQuery(qry1,null);
//                        if (c1.moveToFirst()) {
//                            do {
//                                double adv = c1.getDouble(2);
//
//                            } while (c1.moveToNext());
//                        }
                        HashMap hm = new HashMap();
                        hm.put(keys[0], billno);
                        hm.put(keys[1], date);
                        hm.put(keys[2], ((total/lrt)/fat)*10+"/-");
                        hm.put(keys[3], fat+"%");
                        hm.put(keys[4], lrt);
                        hm.put(keys[5], day);
//                        hm.put(keys[6], adv + "/-");
                        hm.put(keys[6], total+"/-");
                        al.add(hm);
                    }while (c.moveToNext());
                }
                SimpleAdapter simpleAdapter = new SimpleAdapter(this,al,R.layout.farmer_report_design,keys,ids);
                lv.setAdapter(simpleAdapter);
            }
        }
    }
}
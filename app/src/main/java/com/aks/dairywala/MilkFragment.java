package com.aks.dairywala;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aks.db.MilkDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class MilkFragment extends Fragment
{
    ImageView iv;
    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    MilkDatabase md;
    SQLiteDatabase db;
    TextInputLayout til1, til2;
    EditText et1,et2;
    Button b;
    String date;
    int bill_no;
    float fat_con,ltr_con;
    double total, advance = 0.0;
    String day;

    public MilkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_milk, container, false);

        tv1 = v.findViewById(R.id.milk_farmer_bill_no);
        tv2 = v.findViewById(R.id.milk_farmer_bill_date);
        tv3 = v.findViewById(R.id.milk_farmer_id);
        tv4 = v.findViewById(R.id.milk_farmer_name);
        et1 = v.findViewById(R.id.milk_farmer_fat);
        et2 = v.findViewById(R.id.milk_farmer_ltrs);
        til1 = v.findViewById(R.id.til_fat);
        til2 = v.findViewById(R.id.til_ltrs);
        b = v.findViewById(R.id.milk_farmer_save);
        tv5 = v.findViewById(R.id.milk_farmer_total);
        tv6 = v.findViewById(R.id.milk_farmer_day);
        iv = v.findViewById(R.id.farmer_bill_image);

        bill_no = getBillNo();
        tv1.setText(String.valueOf(bill_no));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        date = sdf.format(new Date());
        tv2.setText(date);
        tv3.setText(HomeActivity.id);
        tv4.setText(HomeActivity.name);
        iv.setImageBitmap(HomeActivity.bit);

        final int day1 = Calendar.getInstance().get(Calendar.AM_PM);
        if (day1 == Calendar.AM) {
            this.day = "AM";
            tv6.setText("AM");
        } else {
            this.day  = "PM";
            tv6.setText("PM");
        }

        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String fat = et1.getText().toString().trim();
                String ltr = et2.getText().toString().trim();
                if (fat.isEmpty()) {
                    til1.setError("Empty");
                    et1.requestFocus();
                } else {
                    til1.setErrorEnabled(false);
                    if (ltr.isEmpty()) {
                        til2.setError("Empty");
                        et2.requestFocus();
                    } else {
                        til2.setErrorEnabled(false);
                        fat_con = Float.parseFloat(fat);
                        ltr_con = Float.parseFloat(ltr);
                        double given_fat_rate = (getFatRate()/10);
                        total = (fat_con * given_fat_rate)*ltr_con;
                        tv5.setText(""+total);
                    }
                }
            }
        });
        saveBilling();
        return v;
    }

    public void saveBilling() {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fat = et1.getText().toString().trim();
                String ltr = et2.getText().toString().trim();
                if (fat.isEmpty()) {
                    til1.setError("Empty");
                    et1.requestFocus();
                } else {
                    til1.setErrorEnabled(false);
                    if (ltr.isEmpty()) {
                        til1.setError("Empty");
                        et2.requestFocus();
                    } else {
                        til2.setErrorEnabled(false);
                        double total_advance;
                        String qry = "Select sum("+MilkDatabase.TABLE5_COL3+") from "+MilkDatabase.TABLE_NAME5+" where "+MilkDatabase.TABLE5_COL1+" = "+HomeActivity.id;
                        Cursor c1 = db.rawQuery(qry,null);
                        if (c1.moveToFirst()) {
                            total_advance = c1.getDouble(0);
                        } else {
                            total_advance = 0.0;
                        }
                        ContentValues cv = new ContentValues();
                        cv.put(MilkDatabase.TABLE3_COL1,date);
                        cv.put(MilkDatabase.TABLE3_COL2,bill_no);
                        cv.put(MilkDatabase.TABLE3_COL3,HomeActivity.id);
                        cv.put(MilkDatabase.TABLE3_COL4,fat_con);
                        cv.put(MilkDatabase.TABLE3_COL5,ltr_con);
                        cv.put(MilkDatabase.TABLE3_COL6,day);
                        cv.put(MilkDatabase.TABLE3_COL7,total);
                        cv.put(MilkDatabase.TABLE3_COL8,total_advance);
                        long res = db.insert(MilkDatabase.TABLE_NAME3,null,cv);

                        String qry0 = "Select sum("+MilkDatabase.TABLE3_COL7+") from "+MilkDatabase.TABLE_NAME3+" where "+MilkDatabase.TABLE3_COL3+" = "+HomeActivity.id;
                        Cursor c = db.rawQuery(qry0,null);
                        double d1;
                        if (c.moveToFirst()) {
                            d1 = c.getDouble(0);
                        } else {
                            d1 = 0.0;
                        }

                        double balance = d1 - total_advance;
                        ContentValues cv1 = new ContentValues();
                        cv1.put(MilkDatabase.TABLE4_COL1,HomeActivity.id);
                        cv1.put(MilkDatabase.TABLE4_COL2,d1);
                        cv1.put(MilkDatabase.TABLE4_COL3,total_advance);
                        cv1.put(MilkDatabase.TABLE4_COL4,balance);
                        long no1 = db.insert(MilkDatabase.TABLE_NAME4, null, cv1);
                        if (no1 != -1 && res != -1) {
                            Toast.makeText(getActivity(), "Billing Done", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }

                        String qry2 = "select "+MilkDatabase.TABLE4_COL1+" from "+MilkDatabase.TABLE_NAME4+" where "+MilkDatabase.TABLE4_COL1+" = "+HomeActivity.id;
                        Cursor c2 = db.rawQuery(qry2,null);
                        if (c2.moveToFirst()) {
                            String where = MilkDatabase.TABLE4_COL1+" = ?";
                            String where_args[] = {HomeActivity.id};
                            int rows = db.update(MilkDatabase.TABLE_NAME4,cv1,where,where_args);
                            if (rows>0) {
                                getActivity().finish();
                                String cols[] = {MilkDatabase.TABLE1_COL2,MilkDatabase.TABLE1_COL5,MilkDatabase.TABLE1_COL6};
                                Cursor c3 = db.query(MilkDatabase.TABLE_NAME1,cols,null,null,null,null,null);
                                if (c3.moveToFirst()) {
                                    Intent i = new Intent(getContext(), FinalBillActivity.class);
                                    i.putExtra("k1", c3.getLong(0));
                                    i.putExtra("k2", c3.getString(1));
                                    i.putExtra("k3", (c3.getDouble(2)/10));
                                    i.putExtra("k4",bill_no);
                                    i.putExtra("k5",date);
                                    i.putExtra("k6",HomeActivity.id);
                                    i.putExtra("k7",HomeActivity.name);
                                    i.putExtra("k8",day);
                                    i.putExtra("k9",fat_con);
                                    i.putExtra("k10",ltr_con);
                                    i.putExtra("k11",total);
                                    i.putExtra("k12",d1);
                                    i.putExtra("k13",total_advance);
                                    i.putExtra("k14",balance);

                                    startActivity(i);
                                } else {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }
                        }
                    }
                }
            }
        });
    }

    public double getFatRate() {
        double rate = 0.0;
        String cols[] = {MilkDatabase.TABLE1_COL6};
        Cursor c = db.query(MilkDatabase.TABLE_NAME1,cols,null,null,null,null,null);
        if (c.moveToFirst()) {
            rate = c.getDouble(0);
        }
        return rate;
    }

    private int getBillNo() {
        int bill_no = 0;
        md = new MilkDatabase(getActivity());
        db = md.getWritableDatabase();
        String qry = "select max("+MilkDatabase.TABLE3_COL2+") from "+MilkDatabase.TABLE_NAME3;
        Cursor c = db.rawQuery(qry,null);
        if (c.moveToFirst()) {
            bill_no = c.getInt(0);
            if (bill_no == 0) {
                bill_no = 1001;
            } else {
                bill_no++;
            }
        } else {
            getActivity().finish();
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
        return bill_no;
    }
}

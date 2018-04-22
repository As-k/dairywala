package com.aks.dairywala;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FinalBillActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_bill);
        Bundle b = getIntent().getExtras();

        long Contactno = b.getLong("k1");
        String center_name = b.getString("k2");
        double fat_rate = b.getDouble("k3");
        int bill_no = b.getInt("k4");
        String date = b.getString("k5");
        String farmer_id = b.getString("k6");
        String farmer_name = b.getString("k7");
        String day = b.getString("k8");
        float fat_con = b.getFloat("k9");
        float ltr_con = b.getFloat("k10");
        double total = b.getDouble("k11");
        double final_total = b.getDouble("k12");
        double advance = b.getDouble("k13");
        double balance = b.getDouble("k14");

        TextView tv1 = findViewById(R.id.final_center_name);
        tv1.setText(center_name);
        TextView tv2 = findViewById(R.id.final_cno);
        tv2.setText(Contactno+"");
        TextView tv3 = findViewById(R.id.final_bill_no);
        tv3.setText(bill_no+"");
        TextView tv4 = findViewById(R.id.final_bill_date);
        tv4.setText(date);
        TextView tv5 = findViewById(R.id.final_farmer_id);
        tv5.setText(farmer_id);
        TextView tv6 = findViewById(R.id.final_farmer_name);
        tv6.setText(farmer_name);
        TextView tv7 = findViewById(R.id.final_farmer_day);
//        if (day.equals(""))
//            tv7.setText("AM");
//        else
//            tv7.setText("PM");
        tv7.setText(day);
        TextView tv8 = findViewById(R.id.final_fat_con);
        tv8.setText(fat_con+" %");
        TextView tv9 = findViewById(R.id.final_ltr_con);
        tv9.setText(ltr_con+"");
        TextView tv10 = findViewById(R.id.final_ltr_price);
        tv10.setText(fat_rate+"/-");
        TextView tv11 = findViewById(R.id.final_total);
        tv11.setText(total+"/-");
        TextView tv12 = findViewById(R.id.final_total_pay);
        tv12.setText("Rs. "+final_total+"/-");
        TextView tv13 = findViewById(R.id.final_total_advance);
        tv13.setText("Rs. "+advance+"/-");
        TextView tv14 = findViewById(R.id.final_total_balance);
        tv14.setText("Rs. "+balance+"/-");
    }
}

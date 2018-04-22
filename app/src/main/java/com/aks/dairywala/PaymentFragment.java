package com.aks.dairywala;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aks.db.MilkDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {
    double adv;

    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment, container, false);
        TextView tv = v.findViewById(R.id.advance_date);
        final TextInputLayout til = v.findViewById(R.id.til_amt);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        final String date = sdf.format(new Date());
        tv.setText(date);
        final EditText et = v.findViewById(R.id.advance_amount);
        Button b = v.findViewById(R.id.advance_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String s1 = et.getText().toString().trim();
                if (s1.isEmpty()) {
                    til.setError("Empty");
                } else {
                    til.setErrorEnabled(false);
                    double advance = Double.parseDouble(s1);
                    MilkDatabase md = new MilkDatabase(getContext());
                    SQLiteDatabase db = md.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put(MilkDatabase.TABLE5_COL1,HomeActivity.id);
                    cv.put(MilkDatabase.TABLE5_COL2,date);
                    cv.put(MilkDatabase.TABLE5_COL3,advance);
                    db.insert(MilkDatabase.TABLE_NAME5,null,cv);
                    String qry = "select sum("+MilkDatabase.TABLE5_COL3+") from "+MilkDatabase.TABLE_NAME5+" where "+MilkDatabase.TABLE5_COL1+"="+HomeActivity.id;
                    Cursor c1 = db.rawQuery(qry,null);
                    if(c1.moveToFirst()) {
                        adv = c1.getDouble(0);
                        adv = adv+advance;
                    }

                    String where = MilkDatabase.TABLE4_COL1+" =?";
                    String where_args[] = {HomeActivity.id};
                    ContentValues cv1=new ContentValues();
                    cv1.put(MilkDatabase.TABLE4_COL3,advance);
                    int no=db.update(MilkDatabase.TABLE_NAME4,cv1,where,where_args);
                    if(no != 0) {
                        Log.e("Adv",""+no);
                    }

//                    ContentValues cv2 = new ContentValues();
//                    cv2.put(MilkDatabase.TABLE3_COL8,advance);
//                    String where2 = MilkDatabase.TABLE3_COL3+" = ? and "+MilkDatabase.TABLE3_COL1+" = ?";
//                    String where_arg2[] = {HomeActivity.id,date};
//                    int i = db.update(MilkDatabase.TABLE_NAME3,cv2,where2,where_arg2);
//                    if (i>0) {
//                        Toast.makeText(getActivity(), "adv money added", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(getActivity(), "adv money not added", Toast.LENGTH_SHORT).show();
//                    }
                    getActivity().finish();
                }
            }
        });
        return v;
    }
}

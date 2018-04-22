package com.aks.dairywala;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aks.db.MilkDatabase;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFarmerFragment extends Fragment
{
    ImageView iv;
    TextView tv;
    EditText et1,et2,et3;
    TextInputLayout til1,til2,til3;
    Button b;
    Bitmap bit = null;
    int farmer_id;
    public static final int CAM_REQ_CODE = 121;

    public AddFarmerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v1 =  inflater.inflate(R.layout.fragment_add_farmer, container, false);

        iv = v1.findViewById(R.id.farmer_image);
        tv = v1.findViewById(R.id.farmer_image_message);
        et1 = v1.findViewById(R.id.farmer_id);
        et2 = v1.findViewById(R.id.farmer_name);
        et3 = v1.findViewById(R.id.farmer_contact_no);
        til1 = v1.findViewById(R.id.til_farmer_id);
        til2 = v1.findViewById(R.id.til_farmer_name);
        til3 = v1.findViewById(R.id.til_farmer_cno);
        b = v1.findViewById(R.id.framer_save);

        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent();
                i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,CAM_REQ_CODE);
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent();
                i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,CAM_REQ_CODE);
            }
        });

        farmer_id = farmerId();
        et1.setText(""+farmer_id);
        et1.setEnabled(false);
        til1.setEnabled(false);

        saveBtn();

        return v1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQ_CODE)
        {
            if (resultCode == getActivity().RESULT_OK)
            {
                bit = (Bitmap) data.getExtras().get("data");
                iv.setImageBitmap(bit);
            }
        }
    }

    public int farmerId() {
        int idno = 1;
        MilkDatabase md = new MilkDatabase(getContext());
        SQLiteDatabase sd = md.getWritableDatabase();
        String qry = "select max("+MilkDatabase.TABLE2_COL1+") from "+MilkDatabase.TABLE_NAME2;
        Cursor c = sd.rawQuery(qry,null);
        if (c.moveToFirst()) {
            idno = c.getInt(0);
            idno++;
//            if (idno == 0)
//            {
//                idno = 101;
//            }
//            else
//            {
//                idno++;
//            }
        } else {
            getActivity().finish();
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
        return idno;
    }

    public void saveBtn()
    {
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bit == null) {
                    Toast.makeText(getContext(), "Please click Image", Toast.LENGTH_SHORT).show();
                } else {
                    String id = et1.getText().toString().trim();
                    String name = et2.getText().toString().trim();
                    String cno = et3.getText().toString().trim();

                    if (id.isEmpty()) {
                        til1.setError("Empty");
                        et1.requestFocus();
                    } else {
                        til1.setErrorEnabled(false);
                        if (name.isEmpty()) {
                            til2.setError("Empty");
                            et2.requestFocus();
                        } else {
                            til2.setErrorEnabled(false);
                            if (cno.isEmpty()) {
                                til3.setError("Empty");
                                et3.requestFocus();
                            } else {
                                til3.setErrorEnabled(false);
                                if ((cno.charAt(0) == '9') || (cno.charAt(0) == '8') || (cno.charAt(0) == '7')) {
                                    til3.setErrorEnabled(false);
                                    if (cno.length() == 10) {
                                        til3.setErrorEnabled(false);
                                        long contact = Long.parseLong(cno);

                                        ByteArrayOutputStream bout = new ByteArrayOutputStream();
                                        bit.compress(Bitmap.CompressFormat.JPEG,100,bout);
                                        byte image[] = bout.toByteArray();

                                        ContentValues cv = new ContentValues();
                                        cv.put(MilkDatabase.TABLE2_COL1,farmer_id);
                                        cv.put(MilkDatabase.TABLE2_COL2, name);
                                        cv.put(MilkDatabase.TABLE2_COL3, contact);
                                        cv.put(MilkDatabase.TABLE2_COL4,image);

                                        MilkDatabase md = new MilkDatabase(getContext());
                                        SQLiteDatabase db = md.getWritableDatabase();
                                        long result = db.insert(MilkDatabase.TABLE_NAME2, null,cv);
                                        if (result != -1) {
                                            Toast.makeText(getActivity(), "Farmer Added to Database", Toast.LENGTH_SHORT).show();
                                            getActivity().finish();
                                        }
                                    } else {
                                        til3.setError("Must 10 Digits Contact No.");
                                        et3.requestFocus();
                                    }
                                } else {
                                    til3.setError("Invalid Contact Number");
                                    et3.requestFocus();
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}

package com.aks.dairywala;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aks.db.MilkDatabase;

import java.io.ByteArrayOutputStream;

public class UpdateFarmerActivity extends Activity
{
    int id;
    String name;
    long cno;
    byte image[];
    ImageView iv;
    EditText et1, et2;
    TextInputLayout til1, til2;
    Button b1;
    Bitmap bit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_farmer);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getInt("id");
            name = b.getString("name");
            cno = b.getLong("cno");
            image = b.getByteArray("image");
        }
        else{
            Toast.makeText(this, "No Update", Toast.LENGTH_SHORT).show();
        }

        iv = findViewById(R.id.farmer_update_iv);
        et1 = findViewById(R.id.farmer_update_name);
        et2 = findViewById(R.id.farmer_update_cno);
        til1 = findViewById(R.id.til_update_name);
        til2 = findViewById(R.id.til_update_cno);
        b1 = findViewById(R.id.update_farmer_save);

        bit = BitmapFactory.decodeByteArray(image, 0, image.length);

        et1.setText(name);
        et2.setText(""+cno);
        iv.setImageBitmap(bit);

    }

    public void updateImage(View v) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,121);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 121)
        {
            if (resultCode == RESULT_OK)
            {
                bit = (Bitmap) data.getExtras().get("data");
                iv.setImageBitmap(bit);
            }
        }
    }

    public void updateFarmer(View v)
    {
        if (bit == null)
        {
            Toast.makeText(this, "Please Click Image", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String uname = et1.getText().toString().trim();
            String ucno = et2.getText().toString().trim();
            if (uname.isEmpty())
            {
                til1.setError("Empty");
                et1.requestFocus();
            }
            else
            {
                til1.setEnabled(false);
                if (ucno.isEmpty())
                {
                    et2.setError("Empty");
                    et2.requestFocus();
                }
                else
                {
                    til2.setEnabled(false);
                    if (uname.matches("[A-Za-z ]+")) {
                        til1.setEnabled(false);
                        if (ucno.charAt(0) == '9' || ucno.charAt(0) == '8' || ucno.charAt(0) == '7') {
                            til2.setEnabled(false);
                            if (ucno.length() == 10) {
                                til2.setEnabled(false);
                                long contact = Long.parseLong(ucno);

                                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                                bit.compress(Bitmap.CompressFormat.JPEG,100,bout);

                                byte image1[] = bout.toByteArray();

                                ContentValues cv = new ContentValues();
//                                cv.put(MilkDatabase.TABLE2_COL1,id);
                                cv.put(MilkDatabase.TABLE2_COL2, uname);
                                cv.put(MilkDatabase.TABLE2_COL3, contact);
                                cv.put(MilkDatabase.TABLE2_COL4,image1);

                                MilkDatabase md = new MilkDatabase(this);
                                SQLiteDatabase db = md.getWritableDatabase();
                                String where = MilkDatabase.TABLE2_COL1+" = ? ";
//                                         "update "+MilkDatabase.TABLE_NAME2+" " +
//                                        "set "+MilkDatabase.TABLE2_COL2+" = "+uname+", " +
//                                        ""+MilkDatabase.TABLE2_COL3+" = "+ucno+", " +
//                                        ""+MilkDatabase.TABLE2_COL4+" = "+image1;
                                String whr_arg[] = {""+id};

                                int res = db.update(MilkDatabase.TABLE_NAME2,cv,where,whr_arg);
                                if (res != -1)
                                {
                                    Toast.makeText(this, "Farmer Added to Database", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                            else {
                                til2.setError("Must 10 digit Contact No");
                                et2.requestFocus();
                            }}
                        else {
                            til2.setError("Invalid Contact No");
                            et2.requestFocus();
                        }}
                    else {
                        til1.setError("Invalid Name");
                        et1.requestFocus();
                    }
                }
            }
        }
    }

}

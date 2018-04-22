package com.aks.dairywala;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aks.db.MilkDatabase;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFarmerFragment extends Fragment
{
    ListView lv;
    String keys[] = {"idno","name","cno","image"};
    ArrayList al;
    TextView tv1,tv2,tv3;
    ImageView iv;
    Button b1,b2;
    SQLiteDatabase db;

    public ViewFarmerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_farmer, container, false);

        al = new ArrayList();
        lv = v.findViewById(R.id.farmer_view_list);

        MilkDatabase md = new MilkDatabase(getContext());
        db = md.getWritableDatabase();
        Cursor c = db.query(MilkDatabase.TABLE_NAME2,null,null,null,null,null,null);
        if (c.moveToFirst())
        {
            do
            {
                int id = c.getInt(0);
                String name = c.getString(1);
                long cno = c.getLong(2);
                byte image[] = c.getBlob(3);

                HashMap hm = new HashMap();
                hm.put(keys[0], id);
                hm.put(keys[1], name);
                hm.put(keys[2], cno);
                hm.put(keys[3], image);
                
                al.add(hm);
            }while (c.moveToNext());

            MyFarmerAdapter mfa = new MyFarmerAdapter();
            lv.setAdapter(mfa);
        }
        else
        {
            Toast.makeText(getActivity(), "No Farmer Details Found", Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    public class MyFarmerAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = getLayoutInflater().inflate(R.layout.farmer_view_design,parent,false);

            tv1 = v.findViewById(R.id.farmer_view_id);
            tv2 = v.findViewById(R.id.farmer_view_name);
            tv3 = v.findViewById(R.id.farmer_view_cno);
            iv = v.findViewById(R.id.farmer_view_image);
            b1 = v.findViewById(R.id.farmer_update);
            b2 = v.findViewById(R.id.farmer_delete);

            HashMap hm = (HashMap)al.get(position);

            final int id = (int)hm.get(keys[0]);
            final String name = (String)hm.get(keys[1]);
            final long cno = (Long)hm.get(keys[2]);
            final byte image[] = (byte[])hm.get(keys[3]);

            Bitmap bit = BitmapFactory.decodeByteArray(image,0,image.length);

            tv1.setText(""+id);
            tv2.setText(name);
            tv3.setText(""+cno);
            iv.setImageBitmap(bit);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Next", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), UpdateFarmerActivity.class);
                    i.putExtra("id", id);
                    i.putExtra("name", name);
                    i.putExtra("cno", cno);
                    i.putExtra("image",image);
                    startActivity(i);
                }
            });

            b2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final AlertDialog.Builder adb = new AlertDialog.Builder(getContext(),android.R.style.Theme_Dialog);
                    adb.setIcon(android.R.drawable.ic_delete);
                    adb.setTitle("Farmer : "+name);
                    adb.setMessage("Farmer Deleting Conformation ?");
                    adb.setCancelable(false);
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                            String where = MilkDatabase.TABLE2_COL1+" = ? ";
                            String where_args[] = {""+id};
                            int res = db.delete(MilkDatabase.TABLE_NAME2,where,where_args);
                            if (res != 0)
                            {
                                Toast.makeText(getContext(), "Farmer Deleted", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }
                        }
                    });
                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                    adb.create().show();

                }
            });

            return v;
        }
    }

}

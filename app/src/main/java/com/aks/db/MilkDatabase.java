package com.aks.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ashish on 1/17/2018.
 */

public class MilkDatabase extends SQLiteOpenHelper
{
    public static final String DBNAME = "milk";
    public static final int VERSION = 1;

    public static final String TABLE_NAME1 = "user_register";

    public static final String TABLE1_COL1 = "NAME";
    public static final String TABLE1_COL2 = "CONTACTNO";
    public static final String TABLE1_COL3 = "EMAILID";
    public static final String TABLE1_COL4 = "OTP";
    public static final String TABLE1_COL5 = "CENTER_NAME";
    public static final String TABLE1_COL6 = "FAT_RATE";

    public static final String TABLE_NAME2 = "farmer_details";

    public static final String TABLE2_COL1 = "IDNO";
    public static final String TABLE2_COL2 = "NAME";
    public static final String TABLE2_COL3 = "CONTACTNO";
    public static final String TABLE2_COL4 = "FARMER_IMAGE";


    public static final String TABLE_NAME3 = "PAYMENTS";

    public static final String TABLE3_COL1 = "BILL_DATE";
    public static final String TABLE3_COL2 = "BILL_NO";
    public static final String TABLE3_COL3 = "FARMER_ID";
    public static final String TABLE3_COL4 = "FAT";
    public static final String TABLE3_COL5 = "LEATERS";
    public static final String TABLE3_COL6 = "DAY";
    public static final String TABLE3_COL7 = "TOTAL";
    public static final String TABLE3_COL8 = "ADVANCE";



    public static final String TABLE_NAME4 = "Bill_Payments";

    public static final String TABLE4_COL1 = "FARMER_ID";
    public static final String TABLE4_COL2 = "TOTAL";
    public static final String TABLE4_COL3 = "ADVANCE";
    public static final String TABLE4_COL4 = "BALANCE";
//    public static final String TABLE4_COL5 = "ADVANCE_DATE";


    public static final String TABLE_NAME5 = "Advance_Payments";

    public static final String TABLE5_COL1 = "FARMER_ID";
    public static final String TABLE5_COL2 = "ADVANCE_DATE";
    public static final String TABLE5_COL3 = "ADVANCE";



    public MilkDatabase(Context context)
    {
        super(context,DBNAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String qry1 = "create table "+TABLE_NAME1+"("
                +TABLE1_COL1+" TEXT,"
                +TABLE1_COL2+" NUMBER,"
                +TABLE1_COL3+" TEXT,"
                +TABLE1_COL4+" TEXT,"
                +TABLE1_COL5+" TEXT,"
                +TABLE1_COL6+" REAL)";


        db.execSQL(qry1);

        String qry2 = "create table "+TABLE_NAME2+"("
                +TABLE2_COL1+" NUMBER,"
                +TABLE2_COL2+" TEXT,"
                +TABLE2_COL3+" NUMBER,"
                +TABLE2_COL4+" BLOB)";

        db.execSQL(qry2);

        String qry3 = "create table "+TABLE_NAME3+"("
                +TABLE3_COL1+" TEXT,"
                +TABLE3_COL2+" NUMBER,"
                +TABLE3_COL3+" NUMBER,"
                +TABLE3_COL4+" REAL,"
                +TABLE3_COL5+" REAL,"
                +TABLE3_COL6+" TEXT,"
                +TABLE3_COL7+" REAL,"
                +TABLE3_COL8+" REAL)";

        db.execSQL(qry3);

        String qry4 = "create table "+TABLE_NAME4+"("
                +TABLE4_COL1+" NUMBER,"
                +TABLE4_COL2+" REAL,"
                +TABLE4_COL3+" REAL,"
                +TABLE4_COL4+" REAL)";

        db.execSQL(qry4);

        String qry5 = "create table "+TABLE_NAME5+"("
                +TABLE5_COL1+" NUMBER,"
                +TABLE5_COL2+" TEXT,"
                +TABLE5_COL3+" REAL)";

        db.execSQL(qry5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

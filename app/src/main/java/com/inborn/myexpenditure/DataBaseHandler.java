package com.inborn.myexpenditure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String DBName ="myexpense.db" ;
    Context ctxt;


    public DataBaseHandler(@Nullable Context context) {
        super(context, Environment.getExternalStorageDirectory() + File.separator + "MyExpenditure" + File.separator + DBName, null, 1);
        this.ctxt=ctxt;
    }

    public DataBaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String myexpense="CREATE TABLE MYEXPENSE (SNO INTEGER PRIMARY KEY,DATE TEXT,DESCRIPTION TEXT,RATE TEXT,AMOUNT TEXT,CRDR TEXT)";
        sqLiteDatabase.execSQL(myexpense);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public int addMyExpense(String date,String desc,String rate,String amt,String crdr)
    {
        int flag=0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("SNO", getSNo());
            values.put("DATE", date);
            values.put("DESCRIPTION", desc);
            values.put("RATE", rate);
            values.put("AMOUNT", amt);
            values.put("CRDR", crdr);
            db.insert("MYEXPENSE", null, values);
            Log.d("inserted", "done");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            flag++;
        }
        return flag;
    }

    public String getSNo() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, "MYEXPENSE");
        Log.d("insert", String.valueOf(count));
        String expense = null;
        if (count == 0) {
            expense = "1";
        } else {
            String selectQuery = "SELECT  SNO FROM MYEXPENSE";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                Integer value = Integer.valueOf(cursor.getString(0)) + 1;
                expense = String.format("%05d", value);
            }


        }

        return expense;

    }
    public String[] SelectAllDesc() {
        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase();

            String strSQL = "SELECT DISTINCT DESCRIPTION FROM MYEXPENSE";
            Cursor cursor = db.rawQuery(strSQL, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];
                    int i = 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            return arrData;
        } catch (Exception e) {
            return null;
        }
    }

}

package com.inborn.myexpenditure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import androidx.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
            SQLiteDatabase db= this.getReadableDatabase();

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
    public String[] SelectAllDescReport() {
        try {
            String arrData[] = null;
            SQLiteDatabase db= this.getReadableDatabase();

            String strSQL = "SELECT DISTINCT DESCRIPTION FROM MYEXPENSE";
            Cursor cursor = db.rawQuery(strSQL, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()+1];
                    arrData[0]="ALL";
                    int i = 1;
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
    public String reportHtmlData(String frmdate,String todate,String spinnerdata)
    {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = (Date)formatter.parse(frmdate);
            endDate = (Date)formatter.parse(todate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String temp="";
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        while (cal.getTime().before(endDate)) {
            Date date = cal.getTime();

                temp=temp+" DATE='"+formatter.format(date)+"' OR";

            cal.add(Calendar.DATE, 1);
        }
        temp=temp+" DATE='"+formatter.format(endDate)+"' ";

        SQLiteDatabase db= this.getReadableDatabase();
        String description="";
        if(!spinnerdata.equals("ALL"))
        {
            description="DESCRIPTION='"+spinnerdata+"' AND";
        }
        String strSQL = "SELECT * FROM MYEXPENSE where  ("+description+temp+")";
        Cursor cursor = db.rawQuery(strSQL, null);
        double cramount = 0;
        double dramount=0;
        String data="<html><body><center>";
        String credit="<h3>INCOME</h3><br<br><table border=1><tr>" +
                "<th>S.no</th>" +
                "<th>Date</th>" +
                "<th>Description</th>" +
                "<th >Amount</th></tr>";
        String debit="<br><h3>EXPENSE</h3><br<br><table border=1><tr>" +
                "<th>S.no</th>" +
                "<th>Date</th>" +
                "<th>Description</th>" +
                "<th>Amount</th></tr>";
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int cri=1,dri=1;
                do {
                    if(cursor.getString(5).equals("INCOME"))
                    {
                        credit=credit+"<tr><td>"+cri+"</td>";
                        credit=credit+"<td>"+cursor.getString(1)+"</td>";
                        credit=credit+"<td>"+cursor.getString(2)+"</td>";
                        credit=credit+"<td align=\"right\">"+Double.parseDouble(cursor.getString(4))+"</td>";
                        cramount=cramount+Double.valueOf(cursor.getString(4));
                        cri++;
                    }
                    else
                    {
                        debit=debit+"<tr><td>"+dri+"</td>";
                        debit=debit+"<td>"+cursor.getString(1)+"</td>";
                        debit=debit+"<td>"+cursor.getString(2)+"</td>";
                        debit=debit+"<td align=\"right\">"+Double.parseDouble(cursor.getString(4))+"</td>";
                        dramount=dramount+Double.valueOf(cursor.getString(4));
                        dri++;
                    }

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        credit=credit+"<tr><td></td><td></td><td>Total</td><td align=\"right\">"+cramount+"</td></tr></table>";
        debit=debit+"<tr><td></td><td></td><td>Total</td><td align=\"right\">"+dramount+"</td></tr></table>";

        data=data+credit+debit+"<br><br><h3>Balance="+(cramount-dramount);
        return data;
    }

}

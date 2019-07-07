package com.inborn.myexpenditure;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportFragment extends Fragment {
    View view;
    Context context;
    Button frmdate,todate,submitbtn;
    DatePickerDialog datePickerDialog;
    private int mYear,mMonth,mDay;
    AppCompatSpinner spinner;
    WebView reportwebView;
    Calendar calendar;
    public ReportFragment(Context context) {
        // Required empty public constructor
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_report, container, false);

        frmdate = view.findViewById(R.id.frmDate);
        todate = view.findViewById(R.id.toDate);
        spinner=view.findViewById(R.id.reportspinner);
        submitbtn=view.findViewById(R.id.submitbtn);
        reportwebView=view.findViewById(R.id.reportwebview);
        final DataBaseHandler dataBaseHandler=new DataBaseHandler(context);

        List<String> rlist =null;
        try {
            rlist = Arrays.asList(dataBaseHandler.SelectAllDescReport());
            ArrayAdapter spinneradapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, rlist);
            spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinneradapter);
        }catch (Exception e)
        {}
        frmdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                frmdate.setText(new DecimalFormat("00").format(dayOfMonth) + "-" + new DecimalFormat("00").format(monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                datePickerDialog.show();

            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                todate.setText(new DecimalFormat("00").format(dayOfMonth) + "-" + new DecimalFormat("00").format(monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String frm = frmdate.getText().toString();
                    String to = todate.getText().toString();

                    reportwebView.getSettings().setJavaScriptEnabled(true);
                    reportwebView.loadDataWithBaseURL("", dataBaseHandler.reportHtmlData(frm, to, spinner.getSelectedItem().toString()), "text/html", "UTF-8", "");
                }catch (Exception e)
                {}
            }
        });



        return  view;
    }
}

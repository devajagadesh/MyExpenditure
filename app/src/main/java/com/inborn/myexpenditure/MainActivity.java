package com.inborn.myexpenditure;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    EditText rateEditText,amountEditText;
    AutoCompleteTextView descAutoCompleteTextView;
    AppCompatSpinner crdrSpinner;
    Button saveButton;
    String curdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    final DataBaseHandler handler=new DataBaseHandler(MainActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isFileReadPermissiongranted()) {
            showPermissionRequestAlertmessage();
        }
        setContentView(R.layout.activity_main);
        rateEditText=findViewById(R.id.rate);
        amountEditText=findViewById(R.id.amount);
        descAutoCompleteTextView=findViewById(R.id.description);
        crdrSpinner=findViewById(R.id.crordr);
        saveButton=findViewById(R.id.save);

        final List<String> crdrlist = new ArrayList<String>();
        crdrlist.add("Credit");
        crdrlist.add("Debit");
        ArrayAdapter spinneradapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,crdrlist);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crdrSpinner.setAdapter(spinneradapter);
        crdrSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String data= String.valueOf(crdrSpinner.getItemAtPosition(position));
                if(data.equals("Credit"))
                {
                    rateEditText.setVisibility(View.GONE);
                }
                else{
                    rateEditText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String[] autotextdata=handler.SelectAllDesc();
        if(autotextdata!=null) {
            ArrayAdapter autotextviewadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, autotextdata);
            descAutoCompleteTextView.setAdapter(autotextviewadapter);
            descAutoCompleteTextView.setThreshold(1);
            descAutoCompleteTextView.dismissDropDown();
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (descAutoCompleteTextView.getText().toString().length()>2&&amountEditText.getText().toString().length()>1)
                    {
                        final DataBaseHandler handler=new DataBaseHandler(MainActivity.this);
                        int response=handler.addMyExpense(curdate,descAutoCompleteTextView.getText().toString(),rateEditText.getText().toString(),amountEditText.getText().toString(),crdrSpinner.getSelectedItem().toString());
                        if(response==0)
                        {
                            SweetAlertDialog dialog=new SweetAlertDialog(MainActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                            dialog.setTitle("Success");
                            dialog.show();
                            rateEditText.setText("");
                            amountEditText.setText("");
                            descAutoCompleteTextView.setText("");
                        }
                    }
            }
        });

    }

    private void showPermissionRequestAlertmessage() {
        SweetAlertDialog sweetAlertDialog;
        sweetAlertDialog = new SweetAlertDialog(MainActivity.this,
                SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitle("Requesting Permission");
        sweetAlertDialog.setContentText("Allow this app to read & write files in order to save " +
                "data"+
                " ");
        sweetAlertDialog.setConfirmButton("ok", new SweetAlertDialog.OnSweetClickListener()
        {
            @Override
            public void onClick(SweetAlertDialog sweetDialog)
            {
                sweetDialog.dismissWithAnimation();
                showPermissionRequestDialog();
            }
        });
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }
    private boolean isFileReadPermissiongranted()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }
    private void showPermissionRequestDialog() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                2);

    }
}

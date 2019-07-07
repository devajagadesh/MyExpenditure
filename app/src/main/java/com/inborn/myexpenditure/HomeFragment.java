package com.inborn.myexpenditure;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeFragment extends Fragment {
    Context context;
    EditText amountEditText;
    AutoCompleteTextView descAutoCompleteTextView;
    AppCompatSpinner crdrSpinner;
    Button saveButton;
    View view;
    String curdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    public HomeFragment(Context context) {
        // Required empty public constructor
        this.context=context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        amountEditText=view.findViewById(R.id.amount);
        descAutoCompleteTextView=view.findViewById(R.id.description);
        crdrSpinner=view.findViewById(R.id.crordr);
        saveButton=view.findViewById(R.id.save);
        final List<String> crdrlist = new ArrayList<String>();
        crdrlist.add("Credit");
        crdrlist.add("Debit");
        ArrayAdapter spinneradapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,crdrlist);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crdrSpinner.setAdapter(spinneradapter);
        crdrSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String data= String.valueOf(crdrSpinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        DataBaseHandler handler=new DataBaseHandler(context);
        String[] autotextdata=handler.SelectAllDesc();
        if(autotextdata!=null) {
            ArrayAdapter autotextviewadapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, autotextdata);
            descAutoCompleteTextView.setAdapter(autotextviewadapter);
            descAutoCompleteTextView.setThreshold(1);
            descAutoCompleteTextView.dismissDropDown();
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (descAutoCompleteTextView.getText().toString().length()>2&&amountEditText.getText().toString().length()>1)
                {
                    final DataBaseHandler handler=new DataBaseHandler(context);
                    int response=handler.addMyExpense(curdate,descAutoCompleteTextView.getText().toString(),"0",amountEditText.getText().toString(),crdrSpinner.getSelectedItem().toString());
                    if(response==0)
                    {
                        SweetAlertDialog dialog=new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setTitle("Success");
                        dialog.show();
                        amountEditText.setText("");
                        descAutoCompleteTextView.setText("");
                    }
                }
            }
        });



        return view;
    }
}

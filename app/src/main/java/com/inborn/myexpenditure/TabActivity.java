package com.inborn.myexpenditure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TabActivity extends AppCompatActivity {
    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isFileReadPermissiongranted()) {
            showPermissionRequestAlertmessage();
        }
        setContentView(R.layout.activity_tab);
        tabLayout=(TabLayout)findViewById(R.id.tablayout);
        frameLayout=(FrameLayout)findViewById(R.id.framelayout);

        fragment = new HomeFragment(TabActivity.this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new HomeFragment(TabActivity.this);
                        break;
                    case 1:
                        fragment = new ReportFragment(TabActivity.this);
                        break;

                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void showPermissionRequestAlertmessage() {
        SweetAlertDialog sweetAlertDialog;
        sweetAlertDialog = new SweetAlertDialog(TabActivity.this,
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

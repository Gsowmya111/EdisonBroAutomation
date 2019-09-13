package edisonbro.com.edisonbroautomation;

/**
 *  FILENAME: AboutUs.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Activity to display details about company.

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import edisonbro.com.edisonbroautomation.operatorsettings.OperatorSettingsMain;

public class AboutUs extends AppCompatActivity {
    Button btn_back,b_home;
    TextView tv_vernam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_us);
        tv_vernam= (TextView) findViewById(R.id.tv_vernam);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tv_vernam.setText(version);
            //int verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        btn_back= (Button) findViewById(R.id.btnback);
        b_home= (Button) findViewById(R.id.btnhome);
        b_home.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intnt=new Intent(AboutUs.this, Main_Navigation_Activity.class);
                startActivity(intnt);
                finish();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt=new Intent(AboutUs.this, Main_Navigation_Activity.class);
                startActivity(intnt);
                finish();
            }
        });


    }

    public void onBackPressed() {
        Intent intnt=new Intent(AboutUs.this, Main_Navigation_Activity.class);
        startActivity(intnt);
        finish();
    }
}

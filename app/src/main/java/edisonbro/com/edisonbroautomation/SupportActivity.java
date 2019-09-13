package edisonbro.com.edisonbroautomation;


/**
 *  FILENAME: SupportActivity.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Activity for customer care .

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class SupportActivity extends AppCompatActivity {
    Button btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_support);
        btn_back= (Button) findViewById(R.id.btnback);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt=new Intent(SupportActivity.this, Main_Navigation_Activity.class);
                startActivity(intnt);
                finish();
            }
        });
    }

    public void onBackPressed() {
        Intent intnt=new Intent(SupportActivity.this, Main_Navigation_Activity.class);
        startActivity(intnt);
        finish();
    }
}

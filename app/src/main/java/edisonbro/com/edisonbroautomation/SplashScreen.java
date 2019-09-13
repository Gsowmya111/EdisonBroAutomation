package edisonbro.com.edisonbroautomation;

/**
 *  FILENAME: SplashScreen.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Activity to display splash screen at start of the app .

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */


import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.databasewired.LocalDatabaseHelper;

public class SplashScreen extends AppCompatActivity {
    LocalDatabaseHelper db=null;
    private String TAG1 = "SplashScreen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_splash_screen);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        try{
            db=new LocalDatabaseHelper(SplashScreen.this);
            db.opendb();
        }catch(Exception e){
            e.printStackTrace();
        }

        int applockcount = db.getCount_Applock();

        if(applockcount>0){
            Cursor cursdb=db.fetch_Applockdetails(1);
            String isApplock="";

            if(cursdb!=null){
                isApplock=cursdb.getString(cursdb.getColumnIndexOrThrow(LocalDatabaseHelper.VALUE));
                StaticVariabes_div.log("isapplock"+isApplock, TAG1);

              /*  setContentView(R.layout.activity_splash_screen);
                LinearLayout layout = (LinearLayout) findViewById(R.id.splashLyt);
                AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setFillAfter(true);
                animation.setDuration(500);
                // apply the animation ( fade In ) to your LAyout
                layout.startAnimation(animation);

                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
*/
                if(isApplock.equals("true")){
                    Intent i=new Intent(SplashScreen.this,AppLockActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    splashscreen();
                }
            }
            db.close();
        }else{
            db.insert("AppEnable","false");
            db.insert("AppPassword","1234");
            db.insert("AppEmail","NoEmail");
            db.close();
            splashscreen();
        }

    }


    public void splashscreen(){

        setContentView(R.layout.activity_splash_screen);
       // LinearLayout layout = (LinearLayout) findViewById(R.id.splashLyt);
      //  AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
      //  animation.setFillAfter(true);
     //   animation.setDuration(500);
        // apply the animation ( fade In ) to your LAyout
      //  layout.startAnimation(animation);


        Thread th=new Thread(){

            public void run() {

                try {
                    Thread.sleep(200);


                        Intent i=new Intent(SplashScreen.this,Main_Navigation_Activity.class);
                        startActivity(i);
                        finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };th.start();

    }

    public void onBackPressed() {
        //	super.onBackPressed();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
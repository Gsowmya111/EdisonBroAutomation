package edisonbro.com.edisonbroautomation;

/**
 *  FILENAME: AppLockActivity.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Activity to open lock of app with password.

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edisonbro.com.edisonbroautomation.databasewired.LocalDatabaseHelper;

public class AppLockActivity extends AppCompatActivity implements View.OnClickListener {
    EditText pass ;
    Button btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_0;
    ImageButton btn_del,btn_clear;
    ImageView lock_image;
    TextView tv_forgotapplck;
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    LocalDatabaseHelper localDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_lock);
        tv_forgotapplck= (TextView) findViewById(R.id.tv_forgotapplck);




      //  lock_image=(ImageView) findViewById(R.id.lock_image);

        pass=(EditText) findViewById(R.id.et_pwd_applock);

        btn_0=(Button) findViewById(R.id.btn_0);
        btn_1=(Button) findViewById(R.id.btn_1);
        btn_2=(Button) findViewById(R.id.btn_2);
        btn_3=(Button) findViewById(R.id.btn_3);
        btn_4=(Button) findViewById(R.id.btn_4);
        btn_5=(Button) findViewById(R.id.btn_5);
        btn_6=(Button) findViewById(R.id.btn_6);
        btn_7=(Button) findViewById(R.id.btn_7);
        btn_8=(Button) findViewById(R.id.btn_8);
        btn_9=(Button) findViewById(R.id.btn_9);
        btn_del=(ImageButton) findViewById(R.id.btn_del);
        btn_clear=(ImageButton) findViewById(R.id.btn_clear);

        try{
            localDB=new LocalDatabaseHelper(this);
            localDB.opendb();

        }catch(Exception e){
            e.printStackTrace();
        }

        //setting click listener for buttons
        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_clear.setOnClickListener(this);

        //making text fields un clickable
        pass.setKeyListener(null);

        //setting text change listener
        pass.addTextChangedListener(textwatcher);

        tv_forgotapplck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    //text change listener
    TextWatcher textwatcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,int count) {


        }

        @Override
        public void afterTextChanged(Editable name) {

            //getting text from textfields
            String input1=pass.getText().toString();
            int length=input1.length();

   /*         switch(length){

                case 0:
                    lock_image.setBackgroundResource(R.drawable.lock0);
                    break;
                case 1:
                    lock_image.setBackgroundResource(R.drawable.lock1);
                    break;
                case 2:
                    lock_image.setBackgroundResource(R.drawable.lock2);
                    break;
                case 3:
                    lock_image.setBackgroundResource(R.drawable.lock3);
                    break;
                case 4:
                    lock_image.setBackgroundResource(R.drawable.lock4);
                    break;

            }*/

            if(input1.length()==4){

                String EnteredPassword=input1;
                String DbPassword="";
                Cursor cursdb=localDB.fetch_Applockdetails(2);
                if(cursdb!=null){
                    DbPassword=cursdb.getString(cursdb.getColumnIndexOrThrow(LocalDatabaseHelper.VALUE));
                }


                if(DbPassword!=null){
                    if(EnteredPassword.equals(DbPassword)){
                        //switching activity
                        Intent it=new Intent(AppLockActivity.this,Main_Navigation_Activity.class);
                        startActivity(it);
                        finish();

                    }else{
                        vibration_call();
                        clear();
                      //  lock_image.setBackgroundResource(R.drawable.wrong_lock);
                        pass.setError("Incorrect Password");
                        Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    vibration_call();
                    clear();
                    pass.setError("Incorrect Password");
                    //lock_image.setBackgroundResource(R.drawable.wrong_lock);
                    Toast.makeText(getApplicationContext(), "No Password Set", Toast.LENGTH_SHORT).show();
                }


            }
        }

    };

    //clearing text fields and setting focus in first text field
    void clear(){
        pass.setText("");
        pass.requestFocus();
    }

    void delay(final long time){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try{
                    Thread.sleep(time);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

    }


    //click listener for buttons
    @Override
    public void onClick(View v) {
        String Enteredpass=pass.getText().toString();
        if(Enteredpass.length()<0){
            Enteredpass="";
        }
        switch(v.getId()){
            case R.id.btn_0:
                pass.setText(Enteredpass+"0");
                break;

            case R.id.btn_1:
                pass.setText(Enteredpass+"1");
                break;

            case R.id.btn_2:
                pass.setText(Enteredpass+"2");
                break;

            case R.id.btn_3:
                pass.setText(Enteredpass+"3");
                break;

            case R.id.btn_4:
                pass.setText(Enteredpass+"4");
                break;

            case R.id.btn_5:
                pass.setText(Enteredpass+"5");
                break;

            case R.id.btn_6:
                pass.setText(Enteredpass+"6");
                break;

            case R.id.btn_7:
                pass.setText(Enteredpass+"7");
                break;

            case R.id.btn_8:
                pass.setText(Enteredpass+"8");
                break;

            case R.id.btn_9:
                pass.setText(Enteredpass+"9");
                break;

            case R.id.btn_clear:
                clear();
                break;

            case R.id.btn_del:

                int length=Enteredpass.length();
                String sub=null;

                switch(length)
                {
                    case 1:
                        pass.setText("");
                        break;
                    case 2:
                        sub=Enteredpass.substring(0,1);
                        pass.setText(sub);
                        break;
                    case 3:
                        sub=Enteredpass.substring(0,2);
                        pass.setText(sub);
                        break;
                    case 4:
                        sub=Enteredpass.substring(0,3);
                        pass.setText(sub);
                        break;

                }

                break;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(LocalDatabaseHelper.sdb.isOpen()){
            localDB.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!LocalDatabaseHelper.sdb.isOpen()){
            localDB.opendb();
        }
    }

    //vibrate the button
    private void vibration_call()
    {
        Vibrator vbr=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern={200,300,200,300};
        vbr.vibrate(pattern,-1);

    }

}


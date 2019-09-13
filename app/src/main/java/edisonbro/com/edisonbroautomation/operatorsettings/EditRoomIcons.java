package edisonbro.com.edisonbroautomation.operatorsettings;


/**
 *  FILENAME: EditRoomIcons.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to Edit Room Icons.
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.databasewired.LocalListArrangeTable;
import edisonbro.com.edisonbroautomation.databasewired.MasterAdapter;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;

public class EditRoomIcons extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button edit_btn;

    Spinner room_icons_spinner,room_spinner;
    HouseConfigurationAdapter houseDB=null;
    WirelessConfigurationAdapter WhouseDB=null;
    private MasterAdapter mas_adap;
    private SwbAdapter dev_adap;
    SpinnerAdapter iconsAdapter,roomNameAdapter;
    UpdateAdapter up_adap;
    ArrayList<String> UniqueRoomList=new ArrayList<String>();
    List<String> Device_Type_List,Short_DevNames_List,Full_DevNames_List;

    //lists for device types
    ArrayList<String>  finalDevName_List=new ArrayList<String>();
    ArrayList<Integer> finalDevNo_List =new ArrayList<Integer>();

    int CurrentRoomNo=0 ,DevType_Position=0;
    int spinnerLayoutId= R.layout.spinnerlayout;
    boolean roomaleadyexist=false;
    boolean isDbExists=false;
    ImageView navigateBack;
    String matched_room_image_name,Room_Icon_Image, roomName;
    private ServerDetailsAdapter sdadap;

    LocalListArrangeTable locallist_adap;
    private static final String TAG1 = "Edit Room Icons-";

   /* Integer[] images = { 0, R.drawable.im_entrance3, R.drawable.im_conference3,
            R.drawable.im_reception3, R.drawable.im_bedroom3, R.drawable.im_diningroom3,
            R.drawable.im_kitchen3,R.drawable.im_livingroom3,R.drawable.im_studyroom3,R.drawable.im_playroom3 ,R.drawable.im_dgm3,
            R.drawable.im_cgm3,R.drawable.im_gm3,R.drawable.im_ps3,R.drawable.im_civilroom3,R.drawable.im_corridor3,R.drawable.im_visitorroom3,
            R.drawable.im_serverroom3,R.drawable.im_liftlobby3,R.drawable.im_stairs3,R.drawable.im_toiletcorridor3,R.drawable.im_toiletdis3,
            R.drawable.im_toiletf3,R.drawable.im_toiletm3
    };*/
   Integer[] images = { 0, R.drawable.im_entrance3, R.drawable.im_conference3,
           R.drawable.im_reception3, R.drawable.im_bedroom3, R.drawable.im_diningroom3,
           R.drawable.im_kitchen3,R.drawable.im_livingroom3,R.drawable.im_studyroom3,R.drawable.im_playroom3 ,R.drawable.im_dgm3,
           R.drawable.im_cgm3,R.drawable.im_gm3,R.drawable.im_ps3,R.drawable.im_civilroom3,R.drawable.im_corridor3,R.drawable.im_visitorroom3,
           R.drawable.im_serverroom3,R.drawable.im_liftlobby3,R.drawable.im_stairs3,R.drawable.im_toiletcorridor3,R.drawable.im_toiletdis3,
           R.drawable.im_toiletf3,R.drawable.im_toiletm3,R.drawable.im_accounts3,R.drawable.im_bathroomm3,R.drawable.im_chef3,R.drawable.im_childcare3,R.drawable.im_cross3,R.drawable.im_entertain3,R.drawable.im_finance3,
           R.drawable.im_garden3,R.drawable.im_guest3,R.drawable.im_gym3,R.drawable.im_hall3,R.drawable.im_laundryy3,R.drawable.im_liftspace3,R.drawable.im_movie3,R.drawable.im_multimedia3,R.drawable.im_om3,R.drawable.im_pa3,R.drawable.im_poojaa3,R.drawable.im_psaccount3,R.drawable.im_psec3,
           R.drawable.im_recept3,R.drawable.im_servent3,R.drawable.im_sikh3,R.drawable.im_storeroom3,R.drawable.im_swimmingpool3,R.drawable.im_workstation3
   };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.editroomicons);

        edit_btn=(Button) findViewById(R.id.edit_btn);
        room_spinner = (Spinner) findViewById(R.id.room_name_spin);
        navigateBack=(ImageView) findViewById(R.id.imageView2);
        room_icons_spinner= (Spinner) findViewById(R.id.room_icon_spin);

        room_spinner.setOnItemSelectedListener(this);
        room_icons_spinner.setOnItemSelectedListener(this);


        //setting house name for wireless database
        StaticVariables.WHOUSE_NAME= StaticVariables.HOUSE_NAME+"_WLS";;
        //setting database name with which wireless database is to save
        StaticVariables.HOUSE_DB_NAME=StaticVariables.HOUSE_NAME+"_WLS";;

        SwbAdapter.OriginalDataBase= StaticVariabes_div.housename+".db";
        MasterAdapter.OriginalDataBase=StaticVariabes_div.housename+".db";

        try{
            houseDB=new HouseConfigurationAdapter(this);
            houseDB.open();			//opening house database

            WhouseDB=new WirelessConfigurationAdapter(this);
            WhouseDB.open();

            up_adap=new UpdateAdapter(this);

            mas_adap=new MasterAdapter(this);
            dev_adap = new SwbAdapter(this);

        }catch(Exception e){
            e.printStackTrace();
        }

       /* try {
            isDbExists=WhouseDB.checkdb();
        } catch (IOException e) {
            //e.printStackTrace();
            StaticVariables.printLog("TAG","unable to open database");
        }*/

  /*      if(isDbExists){

            //Fetching list of All room names from database and adding to local array list
            UniqueRoomList=new ArrayList<String>();
            UniqueRoomList.addAll(houseDB.RoomNameList());
            UniqueRoomList.addAll(WhouseDB.WirelessPanelsRoomNameList());
            UniqueRoomList.add("Select Room");

            //Loading data in room name spinner
            //roomNameAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList);
            //room_spinner.setAdapter(roomNameAdapter);

            Set<String> temprr = new LinkedHashSet<String>(UniqueRoomList );

            ArrayList<String> setList=new ArrayList<String>(temprr);

            UniqueRoomList=setList;

            //Loading data in room name spinner
            roomNameAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList);
            room_spinner.setAdapter(roomNameAdapter);
            // Displaying Last item of list
            room_spinner.setSelection(UniqueRoomList.size()-1);

        }else{*/

            fillRoomNameList();

       // }

        iconsAdapter=new MyAdapter(EditRoomIcons.this, R.layout.custom, images);
        // Setting a Custom Adapter to the Spinner
       // room_icons_spinner.setAdapter(new EditRoomIcons.MyAdapter(EditRoomIcons.this, R.layout.custom, images));
        room_icons_spinner.setAdapter( iconsAdapter);
        room_icons_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0) {
                    String imageName = getResources().getResourceName(images[position]);
                    //int actual=imageName.lastIndexOf(":drawable/",0);
                    int actual=imageName.indexOf(":drawable/",0);
                    String actimg=imageName.substring(actual+10,imageName.length()-1);
                    //Toast.makeText(AddNewDevice.this, actimg+"  name " + imageName, Toast.LENGTH_SHORT).show();
                    Room_Icon_Image=actimg;
                }else{

                    Room_Icon_Image="select";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        //setting on click listener on delete button
        edit_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String roomname=null;
                try{
                    roomname=room_spinner.getSelectedItem().toString();

                }catch(Exception e){
                    e.printStackTrace();
                }

                StaticVariabes_div.log( "roomnamold"+roomname,TAG1  );

                if((roomname!=null && !roomname.equals("Select Room")) ) {
                if((Room_Icon_Image!=null && !Room_Icon_Image.equals("select")&&Room_Icon_Image.length()>0) )
                {

                    EditWiredRoomIcons(Room_Icon_Image, roomName);

                            locallistupdate();

                }else{
                    popupinfo("Invalid Please select RoomIcon");
                }
                }else {
                    popupinfo("Invalid Please Select RoomName");
                }

            }
        });


        navigateBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goPrevious();
            }
        });
        StaticVariabes_div.log( "isDbExists"+isDbExists,TAG1  );
    }



    public void locallistupdate(){

        Update_db_version_number();
        try {
            LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
            locallist_adap = new LocalListArrangeTable(EditRoomIcons.this, StaticVariabes_div.housename,StaticVariabes_div.housename);
            locallist_adap.open();
            locallist_adap.deleteall();
            locallist_adap.close();
        }catch(Exception e){
            e.printStackTrace();

        }
    }
    public void EditWiredRoomIcons(String room_icn,String roomname){


            StaticVariabes_div.log( "roomaleadyexist"+roomaleadyexist,TAG1  );
            up_adap.open();
            boolean roomnamchanged=up_adap.update_roomicon_onlytwo(room_icn, roomname);
            up_adap.close();
            if(roomnamchanged){
                popupinfo("Updated Successfully");
                fillRoomNameList();
                fillRoomIconList();
            }else{
                popupinfo("Error Updating Roomname,Try Again");
            }

    }

    public void popupinfo(String msg){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    //filling room name list
    void fillRoomNameList(){

        UniqueRoomList.clear();

        //Fetching list of All room names from database and adding to local array list
        UniqueRoomList.addAll(houseDB.RoomNameList());
        UniqueRoomList.add("Select Room");


        //making adapter
        roomNameAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList);
        room_spinner.setAdapter(roomNameAdapter);
        room_spinner.setSelection(UniqueRoomList.size()-1);

    }

    void fillRoomIconList(){

        iconsAdapter=new MyAdapter(EditRoomIcons.this, R.layout.custom, images);
        room_icons_spinner.setAdapter( iconsAdapter);

    }


    //fill roomnm
    public void roomnamelist(){
        //Fetching list of All room names from database and adding to local array list
        UniqueRoomList=new ArrayList<String>();
        //UniqueRoomList.addAll(houseDB.WirelessRoomNameList());
        UniqueRoomList.addAll(WhouseDB.WirelessPanelsRoomNameList());
        UniqueRoomList.add("Select Room");

        //Loading data in room name spinner
        //roomNameAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList);
        //room_spinner.setAdapter(roomNameAdapter);

        Set<String> temprr = new LinkedHashSet<String>(UniqueRoomList );

        ArrayList<String> setList=new ArrayList<String>(temprr);

        UniqueRoomList=setList;

        //Loading data in room name spinner
        roomNameAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList);
        room_spinner.setAdapter(roomNameAdapter);
    }



    //spinner on item click listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch(parent.getId()){
            case R.id.room_name_spin:{
                if(position!=UniqueRoomList.size()-1){
                    //getting current room name
                     roomName=room_spinner.getSelectedItem().toString();
                    //getting current room number
                    CurrentRoomNo=houseDB.CurrentRoomNumber(roomName);

                    matched_room_image_name=houseDB.CurrentRoomImageName(roomName);

                    int nwimgnm = getResources().getIdentifier(matched_room_image_name+"3", "drawable", getApplicationContext().getPackageName());

                       for(int i=0; i < images.length; i++) {
                            if(nwimgnm==(images[i])){
                                room_icons_spinner.setSelection(i);
                            }
                       }

                }
                break;
            }

            default:
                break;
        }
    }


    //backpress event
    @Override
    public void onBackPressed() {
        goPrevious();
    }


    void goPrevious(){
        //going back to admin page
        Intent it=new Intent(EditRoomIcons.this,Configuration_Main.class);
        startActivity(it);
        //adding transition to activity exit
        overridePendingTransition(R.anim.slideup, R.anim.slidedown);
        finish();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public class MyAdapter extends ArrayAdapter {

        public MyAdapter(Context context, int textViewResourceId,
                         Integer[] objects) {
            super(context, textViewResourceId, objects);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

            // Inflating the layout for the custom Spinner
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom, parent, false);

            // Declaring and Typecasting the textview in the inflated layout
            TextView tvLanguage = (TextView) layout.findViewById(R.id.tvLanguage);

            // Setting the text using the array
            //tvLanguage.setText("Select images");
            //tvLanguage.setTextColor(Color.rgb(75, 180, 225));

            // Declaring and Typecasting the imageView in the inflated layout
            ImageView img = (ImageView) layout.findViewById(R.id.imgLanguage);

            // Setting an image using the id's in the array
            img.setImageResource(images[position]);

            // Setting Special atrributes for 1st element
            if (position == 0) {
                tvLanguage.setText("Select");
                tvLanguage.setGravity(Gravity.CENTER);
                // Removing the image view
                img.setVisibility(View.GONE);
                // Setting the size of the text
                tvLanguage.setTextSize(18f);
                // Setting the text Color
                tvLanguage.setTextColor(Color.WHITE);

            }
            else{
                tvLanguage.setVisibility(View.GONE);


            }

            return layout;
        }

        // It gets a View that displays in the drop down popup the data at the specified position
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // It gets a View that displays the data at the specified position
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }

    public void Update_db_version_number(){
        if(StaticVariabes_div.housename!=null) {
            try {
                ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
                sdadap = new ServerDetailsAdapter(EditRoomIcons.this);
                sdadap.open();
                Cursor cursrno = sdadap.fetchrefno(1);
                String home_versionnumber = cursrno.getString(cursrno.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_db));
                cursrno.close();
                Float fvernum = Float.valueOf(home_versionnumber) + 0.1f;
                String s_vernum = String.format("%.2f", fvernum);
                sdadap.update_versionnumber(s_vernum);
                sdadap.close();
            }catch(Exception e){

            }
        }
    }


}

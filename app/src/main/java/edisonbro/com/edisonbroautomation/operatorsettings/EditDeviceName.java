package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: EditDeviceName.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to Edit Device Name.
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.databasewired.MasterAdapter;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;

public class EditDeviceName extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner dev_name_spinner,room_spinner;
    HouseConfigurationAdapter houseDB=null;
    WirelessConfigurationAdapter WhouseDB=null;
    private MasterAdapter mas_adap;
    //private SwbAdapter dev_adap;
    SpinnerAdapter usr_devname_Adapter,roomNameAdapter;
    UpdateAdapter up_adap;
    ArrayList<String> UniqueRoomList=new ArrayList<String>();
    ArrayList<String> UniquedevnameList=new ArrayList<String>();
    int spinnerLayoutId= R.layout.spinnerlayout;
    boolean isDbExists=false;

    private ArrayList<String> listdevicesnumbers,listdevicesnames;
    String rgbnoarr[],rgbdevnamearr[],dimmrnoarr[],dimmrdevnamearr[]
    , curnoarr[],curdevnamearr[], projscrnoarr[],projscrdevnamearr[]
    , projliftnoarr[],projliftdevnamearr[], fannoarr[],fandevnameearr[]
    , swbnoarr[], swbdevnamearr[], gysernoarr[],gyserdevnamearr[], acnoarr[],acdevnamearr[]
   , pirnoarr[],pirdevnamearr[], gsknoarr[],gskdevnamearr[], clbnoarr[],clbdevnamearr[]
    , dlsnoarr[],dlsdevnamearr[], fmnoarr[],fmdevnamearr[], aqunoarr[],aqudevnamearr[]
    ,swdnoarr[],swddevnamearr[];

    private SwbAdapter swbadap;
    String CurrentRoomNo;
    ImageView navigateBack;
    Button edit_btn;
    EditText et_new_devname;
     boolean devicealeadyexist=false,devicealeadyexist_wrls=false,devicepresentpresent_swb=false,devicepresentpresent_master=false,deviceexist_wrls=false,deviceexist_confwrls=false;
    private static final String TAG1 = "Edit Device Name-";

    private ServerDetailsAdapter sdadap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_device_name);

        navigateBack=(ImageView) findViewById(R.id.imageView2);
        edit_btn=(Button) findViewById(R.id.edit_btn);
        room_spinner = (Spinner) findViewById(R.id.sp_room_list);
        dev_name_spinner = (Spinner) findViewById(R.id.sp_devname_list);

        et_new_devname=(EditText) findViewById(R.id.et_new_devname);

        room_spinner.setOnItemSelectedListener(this);
        dev_name_spinner.setOnItemSelectedListener(this);

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

            //opening wireless   database

            mas_adap=new MasterAdapter(this);
            swbadap = new SwbAdapter(this);



        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            isDbExists=WhouseDB.checkdb();
        } catch (IOException e) {
            //e.printStackTrace();
            StaticVariables.printLog("TAG","unable to open database");
        }

        if(isDbExists){

            fill_wire_wirelessroom();

     /*       //Fetching list of All room names from database and adding to local array list
            UniqueRoomList=new ArrayList<String>();
            UniqueRoomList.addAll(houseDB.RoomNameList());
            if(!WirelessConfigurationAdapter.sdb.isOpen()) {
                WhouseDB.open();
                UniqueRoomList.addAll(WhouseDB.WirelessPanelsRoomNameList());
            }
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
            //room_spinner.setSelection(UniqueRoomList.size()-1);*/

        }else{

            fillRoomNameList();

        }


        navigateBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goPrevious();
            }
        });


        //setting on click listener on delete button
        edit_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                devicealeadyexist=false;
                devicealeadyexist_wrls=false;
                String dev_name=null;
                try{
                    dev_name=dev_name_spinner.getSelectedItem().toString();

                }catch(Exception e){
                    e.printStackTrace();
                }

                StaticVariabes_div.log( "roomnamold"+dev_name,TAG1  );

                if((dev_name!=null && !dev_name.equals("Select Device")) )
                {


                    String value = et_new_devname.getText().toString();

                    StaticVariabes_div.log( "value"+value,TAG1  );
                    if(value.length()<1){
                       // value="DeviceName";
                        et_new_devname.setError("Please Enter DeviceName");

                    }else {

                        StaticVariabes_div.log("value new " + value + "dev_name" + dev_name, TAG1);

                        try {
                            //	WhouseDB=new WirelessConfigurationAdapter(EditRoomName.this);

                            if (isDbExists) {
                                EditWiredRoom_DeviceName(value, dev_name);

                                EditWirelessRoom_Devname(value, dev_name);

                            } else {
                                EditWiredRoom_DeviceName(value, dev_name);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }else{
                    popupinfo("Invalid Data Please Select DeviceName");
                }

            }
        });

    }

    public void fill_wire_wirelessroom(){
        //Fetching list of All room names from database and adding to local array list
        UniqueRoomList=new ArrayList<String>();
        UniqueRoomList.addAll(houseDB.RoomNameList());
        if(!WirelessConfigurationAdapter.sdb.isOpen()) {
            WhouseDB.open();
            UniqueRoomList.addAll(WhouseDB.WirelessPanelsRoomNameList());
        }
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
    }

    public void EditWiredRoom_DeviceName(String value,String devname){

        swbadap.open();
        Cursor curss=swbadap.checkdevnameexist(value);
        if(curss!=null){
            devicealeadyexist=true;
            curss.close();
        }


        Cursor cursdp=swbadap.checkdevnameexist(devname);
        if(cursdp!=null){
            devicepresentpresent_swb=true;
            cursdp.close();
        }


        swbadap.close();

        mas_adap.open();
        Cursor curmass=mas_adap.checkdevnameexist(value);
        if(curmass!=null){
            devicealeadyexist=true;
            curmass.close();
        }


        Cursor curmassdp=mas_adap.checkdevnameexist(devname);
        if(curmassdp!=null){
            devicepresentpresent_master=true;
            curmassdp.close();
        }


        mas_adap.close();





        if(!devicealeadyexist){

            StaticVariabes_div.log( "devicealeadyexist"+devicealeadyexist,TAG1  );
            up_adap.open();
            boolean devnamchanged=up_adap.update_devname_onlytwo(devname, value);
            up_adap.close();

            if(devnamchanged){
                popupinfo("Wired Device Name Updated Successfully");
                Update_db_version_number();
                if(isDbExists){
                    fill_wire_wirelessroom();
                }else {
                    fillRoomNameList();
                }
            }else{
                popupinfo("Error Updating Wired Device Name,Try Again");

            }

        }else{
            popupinfo("Wired Device Name Already Exists! Try Another Name");
        }

    }

    public void EditWirelessRoom_Devname(String value ,String dev_nm ){
       if(!WirelessConfigurationAdapter.sdb.isOpen()){
            WhouseDB.open();
            StaticVariables.printLog("TAG","DB open ");
        }

        Cursor curss=WhouseDB.checkdevnameexistWrls(value);
        if(curss!=null){
            devicealeadyexist_wrls=true;
        }

        Cursor curscnf=WhouseDB.checkdevnameexistconfgWrls(value);
        if(curscnf!=null){
            devicealeadyexist_wrls=true;
        }


        if(!devicealeadyexist_wrls){

            Cursor curss_wr=WhouseDB.checkdevnameexistWrls(dev_nm);
            if(curss_wr!=null){
                deviceexist_wrls=true;
            }

            Cursor curscnf_cng=WhouseDB.checkdevnameexistconfgWrls(dev_nm);
            if(curscnf_cng!=null){
                deviceexist_confwrls=true;
            }


            if(deviceexist_wrls||deviceexist_confwrls) {
                //update roomname data from wireless table
                boolean isWLS_updated = WhouseDB.EditDeviceName_Wireless(value, dev_nm);

                if (isWLS_updated) {
                    StaticVariables.printLog("TAG", "wireless details updated");
                    popupinfo("Wireless Device Name Updated Successfully");
                    Update_db_version_number();
                    fill_wire_wirelessroom();

                } else {
                    popupinfo("Error Updating Wireless Device Name!Try Again");
                }
            }else{
                Toast.makeText(EditDeviceName.this,"No Wireless Device",Toast.LENGTH_SHORT).show();
            }
        }else{
            value="";
            popupinfo("Wireless Device Name Already Exists");

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

    void goPrevious(){
        //going back to admin page
        Intent it=new Intent(EditDeviceName.this,Configuration_Main.class);
        startActivity(it);
        finish();
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

    //spinner on item click listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch(parent.getId()){
            case R.id.sp_room_list:{
                if(position!=UniqueRoomList.size()-1){

                    //getting current room name
                    String roomName=room_spinner.getSelectedItem().toString();
                    //getting current room number
                    CurrentRoomNo=""+houseDB.CurrentRoomNumber(roomName);

                    prepareList(CurrentRoomNo);

                }
                break;
            }

            default:
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public void prepareList(String roomno)
    {

        StaticVariabes_div.log("Edit in preparelist roomno"+roomno, TAG1);

        listdevicesnumbers = new ArrayList<String>();
        listdevicesnames = new ArrayList<String>();

        String[] finallistarr=null;
        String[] finaldevnumlistarr=null;
        String[]finaldevnamelistarr=null;


//****************************************************************************************************
        swbadap.open();
        int swbcount=swbadap.getCount_housenoroomnodevtypename(roomno,"SWB");
        swbnoarr=new String[swbcount];
        swbdevnamearr=new String[swbcount];

        swbnoarr=swbadap.getall_housenoroomnodevicetypename(swbcount, roomno, "SWB");

        swbdevnamearr=swbadap.getall_devicenames(swbcount, roomno, "SWB");

        if(swbcount!=0){

            listdevicesnumbers.addAll(Arrays.asList(swbnoarr));
            listdevicesnames.addAll(Arrays.asList(swbdevnamearr));
        }

        int swdcount=swbadap.getCount_housenoroomnodevtypename(roomno,"SWD");
        swdnoarr=new String[swdcount];
        swddevnamearr=new String[swdcount];

        swdnoarr=swbadap.getall_housenoroomnodevicetypename(swdcount, roomno, "SWD");
        swddevnamearr=swbadap.getall_devicenames(swbcount, roomno, "SWD");

        if(swdcount!=0){
            listdevicesnumbers.addAll(Arrays.asList(swdnoarr));
            listdevicesnames.addAll(Arrays.asList(swddevnamearr));
        }
         swbadap.close();

         mas_adap.open();


        //................................................................................
        int rgbcount=mas_adap.getCount_housenoroomnodevtypename(roomno,"RGB");
        rgbnoarr=new String[rgbcount];
        rgbdevnamearr=new String[rgbcount];

        rgbnoarr=mas_adap.getall_housenoroomnodevicetypename(rgbcount, roomno, "RGB");
        rgbdevnamearr=mas_adap.getall_roomnodevicenames(rgbcount, roomno, "RGB");

        if(rgbcount!=0){
            listdevicesnumbers.addAll(Arrays.asList(rgbnoarr));
            listdevicesnames.addAll(Arrays.asList(rgbdevnamearr));
        }
        //................................................................................
        int dmrcount=mas_adap.getCount_housenoroomnodevtypename(roomno,"DMR");
        dimmrnoarr=new String[dmrcount];
        dimmrdevnamearr=new String[dmrcount];

        dimmrnoarr=mas_adap.getall_housenoroomnodevicetypename(dmrcount, roomno, "DMR");
        dimmrdevnamearr=mas_adap.getall_roomnodevicenames(dmrcount, roomno, "DMR");

        if(dmrcount!=0){
            listdevicesnumbers.addAll(Arrays.asList(dimmrnoarr));
            listdevicesnames.addAll(Arrays.asList(dimmrdevnamearr));
        }
        //................................................................................

        int curcount=mas_adap.getCount_housenoroomnodevtypename( roomno,"CUR");
        curnoarr=new String[curcount];
        curdevnamearr=new String[curcount];

        curnoarr=mas_adap.getall_housenoroomnodevicetypename(curcount, roomno,"CUR");
        curdevnamearr=mas_adap.getall_roomnodevicenames(curcount, roomno,"CUR");

        if(curcount!=0){
            listdevicesnumbers.addAll(Arrays.asList(curnoarr));
            listdevicesnames.addAll(Arrays.asList(curdevnamearr));
        }


        //................................................................................
        int fancount=mas_adap.getCount_housenoroomnodevtypename( roomno,"FAN");
        fannoarr=new String[fancount];
        fandevnameearr=new String[fancount];

        fannoarr=mas_adap.getall_housenoroomnodevicetypename(fancount, roomno,"FAN");
        fandevnameearr=mas_adap.getall_roomnodevicenames(fancount, roomno,"FAN");

        if(fancount!=0){
            listdevicesnumbers.addAll(Arrays.asList(fannoarr));
            listdevicesnames.addAll(Arrays.asList(fandevnameearr));
        }

        //..................................................................................................
        int geysercount=mas_adap.getCount_housenoroomnodevtypename( roomno,"GSR");
        gysernoarr=new String[geysercount];
        gyserdevnamearr=new String[geysercount];

        gysernoarr=mas_adap.getall_housenoroomnodevicetypename(geysercount, roomno,"GSR");
        gyserdevnamearr=mas_adap.getall_roomnodevicenames(geysercount, roomno,"GSR");

        if(geysercount!=0){
            listdevicesnumbers.addAll(Arrays.asList(gysernoarr));
            listdevicesnames.addAll(Arrays.asList(gyserdevnamearr));

        }
        //...........................................................................................
        int account=mas_adap.getCount_housenoroomnodevtypename( roomno,"ACR");
        acnoarr=new String[account];
        acdevnamearr=new String[account];

        acnoarr=mas_adap.getall_housenoroomnodevicetypename(account, roomno,"ACR");
        acdevnamearr=mas_adap.getall_roomnodevicenames(account, roomno,"ACR");


        if(account!=0){
            listdevicesnumbers.addAll(Arrays.asList(acnoarr));
            listdevicesnames.addAll(Arrays.asList(acdevnamearr));
        }

        //............................................................................

        int pircount=mas_adap.getCount_housenoroomnodevtypename( roomno,"PIR");
        pirnoarr=new String[pircount];
        pirdevnamearr=new String[pircount];

        pirnoarr=mas_adap.getall_housenoroomnodevicetypename(pircount, roomno,"PIR");
        pirdevnamearr=mas_adap.getall_roomnodevicenames(pircount, roomno,"PIR");


        if(pircount!=0){
            listdevicesnumbers.addAll(Arrays.asList(pirnoarr));
            listdevicesnames.addAll(Arrays.asList(pirdevnamearr));

        }
        //............................................................................

        int clbcount=mas_adap.getCount_housenoroomnodevtypename( roomno,"CLB");
        clbnoarr=new String[clbcount];
        clbdevnamearr=new String[clbcount];

        clbnoarr=mas_adap.getall_housenoroomnodevicetypename(clbcount, roomno,"CLB");
        clbdevnamearr=mas_adap.getall_roomnodevicenames(clbcount, roomno,"CLB");

        if(clbcount!=0){
            listdevicesnumbers.addAll(Arrays.asList(clbnoarr));
            listdevicesnames.addAll(Arrays.asList(clbdevnamearr));

        }
        //............................................................................

        int dlscount=mas_adap.getCount_housenoroomnodevtypename( roomno,"DLS");
        dlsnoarr=new String[dlscount];
        dlsdevnamearr=new String[dlscount];

        dlsnoarr=mas_adap.getall_housenoroomnodevicetypename(dlscount, roomno,"DLS");
        dlsdevnamearr=mas_adap.getall_roomnodevicenames(dlscount, roomno,"DLS");

        if(dlscount!=0){
            listdevicesnumbers.addAll(Arrays.asList(dlsnoarr));
            listdevicesnames.addAll(Arrays.asList(dlsdevnamearr));

        }
        //............................................................................

        int fmcount=mas_adap.getCount_housenoroomnodevtypename( roomno,"FMD");

        fmnoarr=new String[fmcount];
        fmdevnamearr=new String[fmcount];

        fmnoarr=mas_adap.getall_housenoroomnodevicetypename(fmcount, roomno,"FMD");
        fmdevnamearr=mas_adap.getall_roomnodevicenames(fmcount, roomno,"FMD");

        if(fmcount!=0){
            listdevicesnumbers.addAll(Arrays.asList(fmnoarr));
            listdevicesnames.addAll(Arrays.asList(fmdevnamearr));

        }

        //...................................................................................................

        int projscrcount=mas_adap.getCount_housenoroomnodevtypename( roomno,"PSC");
        projscrnoarr=new String[projscrcount];
        projscrdevnamearr=new String[projscrcount];

        projscrnoarr=mas_adap.getall_housenoroomnodevicetypename(projscrcount, roomno,"PSC");
        projscrdevnamearr=mas_adap.getall_roomnodevicenames(projscrcount, roomno,"PSC");


        if(projscrcount!=0){
            listdevicesnumbers.addAll(Arrays.asList(projscrnoarr));
            listdevicesnames.addAll(Arrays.asList(projscrdevnamearr));
        }
        //...................................................................................................

        int projliftcount=mas_adap.getCount_housenoroomnodevtypename( roomno,"PLC");
        projliftnoarr=new String[projliftcount];
        projliftdevnamearr=new String[projliftcount];

        projliftnoarr=mas_adap.getall_housenoroomnodevicetypename(projliftcount, roomno,"PLC");
        projliftdevnamearr=mas_adap.getall_roomnodevicenames(projliftcount, roomno,"PLC");

        if(projliftcount!=0){
            listdevicesnumbers.addAll(Arrays.asList(projliftnoarr));
            listdevicesnames.addAll(Arrays.asList(projliftdevnamearr));
        }
        //..................................................................................................

        int gskcount=mas_adap.getCount_housenoroomnodevtypename( roomno,"GSK");
        gsknoarr=new String[gskcount];
        gskdevnamearr=new String[gskcount];

        gsknoarr=mas_adap.getall_housenoroomnodevicetypename(gskcount, roomno,"GSK");
        gskdevnamearr=mas_adap.getall_roomnodevicenames(gskcount, roomno,"GSK");

        if(gskcount!=0){
            listdevicesnumbers.addAll(Arrays.asList(gsknoarr));
            listdevicesnames.addAll(Arrays.asList(gskdevnamearr));
        }
        //...........................................................................................
        int aqucount=mas_adap.getCount_housenoroomnodevtypename( roomno,"AQU");
        aqunoarr=new String[aqucount];
        aqudevnamearr=new String[aqucount];

        aqunoarr=mas_adap.getall_housenoroomnodevicetypename(aqucount, roomno,"AQU");
        aqudevnamearr=mas_adap.getall_roomnodevicenames(aqucount, roomno,"AQU");


        if(aqucount!=0){
            listdevicesnumbers.addAll(Arrays.asList(aqunoarr));
            listdevicesnames.addAll(Arrays.asList(aqudevnamearr));
        }

         mas_adap.close();

        finaldevnumlistarr=listdevicesnumbers.toArray( new String[listdevicesnumbers.size()] );
        finaldevnamelistarr=listdevicesnames.toArray( new String[listdevicesnames.size()] );


        if(listdevicesnames!=null){
            UniquedevnameList.clear();
            UniquedevnameList.addAll(listdevicesnames);
            UniquedevnameList.add("Select Device");



            usr_devname_Adapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniquedevnameList);
            dev_name_spinner.setAdapter(usr_devname_Adapter);
            dev_name_spinner.setSelection(UniquedevnameList.size()-1);
        }


        for(int k=0;k<finaldevnamelistarr.length;k++){
            StaticVariabes_div.log("ffinaldevnamelistarr"+finaldevnamelistarr[k], TAG1);
        }


    }
    public void Update_db_version_number(){
        if(StaticVariabes_div.housename!=null) {
            try {
                ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
                sdadap = new ServerDetailsAdapter(EditDeviceName.this);
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

    //backpress event
    @Override
    public void onBackPressed() {
        goPrevious();
    }
}

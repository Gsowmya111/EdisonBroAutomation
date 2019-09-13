package edisonbro.com.edisonbroautomation.operatorsettings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
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
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;

public class Edit_Alexa extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG1 = "Edit Alexa Name-";
    Spinner dev_name_spinner,room_spinner,new_room_spinner;
    HouseConfigurationAdapter houseDB=null;
    WirelessConfigurationAdapter WhouseDB=null;
    private MasterAdapter mas_adap;
    EditText et_new_devname,et_new_devid;
    SpinnerAdapter usr_devname_Adapter,roomNameAdapter;
    UpdateAdapter up_adap;
    Button edit_btn,move_btn;
    ImageView navigateBack;
    TextView tv_alexaid;
    ArrayList<String> UniqueRoomList=new ArrayList<String>();
    ArrayList<String> UniquedevnameList=new ArrayList<String>();
    String alexanoarr[],alexadevnamearr[];
    int spinnerLayoutId= R.layout.spinnerlayout;
    String CurrentRoomNo,alexa_id,NewRoomNo;
    private ArrayList<String> listdevicesnumbers,listdevicesnames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_edit_alexa);

        navigateBack=(ImageView) findViewById(R.id.imageView2);
        edit_btn=(Button) findViewById(R.id.edit_btn);
        room_spinner = (Spinner) findViewById(R.id.sp_room_list);
        dev_name_spinner = (Spinner) findViewById(R.id.sp_devname_list);

        et_new_devname=(EditText) findViewById(R.id.et_new_devname);
        et_new_devid=(EditText) findViewById(R.id.et_new_devid);
        tv_alexaid=(TextView)findViewById(R.id.txt_mac);
        new_room_spinner = (Spinner) findViewById(R.id.sp_newroom_list);

        move_btn=(Button) findViewById(R.id.move_btn);

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

            mas_adap=new MasterAdapter(this);


            fill_wire_wirelessroom();

        }catch(Exception e){
            e.printStackTrace();
        }


        navigateBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goPrevious();
            }
        });

        move_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String roomName=room_spinner.getSelectedItem().toString();

                if(roomName!=null&&roomName.length()>0) {
                    if(roomName.equals("Select Room")){
                        Toast.makeText(Edit_Alexa.this,"please select room",Toast.LENGTH_SHORT).show();

                    }else{
                        //getting current room number
                        CurrentRoomNo = "" + houseDB.CurrentRoomNumber(roomName);
                        //getting current room name
                        if(dev_name_spinner.getSelectedItem()!=null){
                        String dev = dev_name_spinner.getSelectedItem().toString();

                        if(dev!=null&&dev.length()>0) {
                            String newroomsp = new_room_spinner.getSelectedItem().toString();

                            NewRoomNo = "" + houseDB.CurrentRoomNumber(newroomsp);
                            if (newroomsp != null && newroomsp.length() > 0) {

                                if(!(roomName.equals(newroomsp))) {
                                    boolean upd = houseDB.updateMasterTable(NewRoomNo, newroomsp, dev);
                                    if (upd) {

                                        fill_wire_wirelessroom();
                                        alexa_id = "";
                                        tv_alexaid.setText("");
                                        popupinfo("Moved Successfully");
                                    } else {
                                        popupinfo("Error Please Try Again");
                                    }
                                }else {
                                    popupinfo("please select other room");
                                }
                            } else {
                                Toast.makeText(Edit_Alexa.this, "please select room", Toast.LENGTH_SHORT).show();

                            }
                         }
                        }else{
                            Toast.makeText(Edit_Alexa.this,"please select device",Toast.LENGTH_SHORT).show();

                        }

                    }
                }else{
                    Toast.makeText(Edit_Alexa.this,"please select room",Toast.LENGTH_SHORT).show();

                }
            }
        });


        edit_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String roomName=room_spinner.getSelectedItem().toString();

                if(roomName!=null&&roomName.length()>0) {
                    if(roomName.equals("Select Room")){
                        Toast.makeText(Edit_Alexa.this,"Please Select Room",Toast.LENGTH_SHORT).show();

                    }else{
                        //getting current room number
                        CurrentRoomNo = "" + houseDB.CurrentRoomNumber(roomName);
                        //getting current room name
                        if(dev_name_spinner.getSelectedItem()!=null){
                            String dev = dev_name_spinner.getSelectedItem().toString();

                            if(dev!=null&&dev.length()>0) {
                                String newdevname = et_new_devname.getText().toString();
                                String newdevid = et_new_devid.getText().toString();


                                if (newdevname != null && newdevname.length() > 0&&newdevid!=null&& newdevid.length() > 0) {

                                    if(!(newdevname.equals(dev))) {
                                        boolean upd = houseDB.update_MstrTble_name_id(dev,newdevname,newdevid,"both");
                                        if (upd) {

                                            fill_wire_wirelessroom();
                                            alexa_id = "";
                                            tv_alexaid.setText("");
                                            et_new_devname.setText("");
                                            et_new_devid.setText("");
                                            popupinfo("Name And Id Updated Successfully");
                                        } else {
                                            popupinfo("Error Please Try Again");
                                        }
                                    }else {
                                        popupinfo("Please Enter Other Name");
                                    }
                                }else if(newdevname != null && newdevname.length() > 0&& newdevid.length() ==0){
                                    if(!(newdevname.equals(dev))) {
                                        boolean upd = houseDB.update_MstrTble_name_id(dev,newdevname,newdevid,"nam");
                                        if (upd) {

                                            fill_wire_wirelessroom();
                                            alexa_id = "";
                                            tv_alexaid.setText("");
                                            et_new_devname.setText("");
                                            et_new_devid.setText("");
                                            popupinfo("Name Updated Successfully");
                                        } else {
                                            popupinfo("Error Please Try Again");
                                        }
                                    }else {
                                        popupinfo("Please Enter Other Name");
                                    }
                                }else if(newdevname.length() == 0&&newdevid!=null&& newdevid.length() > 0){
                                    if(!(newdevid.equals(alexa_id))) {
                                        boolean upd = houseDB.update_MstrTble_name_id(dev,newdevname,newdevid,"id");
                                        if (upd) {

                                            fill_wire_wirelessroom();
                                            alexa_id = "";
                                            tv_alexaid.setText("");
                                            et_new_devname.setText("");
                                            et_new_devid.setText("");
                                            popupinfo("Id Updated Successfully");
                                        } else {
                                            popupinfo("Error Please Try Again");
                                        }
                                    }else {
                                        popupinfo("Please Enter Other Name");
                                    }
                                }
                                else {
                                    Toast.makeText(Edit_Alexa.this, "Please Enter Details", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }else{
                            Toast.makeText(Edit_Alexa.this,"Please Select Device",Toast.LENGTH_SHORT).show();

                        }

                    }
                }else{
                    Toast.makeText(Edit_Alexa.this,"Please Select Room",Toast.LENGTH_SHORT).show();

                }
            }
        });
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
        Intent it=new Intent(Edit_Alexa.this,Configuration_Main.class);
        startActivity(it);
        finish();
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

        new_room_spinner.setAdapter(roomNameAdapter);
        // Displaying Last item of list
    }
    //spinner on item click listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch(parent.getId()){
            case R.id.sp_room_list:{
                if(position!=UniqueRoomList.size()-1){

                    alexa_id="";
                    tv_alexaid.setText("");
                    //getting current room name
                    String roomName=room_spinner.getSelectedItem().toString();
                    //getting current room number
                    CurrentRoomNo=""+houseDB.CurrentRoomNumber(roomName);

                    prepareList(CurrentRoomNo);

                }
                break;
            }
            case R.id.sp_devname_list:{
                if(position!=UniquedevnameList.size()-1){

                    //getting current room name
                    String  dev=dev_name_spinner.getSelectedItem().toString();
                    //getting current room number
                    alexa_id=""+houseDB.getalexa_id(dev);

                    tv_alexaid.setText(alexa_id);

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


        mas_adap.open();


        //................................................................................
        int alexacount=mas_adap.getCount_housenoroomnodevtypename(roomno,"ALXA");
        alexanoarr=new String[alexacount];
        alexadevnamearr=new String[alexacount];

        alexanoarr=mas_adap.getall_housenoroomnodevicetypename(alexacount, roomno, "ALXA");
        alexadevnamearr=mas_adap.getall_roomnodevicenames(alexacount, roomno, "ALXA");

        if(alexacount!=0){
            listdevicesnumbers.addAll(Arrays.asList(alexanoarr));
            listdevicesnames.addAll(Arrays.asList(alexadevnamearr));
        }
        //................................................................................


        mas_adap.close();

        finaldevnumlistarr=listdevicesnumbers.toArray( new String[listdevicesnumbers.size()] );
        finaldevnamelistarr=listdevicesnames.toArray( new String[listdevicesnames.size()] );


        if(listdevicesnames!=null){
            UniquedevnameList.clear();
            UniquedevnameList.addAll(listdevicesnames);
            UniquedevnameList.add("Select Device");



            usr_devname_Adapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniquedevnameList);
            dev_name_spinner.setAdapter(usr_devname_Adapter);
           // dev_name_spinner.setSelection(UniquedevnameList.size()-1);
        }


        for(int k=0;k<finaldevnamelistarr.length;k++){
            StaticVariabes_div.log("ffinaldevnamelistarr"+finaldevnamelistarr[k], TAG1);
        }


    }

    //backpress event
    @Override
    public void onBackPressed() {
        goPrevious();
    }
}

package edisonbro.com.edisonbroautomation.operatorsettings;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;


public class UpdateAdapter {
	private Context mcontext;
	private SQLiteDatabase dbs;
	//private a$a_A camera_dbhelp;
	public static String ORIGINAL_PATH =null;
	public static String OriginalDataBase = StaticVariabes_div.housename+".db";
	private static final String TAG1="Update Adap  - ";
	public static final String KEY_b = "b";
	public static final String KEY_ec = "ec";
	public static final String KEY_eb = "eb";
	public static final String KEY_ed = "ed";

	public UpdateAdapter(Context context) {
		mcontext = context;
		ORIGINAL_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";
	}

	public void open() throws SQLException {
		try {
			String path = ORIGINAL_PATH + OriginalDataBase;
			StaticVariabes_div.log("Databse Path is " + path, TAG1);
			dbs = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READWRITE
					+ SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (dbs != null) {
				dbs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public boolean updateroomname(String roomname,String newroomnam){
	
		roomname="'"+roomname+"'";
		open();
		dbs.beginTransaction();
		try{
			ContentValues args = new ContentValues();
			   args.put(KEY_b, newroomnam);
			boolean swb=dbs.update("SwitchBoardTable", args, KEY_b + "=" + roomname, null)>0;
			boolean mas=dbs.update("MasterTable", args, KEY_b + "=" + roomname, null)>0;
			boolean camb=dbs.update("CameraTable", args, KEY_b + "=" + roomname, null)>0;
			boolean ircon=dbs.update("IRConfigTable", args, KEY_b + "=" + roomname, null)>0;
			boolean irdb=dbs.update("IRBlasterTable", args, KEY_b + "=" + roomname, null)>0;		
			dbs.setTransactionSuccessful();
			return true;
		}catch(Exception ce){
			ce.printStackTrace(); 
		}finally{
			dbs.endTransaction();
			close();
		}
		return false;

	}
	public boolean updateroomnamechange(String roomname,String newroomnam){
		
		roomname="'"+roomname+"'";
		open();
		//dbs.beginTransaction();
		try{
			ContentValues args = new ContentValues();
			   args.put(KEY_b, newroomnam);
			boolean swb=dbs.update("SwitchBoardTable", args, KEY_b + "=" + roomname, null)>0;
			boolean mas=dbs.update("MasterTable", args, KEY_b + "=" + roomname, null)>0;
			boolean camb=dbs.update("CameraTable", args, KEY_b + "=" + roomname, null)>0;
			boolean ircon=dbs.update("IRConfigTable", args, KEY_b + "=" + roomname, null)>0;
			boolean irdb=dbs.update("IRBlasterTable", args, KEY_b + "=" + roomname, null)>0;		
			//dbs.setTransactionSuccessful();
			return true;
		}catch(Exception ce){
			ce.printStackTrace(); 
		}finally{
			//dbs.endTransaction();
			close();
		}
		return false;

	}

	public boolean update_roomname_onlytwo(String roomname,String newroomnam){

		roomname="'"+roomname+"'";
		open();
		//dbs.beginTransaction();
		try{
			ContentValues args = new ContentValues();
			args.put(KEY_b, newroomnam);
			boolean swb=dbs.update("SwitchBoardTable", args, KEY_b + "=" + roomname, null)>0;
			boolean mas=dbs.update("MasterTable", args, KEY_b + "=" + roomname, null)>0;

			//dbs.setTransactionSuccessful();
			return true;
		}catch(Exception ce){
			ce.printStackTrace();
		}finally{
			//dbs.endTransaction();
			close();
		}
		return false;

	}


	public boolean update_roomicon_onlytwo(String roomicon,String roomname){

		roomname="'"+roomname+"'";
		open();
		//dbs.beginTransaction();
		try{
			ContentValues args = new ContentValues();
			args.put(KEY_eb, roomicon);
			boolean swb=dbs.update("SwitchBoardTable", args, KEY_b + "=" + roomname, null)>0;
			boolean mas=dbs.update("MasterTable", args, KEY_b + "=" + roomname, null)>0;

			//dbs.setTransactionSuccessful();
			return true;
		}catch(Exception ce){
			ce.printStackTrace();
		}finally{
			//dbs.endTransaction();
			close();
		}
		return false;

	}

	public boolean update_devname_onlytwo(String devname,String newroomnam){

		devname="'"+devname+"'";
		open();
		//dbs.beginTransaction();
		try{
			ContentValues args = new ContentValues();
			args.put(KEY_ec, newroomnam);
			boolean swb=dbs.update("SwitchBoardTable", args, KEY_ec + "=" + devname, null)>0;
			boolean mas=dbs.update("MasterTable", args, KEY_ec + "=" + devname, null)>0;

			//dbs.setTransactionSuccessful();
			if(swb||mas)
			return true;
		}catch(Exception ce){
			ce.printStackTrace();
		}finally{
			//dbs.endTransaction();
			close();
		}
		return false;

	}

	public boolean update_dev_version(String devname,String newversion){

		devname="'"+devname+"'";
		open();
		//dbs.beginTransaction();
		try{
			ContentValues args = new ContentValues();
			args.put(KEY_ed, newversion);
			boolean swb=dbs.update("SwitchBoardTable", args, KEY_ec + "=" + devname, null)>0;
			boolean mas=dbs.update("MasterTable", args, KEY_ec + "=" + devname, null)>0;

			//dbs.setTransactionSuccessful();
			if(swb||mas)
				return true;
		}catch(Exception ce){
			ce.printStackTrace();
		}finally{
			//dbs.endTransaction();
			close();
		}
		return false;

	}
	public boolean deleteroom(String roomname){
		
		roomname="'"+roomname+"'";
		open();
		dbs.beginTransaction();
		try{
			
			boolean swb=dbs.delete("SwitchBoardTable", KEY_b + "=" + roomname, null)>0;
			boolean mas=dbs.delete("MasterTable", KEY_b + "=" + roomname, null)>0;
			boolean camb=dbs.delete("CameraTable", KEY_b + "=" + roomname, null)>0;
			boolean ircon=dbs.delete("IRConfigTable", KEY_b + "=" + roomname, null)>0;
			boolean irdb=dbs.delete("IRBlasterTable", KEY_b + "=" + roomname, null)>0;		
			dbs.setTransactionSuccessful();
			return true;
		}catch(Exception ce){
			ce.printStackTrace(); 
		}finally{
			dbs.endTransaction();
			close();
		}
		return false;

	}
	
	
	
}

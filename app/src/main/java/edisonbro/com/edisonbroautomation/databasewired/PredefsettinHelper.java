package edisonbro.com.edisonbroautomation.databasewired;


/**
 *  FILENAME: PredefsettinHelper.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database class for creating Predefined settings table[contains details of all devices set for mood setting] .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;

public class PredefsettinHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "PredefDetailsdb";

	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table PredefDetails_table ( _id integer primary key , "
			+"hn integer,hna text null,rn integer,rna text null,stno text null,stna text null,"
			+"dt text null,dna text null,dno text null,wd text null,dd text null,ea text null,eb text null,ec text null,"
			+"ed text null,ee text null,ef text null,eg text null,eh text null,ei text null,ej text null);";

	public PredefsettinHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		StaticVariabes_div.log("HttpConnect", "createdr" );
	}

	// Method is called during an upgrade of the database, e.g. if you increase
	// the database version
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(PredefsettinHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS todo");
		onCreate(database);
	}
}

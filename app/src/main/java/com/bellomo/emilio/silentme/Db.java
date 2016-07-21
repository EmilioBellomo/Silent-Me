package com.bellomo.emilio.silentme;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class Db extends SQLiteOpenHelper{
	
	private static final String DB_NAME = "silent.me.db";
	private static final int DB_VERSION = 1;

	public Db(Context context){
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		// TODO: Implement this method
		db.execSQL(Statements.SILENT_STATEMENT);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		// TODO: Implement this method
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onConfigure(SQLiteDatabase db){
		// TODO: Implement this method
		super.onConfigure(db);
		db.setForeignKeyConstraintsEnabled(true);
	}
	
}

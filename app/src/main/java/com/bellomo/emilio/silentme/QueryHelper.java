package com.bellomo.emilio.silentme;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class QueryHelper{
	
	public static final int setBulkInsert(Context context, String table, SQLiteDatabase myDb, ContentValues[] values){

		int returnCount = 0;

		try {

			for (ContentValues value : values) {

				final long _id = myDb.replace(table, null, value);
				
				if (_id != -1)
					returnCount++;
			}

			myDb.setTransactionSuccessful();

		} finally {
			myDb.endTransaction();
		}
		
		return returnCount;
	}

}

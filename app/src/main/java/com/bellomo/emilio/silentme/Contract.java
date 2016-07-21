package com.bellomo.emilio.silentme;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

public class Contract{
	
	public static final String CONTENT_AUTHORITY = "silent.me.app.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class Silent{

        public static final String TABLE_NAME = "silent";
		
        public static final String COLUMN_ID = TABLE_NAME + "_id";
        public static final String COLUMN_START = TABLE_NAME + "_start";
        public static final String COLUMN_END = TABLE_NAME + "_end";
        public static final String COLUMN_DAY = TABLE_NAME + "_days";
        public static final String COLUMN_REPEAT = TABLE_NAME + "_repeat";
        public static final String COLUMN_CLOSED = TABLE_NAME + "_closed";
        public static final String COLUMN_META = TABLE_NAME + "_meta";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

		public static Uri buildUri(long id){
				return ContentUris.withAppendedId(CONTENT_URI, id);
        }
				
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

    }
}

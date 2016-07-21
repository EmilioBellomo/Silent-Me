package com.bellomo.emilio.silentme;

public class Statements {

    private static final String START_STATEMENTS = "create table if not exists ";

    public static final String SILENT_STATEMENT = START_STATEMENTS + Contract.Silent.TABLE_NAME + " (" +
			Contract.Silent.COLUMN_ID + " integer primary key, " +
			Contract.Silent.COLUMN_START + " DATETIME, " +
			Contract.Silent.COLUMN_END + " DATETIME, " +
			Contract.Silent.COLUMN_DAY + " VARCHAR(16) default 0, " +
			Contract.Silent.COLUMN_REPEAT + " TINYINT default 0, " +
			Contract.Silent.COLUMN_CLOSED + " TINYINT default 0, " +
			Contract.Silent.COLUMN_META + " TEXT);";
	
}
package com.bellomo.emilio.silentme;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class SilentMeProvider extends ContentProvider {
    private Db db;
    private static final int CODE_GET_ONE = 100;
    private static final int CODE_GET_LIST = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public SilentMeProvider() {
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;

        matcher.addURI(authority, Contract.Silent.TABLE_NAME,
                CODE_GET_LIST);

        matcher.addURI(authority, Contract.Silent.TABLE_NAME + "/#",
                CODE_GET_ONE);

        return matcher;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        //istanzio il db qui
        db = new Db(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {

            case CODE_GET_LIST:

                cursor = db.getReadableDatabase().query(
                        Contract.Silent.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null, sortOrder
                );

                break;

            case CODE_GET_ONE:

                cursor = db.getReadableDatabase().query(
                        Contract.Silent.TABLE_NAME,
                        projection,
                        Contract.Silent.COLUMN_ID + " = ?",
                        new String[]{uri.getLastPathSegment()},
                        null, null, sortOrder
                );

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri returnUri = null;
        long id = -1;

        switch (sUriMatcher.match(uri)) {

            case CODE_GET_LIST:

                id = db.getWritableDatabase().insert(
                        Contract.Silent.TABLE_NAME,
                        null, values
                );

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        returnUri = ContentUris.withAppendedId(uri, id);

        if (!returnUri.getLastPathSegment().equals("-1"))
            getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {

        int rows = 0;

        final SQLiteDatabase myDb = db.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case CODE_GET_LIST:
                rows = myDb.delete(Contract.Silent.TABLE_NAME, where, whereArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if (rows > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        // TODO: Implement this method

        int rows = 0;

        switch (sUriMatcher.match(uri)) {

            case CODE_GET_ONE:

                rows = db.getWritableDatabase().update(
                        Contract.Silent.TABLE_NAME,
                        values,
                        Contract.Silent.COLUMN_ID + " = ?",
                        new String[]{uri.getLastPathSegment()}
                );

                break;

            case CODE_GET_LIST:

                rows = db.getWritableDatabase().update(
                        Contract.Silent.TABLE_NAME,
                        values,
                        where,
                        whereArgs
                );

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if (rows > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rows;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase myDb = db.getWritableDatabase();
        myDb.beginTransaction();

        int returnCount = 0;

        switch (sUriMatcher.match(uri)) {

            case CODE_GET_LIST:
                returnCount = QueryHelper.setBulkInsert(getContext(), Contract.Silent.TABLE_NAME, myDb, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        if (returnCount > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return returnCount;
    }
}

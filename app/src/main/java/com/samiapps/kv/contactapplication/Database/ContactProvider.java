package com.samiapps.kv.contactapplication.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ContactProvider extends ContentProvider {
    final int Contact=10;
   
    public static ContactDb contactDatabase;
    public static final UriMatcher uriMatcher=buildUriMatcher();

    @Override
    public boolean onCreate() {
        contactDatabase=new ContactDb(getContext());
        return true;
    }
    public static UriMatcher buildUriMatcher()
    {
        UriMatcher umatcher=new UriMatcher(UriMatcher.NO_MATCH);
        umatcher.addURI(ContactContract.Content_Authority,ContactContract.path_contact,10);

        return umatcher;

    }






    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (uriMatcher.match(uri))
        {
            case Contact: {

                retCursor = contactDatabase.getReadableDatabase().query(ContactContract.ContactC.tableName, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }




    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case Contact: {
                return ContactContract.ContactC.Content_Type;

            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }


    @Override
    public Uri insert (Uri uri, ContentValues values){
        Log.d("uric",uri.toString());
        final SQLiteDatabase db = contactDatabase.getWritableDatabase();
        Uri resultUri;
        switch (uriMatcher.match(uri)) {
            case Contact: {
                int id = (int) db.insert(ContactContract.ContactC.tableName, null, values);
                if (id > 0) {
                    resultUri = ContactContract.ContactC.BuildUriFromId(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;


            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;


    }

    @Override
    public int delete (Uri uri, String selection, String[]selectionArgs){
        final SQLiteDatabase db = contactDatabase.getWritableDatabase();
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (uriMatcher.match(uri)) {
            case Contact:
                rowsDeleted = db.delete(
                        ContactContract.ContactC.tableName, selection, selectionArgs);
                break;



            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update (Uri uri, ContentValues values, String selection, String[]selectionArgs){
        final SQLiteDatabase db = contactDatabase.getWritableDatabase();
        int rowsUpdated;

        switch (uriMatcher.match(uri)) {
            case Contact:
                rowsUpdated = db.update(ContactContract.ContactC.tableName, values, selection,
                        selectionArgs);
                break;



            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
    @Override
    public int bulkInsert(Uri uri,ContentValues[] values)
    {
        final SQLiteDatabase db=contactDatabase.getWritableDatabase();
        final int match=uriMatcher.match(uri);
        switch (match)
        {
            case Contact: {
                db.beginTransaction();
                int retcount = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insert(ContactContract.ContactC.tableName, null, value);
                        if (id != -1) {

                            retcount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return retcount;
            }


            default:
                return super.bulkInsert(uri,values);

        }

    }
}

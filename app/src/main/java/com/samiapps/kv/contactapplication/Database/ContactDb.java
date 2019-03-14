package com.samiapps.kv.contactapplication.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDb extends SQLiteOpenHelper {
    public static final int Database_version=1;
    public static final String Database_name="contact.db";
    public ContactDb(Context context) {
        super(context,Database_name,null,Database_version);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_CONTACT_TABLE = "CREATE TABLE " + ContactContract.ContactC.tableName + " (" +
                ContactContract.ContactC.Column_Contactid+ " INTEGER PRIMARY KEY , " +
                ContactContract.ContactC.COLUMN_FirstName + " TEXT NOT NULL, " +
                ContactContract.ContactC.COLUMN_LastName + " TEXT NOT NULL, "+
                ContactContract.ContactC.COLUMN_Image+ " TEXT );";


        //MovieContract.TrailerC.Column_MovieKey + " TEXT UNIQUE NOT NULL);";



        db.execSQL(SQL_CREATE_CONTACT_TABLE);


    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+ ContactContract.ContactC.tableName);


        onCreate(db);
    }

    public void deleteAll(SQLiteDatabase db)
    {
        //SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        //db.execSQL("delete * from"+ TABLE_NAME);
        db.execSQL("delete from "+ ContactContract.ContactC.tableName);
        db.close();
    }
}

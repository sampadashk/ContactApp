package com.samiapps.kv.contactapplication.Database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContactContract {
    public static final String Content_Authority = "com.samiapps.kv.contactapplication";
    public static final Uri Base_Uri = Uri.parse("content://" + Content_Authority);
    public static final String path_contact = "contact";


    public static class ContactC implements BaseColumns {
        public static Uri Content_Uri = Base_Uri.buildUpon().appendPath(path_contact).build();

        public static final String Content_Type = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Content_Authority + "/" + path_contact;
        public static final String Content_Item_Type = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Content_Authority + "/" + path_contact;
        public static final String tableName = "contact";
        public static final String Column_Contactid = "id";

        public static final String COLUMN_FirstName = "first_name";
        public static final String COLUMN_LastName = "last_name" ;
        public static final String COLUMN_Image="avatar";


        public static Uri BuildUriFromId(int id) {
            return ContentUris.withAppendedId(Content_Uri, id);
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }
}

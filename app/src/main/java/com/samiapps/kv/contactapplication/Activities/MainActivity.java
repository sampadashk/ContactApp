package com.samiapps.kv.contactapplication.Activities;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samiapps.kv.contactapplication.Adapter.ContactListAdapter;
import com.samiapps.kv.contactapplication.Database.ContactContract;
import com.samiapps.kv.contactapplication.Helper.GlobalProvider;
import com.samiapps.kv.contactapplication.Models.Contact;
import com.samiapps.kv.contactapplication.Models.ContactResult;
import com.samiapps.kv.contactapplication.Network.Constants;
import com.samiapps.kv.contactapplication.R;
import com.samiapps.kv.contactapplication.Utility.PaginationScrollListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView contactListRecyclerView;
    ArrayList<Contact> contactList;
    ContactListAdapter contactListAdapter;
    GlobalProvider globalProvider;
    final String tag_per_page="per_page";
    int totalItems=10;
    int page=1;
    int TOTAL_PAGES=1;
    ImageView addImageView;
    
    private boolean isLoading = false;
    private boolean isLastPage = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("oncreatecc","oncreate");
        setContentView(R.layout.activity_main);
        contactListRecyclerView=(RecyclerView)findViewById(R.id.recycler_contact_list);
        addImageView=(ImageView) findViewById(R.id.add);
        contactList=new ArrayList<>();
        globalProvider=GlobalProvider.getGlobalProviderInstance(this);
        contactListAdapter=new ContactListAdapter(this,contactList);
        contactListRecyclerView.setAdapter(contactListAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        contactListRecyclerView.setLayoutManager(linearLayoutManager);

        contactListRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        contactListRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                page += 1;
                loadNextContacts();

                

            }

            @Override
            public int getTotalPageCount() {
                //Todo
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddContactActivity.class);
                startActivity(intent);
            }
        });
        loadContacts();





    }


    private void loadNextContacts() {
        String url= Constants.base_url+"?"+tag_per_page+"="+totalItems+"&page="+page;

        StringRequest commonRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonParser jsonParser = jsonFactory.createParser(response);
                    ContactResult contactResult = (ContactResult) objectMapper.readValue(jsonParser, ContactResult.class);
                    Log.d("checknewsize",contactResult.getData().size()+"");
                    contactList.addAll(contactResult.getData());
                    contactListAdapter.notifyDataSetChanged();
                    Log.d("contactlistsize",contactList.size()+"");


                    Log.d("totalpages",TOTAL_PAGES+"");
                    Log.d("getpage",contactResult.getPage()+"");
                    ContentValues[] bulkToInsert;
                    List<ContentValues> mValueList = new ArrayList<ContentValues>();
                    for(Contact contact:contactResult.getData())
                    {
                        ContentValues mNewValues = new ContentValues();
                        mNewValues.put(ContactContract.ContactC.Column_Contactid,contact.getId() );
                        mNewValues.put(ContactContract.ContactC.COLUMN_FirstName,contact.getFirstName() );
                        mNewValues.put(ContactContract.ContactC.COLUMN_LastName,contact.getLastName() );
                        mNewValues.put(ContactContract.ContactC.COLUMN_Image,contact.getAvatar() );
                        mValueList.add(mNewValues);
                    }
                    bulkToInsert = new ContentValues[mValueList.size()];
                    mValueList.toArray(bulkToInsert);
                    getContentResolver().bulkInsert(ContactContract.ContactC.Content_Uri, bulkToInsert);
                   // TOTAL_PAGES=contactResult.getTotal_pages();
                    if(contactResult.getPage()<TOTAL_PAGES)
                    {
                        isLoading=false;

                    }
                    else
                    {
                        isLastPage=true;
                    }



                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //HANDLE RESPONSE
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String message=globalProvider.getErrorMessage(error);
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
            }
        });
        globalProvider.addRequest(commonRequest);

    }
    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("contactListKey",  contactList);
        super.onSaveInstanceState(outState);
    }
    */

    private void loadContacts() {
        Log.d("loadcontactcalled","here");
        String url= Constants.base_url+"?"+tag_per_page+"="+totalItems;
       // url+="?"+"per_page="+10;
        StringRequest commonRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonParser jsonParser = jsonFactory.createParser(response);
                    ContactResult contactResult = (ContactResult) objectMapper.readValue(jsonParser, ContactResult.class);
                    contactList.addAll(contactResult.getData());
                    Log.d("contactlistsize",contactList.size()+"");


                    contactListAdapter.notifyDataSetChanged();
                    ContentValues[] bulkToInsert;
                    List<ContentValues> mValueList = new ArrayList<ContentValues>();
                    for(Contact contact:contactList)
                    {
                        ContentValues mNewValues = new ContentValues();
                        mNewValues.put(ContactContract.ContactC.Column_Contactid,contact.getId() );
                        mNewValues.put(ContactContract.ContactC.COLUMN_FirstName,contact.getFirstName() );
                        mNewValues.put(ContactContract.ContactC.COLUMN_LastName,contact.getLastName() );
                        mNewValues.put(ContactContract.ContactC.COLUMN_Image,contact.getAvatar() );
                        mValueList.add(mNewValues);
                    }
                    bulkToInsert = new ContentValues[mValueList.size()];
                    mValueList.toArray(bulkToInsert);
                    getContentResolver().bulkInsert(ContactContract.ContactC.Content_Uri, bulkToInsert);
                    TOTAL_PAGES=contactResult.getTotal_pages();
                    if(contactResult.getPage()<contactResult.getTotal_pages())
                    {


                    }
                    else
                    {
                        isLastPage=true;
                    }



                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //HANDLE RESPONSE
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                String message=globalProvider.getErrorMessage(error);
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();


            }
        });
        globalProvider.addRequest(commonRequest);


    }
    public void onStop()
    {
        super.onStop();
        Uri uri = ContactContract.ContactC.Content_Uri;
        String[] projection = new String[] {ContactContract.ContactC.Column_Contactid, ContactContract.ContactC.COLUMN_FirstName,ContactContract.ContactC.COLUMN_LastName,ContactContract.ContactC.COLUMN_Image};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                sortOrder);
        if (cursor != null) {
            cursor.moveToFirst();
            String name;
            for (int i = 0; i < cursor.getCount(); i++){
                name = cursor.getString(cursor
                        .getColumnIndexOrThrow(ContactContract.ContactC.COLUMN_FirstName));
                Log.d("name",name);

                cursor.moveToNext();
            }
            // always close the cursor
            cursor.close();
        }

    }
}

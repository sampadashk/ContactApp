package com.samiapps.kv.contactapplication.Activities;

import android.app.Activity;
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
import com.android.volley.toolbox.JsonObjectRequest;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView contactListRecyclerView;
    ArrayList<Contact> contactList;
    ContactListAdapter contactListAdapter;
    GlobalProvider globalProvider;
    final String tag_per_page = "per_page";
    int totalItems = 10;
    int page = 1;
    int TOTAL_PAGES = 1;
    ImageView addImageView;
    final int ADD_Req_Code = 5;

    private boolean isLoading = false;
    private boolean isLastPage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactListRecyclerView = (RecyclerView) findViewById(R.id.recycler_contact_list);
        addImageView = (ImageView) findViewById(R.id.add);
        contactList = new ArrayList<>();
        globalProvider = GlobalProvider.getGlobalProviderInstance(this);
        contactListAdapter = new ContactListAdapter(this, contactList);
        contactListRecyclerView.setAdapter(contactListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivityForResult(intent, ADD_Req_Code);
            }
        });
        loadContacts();


    }


    private void loadNextContacts() {
        String url = Constants.base_url + "?" + tag_per_page + "=" + totalItems + "&page=" + page;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonParser jsonParser = jsonFactory.createParser(response);
                    ContactResult contactResult = (ContactResult) objectMapper.readValue(jsonParser, ContactResult.class);
                    contactList.addAll(contactResult.getData());
                    contactListAdapter.notifyDataSetChanged();
                    if (contactResult.getPage() < TOTAL_PAGES) {
                        isLoading = false;

                    } else {
                        isLastPage = true;
                    }


                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = globalProvider.getErrorMessage(error);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

            }
        });
        globalProvider.addRequest(stringRequest);


    }

    private void loadContacts() {
        String url = Constants.base_url + "?" + tag_per_page + "=" + totalItems + "&page=" + page;


        StringRequest commonRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonFactory jsonFactory = new JsonFactory();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonParser jsonParser = jsonFactory.createParser(response);
                    ContactResult contactResult = (ContactResult) objectMapper.readValue(jsonParser, ContactResult.class);
                    contactList.addAll(contactResult.getData());
                    contactListAdapter.notifyDataSetChanged();
                    TOTAL_PAGES = contactResult.getTotal_pages();
                    if (contactResult.getPage() < contactResult.getTotal_pages()) {


                    } else {
                        isLastPage = true;
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
                String message = globalProvider.getErrorMessage(error);
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();


            }
        });
        globalProvider.addRequest(commonRequest);


    }

    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == contactListAdapter.reqCode && resultCode == Activity.RESULT_OK) {
            Contact updatedContact = data.getParcelableExtra("updatedContact");
            for (Contact contact : contactList) {
                if (updatedContact.getId().equals(contact.getId())) {
                    contact.setFirstName(updatedContact.getFirstName());
                    contact.setLastName(updatedContact.getLastName());
                    contactListAdapter.notifyDataSetChanged();
                    break;
                }
            }


        } else if (requestCode == ADD_Req_Code && resultCode == Activity.RESULT_OK) {
            Contact addedContact = data.getParcelableExtra("contactAdded");
            contactList.add(addedContact);
            contactListAdapter.notifyDataSetChanged();

        }
    }
}

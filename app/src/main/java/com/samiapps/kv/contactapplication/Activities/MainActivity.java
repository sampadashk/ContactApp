package com.samiapps.kv.contactapplication.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import com.samiapps.kv.contactapplication.Helper.GlobalProvider;
import com.samiapps.kv.contactapplication.Models.Contact;
import com.samiapps.kv.contactapplication.Models.ContactResult;
import com.samiapps.kv.contactapplication.Network.Constants;
import com.samiapps.kv.contactapplication.R;
import com.samiapps.kv.contactapplication.Utility.PaginationScrollListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView contactListRecyclerView;
    List<Contact> contactList;
    ContactListAdapter contactListAdapter;
    GlobalProvider globalProvider;
    final String tag_per_page="per_page";
    int totalItems=5;
    int page=1;
    int TOTAL_PAGES=1;
    
    private boolean isLoading = false;
    private boolean isLastPage = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactListRecyclerView=(RecyclerView)findViewById(R.id.recycler_contact_list);
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
                    contactList.addAll(contactResult.getData());
                    Log.d("contactlistsize",contactList.size()+"");

                    contactListAdapter.notifyDataSetChanged();
                    Log.d("totalpages",TOTAL_PAGES+"");
                    Log.d("getpage",contactResult.getPage()+"");
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


            }
        });
        globalProvider.addRequest(commonRequest);

    }

    private void loadContacts() {
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
            public void onErrorResponse(VolleyError error) {


            }
        });
        globalProvider.addRequest(commonRequest);


    }
}

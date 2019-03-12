package com.samiapps.kv.contactapplication.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
        getContacts();


    }

    private void getContacts() {
        String url= Constants.base_url;
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
        }) {
            @Override
            protected Map<String, String> getParams()  {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("per_page",String.valueOf(10));
                return hashMap;
            }
        };
        globalProvider.addRequest(commonRequest);


    }
}

package com.samiapps.kv.contactapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.request.RequestOptions;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samiapps.kv.contactapplication.Helper.GlobalProvider;
import com.samiapps.kv.contactapplication.Models.Contact;
import com.samiapps.kv.contactapplication.Network.Constants;
import com.samiapps.kv.contactapplication.Network.CustomRequest;
import com.samiapps.kv.contactapplication.R;
import com.samiapps.kv.contactapplication.Utility.GlideApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddContactActivity extends AppCompatActivity {
    EditText firstNameText, lastNameText;
    TextView contactIdText;
    Contact contact;
    TextView save, cancel;
    GlobalProvider globalProvider;
    ImageView photoImageView;
    int id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_layout);
        firstNameText = (EditText) findViewById(R.id.first_name);
        lastNameText = (EditText) findViewById(R.id.last_name);
        save = (TextView) findViewById(R.id.save);
        cancel = (TextView) findViewById(R.id.cancel);
        photoImageView = (ImageView) findViewById(R.id.photo);
        globalProvider = GlobalProvider.getGlobalProviderInstance(AddContactActivity.this);
        GlideApp.with(this).load(R.drawable.photo).apply(RequestOptions.circleCropTransform()).into(photoImageView);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(firstNameText.getText())) {
                    firstNameText.setError(getString(R.string.enter_first_name));
                    return;

                }
                if (TextUtils.isEmpty(lastNameText.getText().toString())) {
                    lastNameText.setError(getString(R.string.enter_last_name));
                    return;


                }
                Map<String, String> map = new HashMap<>();
                map.put("first_name", firstNameText.getText().toString());
                map.put("last_name", lastNameText.getText().toString());
                CustomRequest customRequest = new CustomRequest(Request.Method.POST, Constants.base_url, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = "Contact " + response.getString("first_name") + " " + response.getString("last_name") + " added";
                            id = response.getInt("id");
                            Toast.makeText(AddContactActivity.this, result, Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    contact = new Contact();
                                    contact.setFirstName(firstNameText.getText().toString());
                                    contact.setLastName(lastNameText.getText().toString());
                                    contact.setId(id);
                                    Intent intent = new Intent();

                                    intent.putExtra("contactAdded", contact);


                                    setResult(Activity.RESULT_OK, intent);
                                    AddContactActivity.this.finish();

                                }
                            }, Toast.LENGTH_SHORT + 1000);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = globalProvider.getErrorMessage(error);
                        Toast.makeText(AddContactActivity.this, message, Toast.LENGTH_LONG).show();

                    }
                });
                globalProvider.addRequest(customRequest);

            }
        });
    }


}









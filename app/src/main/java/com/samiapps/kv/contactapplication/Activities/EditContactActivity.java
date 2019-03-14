package com.samiapps.kv.contactapplication.Activities;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.request.RequestOptions;
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

public class EditContactActivity extends AppCompatActivity {
    EditText firstNameText,lastNameText;
    TextView contactIdText;
    Contact contact;
    TextView save,cancel;
    GlobalProvider globalProvider;
    ImageView photoImageView;
    Map<String,String> params=new HashMap();


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_contact_layout);
        firstNameText=(EditText) findViewById(R.id.first_name);
        lastNameText=(EditText)findViewById(R.id.last_name);
        contactIdText=(TextView)findViewById(R.id.contact_id);
        photoImageView=(ImageView) findViewById(R.id.photo);
        save=(TextView) findViewById(R.id.save);
        cancel=(TextView) findViewById(R.id.cancel);
        contact= (Contact) getIntent().getParcelableExtra("contact");
        firstNameText.setText(contact.getFirstName());
        lastNameText.setText(contact.getLastName());
        contactIdText.setText(contact.getId()+"");
        globalProvider=GlobalProvider.getGlobalProviderInstance(this);
        firstNameText.addTextChangedListener(new GenericTextWatcher(firstNameText));
        lastNameText.addTextChangedListener(new GenericTextWatcher(lastNameText));
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
                String url = Constants.base_url + "/" + contact.getId();
                Log.d("curl", url);

                CustomRequest customRequest = new CustomRequest(Request.Method.PATCH, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("checkresponse", response.toString());
                        String result= "Contact "+" Edited Successfully";
                        Toast.makeText(EditContactActivity.this,result,Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                EditContactActivity.this.finish();
                            }
                        }, Toast.LENGTH_SHORT+1000);




                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("checkerror", error.toString());
                        String message=globalProvider.getErrorMessage(error);
                        Toast.makeText(EditContactActivity.this,message,Toast.LENGTH_LONG).show();

                    }
                });
                globalProvider.addRequest(customRequest);


            }
        });





    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.first_name: {
                    params.put("first_name", text);
                    break;
                }


                case R.id.last_name:

                {
                    params.put("last_name", text);
                    break;
                }



            }
        }
    }

}

package com.samiapps.kv.contactapplication.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.samiapps.kv.contactapplication.Models.Contact;
import com.samiapps.kv.contactapplication.R;

public class ContactDetail extends AppCompatActivity {
    Contact contact;
    TextView contactNameText;
    ImageView contactImageView;
    TextView contactIdText;
    ImageView backButton;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail_layout);
        contact= (Contact) getIntent().getParcelableExtra("contact");
        contactNameText=(TextView)findViewById(R.id.contactNameTextView);
        contactImageView=(ImageView)findViewById(R.id.profile_image);
        contactIdText=(TextView) findViewById(R.id.contact_id);
        backButton=(ImageView) findViewById(R.id.backButton);

        contactNameText.setText(contact.getFirstName()+" "+contact.getLastName());
        Glide.with(this).load(contact.getAvatar()).apply(RequestOptions.circleCropTransform()).into(contactImageView);
        contactIdText.setText(contact.getId()+"");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }
}

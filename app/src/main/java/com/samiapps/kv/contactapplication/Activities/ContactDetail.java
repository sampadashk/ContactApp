package com.samiapps.kv.contactapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.samiapps.kv.contactapplication.Models.Contact;
import com.samiapps.kv.contactapplication.R;

import java.util.Map;

public class ContactDetail extends AppCompatActivity {
    Contact contact;
    TextView contactNameText;
    ImageView contactImageView;
    TextView contactIdText;
    ImageView backButton;
    TextView editText;
    final int reqCode = 123;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail_layout);
        contact = (Contact) getIntent().getParcelableExtra("contact");
        contactNameText = (TextView) findViewById(R.id.contactNameTextView);
        contactImageView = (ImageView) findViewById(R.id.profile_image);
        contactIdText = (TextView) findViewById(R.id.contact_id);
        backButton = (ImageView) findViewById(R.id.backButton);
        editText = (TextView) findViewById(R.id.edit);
        contactNameText.setText(contact.getFirstName() + " " + contact.getLastName());
        Glide.with(this).load(contact.getAvatar()).placeholder(R.drawable.photo).apply(RequestOptions.circleCropTransform()).into(contactImageView);
        contactIdText.setText(contact.getId() + "");
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactDetail.this, EditContactActivity.class);
                intent.putExtra("contact", contact);
                startActivityForResult(intent, reqCode);

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("updatedContact", contact);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("updatedContact", contact);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == reqCode && resultCode == Activity.RESULT_OK) {
            String firstName = data.getStringExtra("firstName");
            String lastName = data.getStringExtra("lastName");
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contactNameText.setText(firstName + " " + lastName);

        }
    }
}

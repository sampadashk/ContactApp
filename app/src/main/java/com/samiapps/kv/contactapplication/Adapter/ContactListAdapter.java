package com.samiapps.kv.contactapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.samiapps.kv.contactapplication.Activities.ContactDetail;
import com.samiapps.kv.contactapplication.Models.Contact;
import com.samiapps.kv.contactapplication.R;
import com.samiapps.kv.contactapplication.Utility.GlideApp;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>  {
    Context context;
    List<Contact> contactList;
    public ContactListAdapter(Context context,List<Contact> contactList)
    {
        this.context=context;
        this.contactList=contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.contact_list_item,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       final Contact contact= contactList.get(i);
       viewHolder.contactNameTextView.setText(contact.getFirstName()+" "+contact.getLastName());
        Log.d("checkname",contact.getFirstName());
        GlideApp.with(context).load(contact.getAvatar()).apply(RequestOptions.circleCropTransform()).into(viewHolder.contactImageView);
       // Glide.with(context).load(contact.getAvatar()).apply(RequestOptions.circleCropTransform()).into(viewHolder.contactImageView);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ContactDetail.class);
                intent.putExtra("contact",contact);
                context.startActivity(intent);
            }
        });





    }

    @Override
    public int getItemCount() {
        Log.d("checksize",contactList.size()+"");
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView contactNameTextView;
        ImageView contactImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactImageView=(ImageView)itemView.findViewById(R.id.contactImageView);
            contactNameTextView=(TextView)itemView.findViewById(R.id.contactNameTextView);
        }
    }
}

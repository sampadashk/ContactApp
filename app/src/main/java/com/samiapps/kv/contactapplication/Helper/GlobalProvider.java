package com.samiapps.kv.contactapplication.Helper;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class GlobalProvider {
    private static GlobalProvider globalProviderInstance;
    private Context context;

    private static RequestQueue requestQueue;
    private GlobalProvider(Context context)
    {
        this.context=context;
        if(requestQueue==null)
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());

    }
    public static synchronized GlobalProvider getGlobalProviderInstance(Context context) {
        if(globalProviderInstance==null)
        {
            globalProviderInstance=new GlobalProvider(context);

        }
        return globalProviderInstance;
    }
    public static void addRequest(Request request)
    {
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);

    }
    public  String getErrorMessage(VolleyError error)
    {
        String message = null;
        if (error instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        }  else if (error instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        return message;
    }


}

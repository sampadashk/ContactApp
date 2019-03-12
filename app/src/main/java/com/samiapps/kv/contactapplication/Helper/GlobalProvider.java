package com.samiapps.kv.contactapplication.Helper;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
}

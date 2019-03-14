package com.samiapps.kv.contactapplication.Activities;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

public class ContactApplication extends Application {
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {

                // new activity created; force its orientation to portrait
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d("applicationstate", "started");

            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d("applicationstate", "resumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d("applicationstate", "paused");

            }

            @Override
            public void onActivityStopped(Activity activity) {

                Log.d("applicationstate", "stopped");

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d("applicationstate", "des");

            }


        });
    }
}

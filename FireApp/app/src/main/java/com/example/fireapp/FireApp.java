package com.example.fireapp;

import android.app.Application;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class FireApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * previous version
         *
         Firebase.setAndroidContext(this);
         */

        Firebase.setAndroidContext(this);
        /**
         * newer version
         */
//        if(!FirebaseApp.getApps(this).isEmpty()){
//            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        }

    }
}

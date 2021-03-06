package com.example.blogapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class BlogApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //used for syncing the strings used inside our app
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //used for syncing the images used inside our app.
        Picasso.Builder builder=new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built=builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}

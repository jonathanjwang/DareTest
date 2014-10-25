package com.example.jonawan.daretest;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;

/**
 * Created by jonathanjwang on 10/19/14.
 */
public class Application extends android.app.Application {


    @Override
    public void onCreate()
    {
        super.onCreate();

        // Parse Push Notifications
        ParseObject.registerSubclass(Dare.class);
        Parse.initialize(this, "cdPS8WC9A9L4bJtpKaj3LSqVnyeBDtl5znL4PM8L", "JBYLF9PWmZTNFzKGCViILpIleBchMjHoq9BYoilT");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("DareChannel");


    }

}
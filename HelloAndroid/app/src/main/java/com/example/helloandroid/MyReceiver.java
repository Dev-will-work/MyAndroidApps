package com.example.helloandroid;

import static android.content.Intent.ACTION_TIMEZONE_CHANGED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Locale;
import java.util.TimeZone;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // TODO: an Intent broadcast.
        String action = intent.getAction();
        // Notices about region/locale change and remembers them in activity
        if (action.equals(ACTION_TIMEZONE_CHANGED)) {
            if (MainActivity.oldTZ != null && MainActivity.oldTZ != TimeZone.getDefault()) {
                Toast.makeText(context.getApplicationContext(), "Timezone changed from " + MainActivity.oldTZ.getID() + " to " + TimeZone.getDefault().getID(),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context.getApplicationContext(), "Timezone changed to " + TimeZone.getDefault().getID(),Toast.LENGTH_SHORT).show();
            }
            MainActivity.oldTZ = TimeZone.getDefault();
        } else {
            if (MainActivity.locale != null && MainActivity.locale != Locale.getDefault()) {
                Toast.makeText(context.getApplicationContext(), "Locale changed from " + MainActivity.locale.getLanguage() + " to " + Locale.getDefault().getLanguage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context.getApplicationContext(), "Locale changed to " + Locale.getDefault().getLanguage(),Toast.LENGTH_SHORT).show();
            }
            MainActivity.locale = Locale.getDefault();
        }
    }
}
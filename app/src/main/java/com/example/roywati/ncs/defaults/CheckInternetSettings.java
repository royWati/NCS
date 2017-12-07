package com.example.roywati.ncs.defaults;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by Chris on 6/10/2017.
 */
public class CheckInternetSettings {
Context context;
    public CheckInternetSettings(Activity activity){
        context=activity;
    }

    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        alertDialog.setTitle("Internet setting");
        alertDialog.setMessage("Internet is not enabled. do you want to go to settings menu");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();

    }

    public boolean isNetworkConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);


        if(connectivityManager.getActiveNetworkInfo()!=null){

            return true;
        }else{
            return false;
        }

    }
}

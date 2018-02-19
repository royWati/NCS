package com.example.roywati.ncs.defaults;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

/**
 * Created by root on 2/18/18.
 */

public class NoDataException {

    Context context;

    public NoDataException(Context context){
        this.context=context;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);
        alertDialog.setTitle("Internet setting");
        alertDialog.setMessage("Internet has a problem. Check your data settings");
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
           }
       });
        alertDialog.show();
    }

}

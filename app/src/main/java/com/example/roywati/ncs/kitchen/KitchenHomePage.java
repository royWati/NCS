package com.example.roywati.ncs.kitchen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.CheckInternetSettings;
import com.example.roywati.ncs.defaults.LoginActivity;
import com.example.roywati.ncs.waiter.AppConfig;

public class KitchenHomePage extends AppCompatActivity {
    Button btn;
    GridView gridView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("kitchen homepage");
        setContentView((int) R.layout.kitchen_homepage);
        this.gridView = (GridView) findViewById(R.id.homepage_kitchen_gridview);
        this.btn = (Button) findViewById(R.id.logout_kitchen);
        this.btn.setVisibility(0);
        this.gridView.setAdapter(new HomepageAdapter(this, AppConfig.menuItemId, AppConfig.menuItemName));
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppConfigKitchen.homepageId = ((TextView) view.findViewById(R.id.kitchen_homepage_id)).getText().toString();
                KitchenHomePage.this.startActivity(new Intent(KitchenHomePage.this, KitchenMenuItems.class));
             //   Toast.makeText(KitchenHomePage.this.getApplicationContext(), AppConfigKitchen.homepageId + "  " + AppConfigKitchen.userId, 1).show();
            }
        });
        this.btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(KitchenHomePage.this, LoginActivity.class);
                intent.setFlags(268468224);
                KitchenHomePage.this.startActivity(intent);
            }
        });
    }

    public void onBackPressed(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setMessage((CharSequence) "You will be logged out!!");
        alertDialog.setPositiveButton((CharSequence) "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(KitchenHomePage.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });
        alertDialog.setNegativeButton((CharSequence) "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();

    }
}

package com.example.roywati.ncs.kitchen;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.LoginActivity;
import com.example.roywati.ncs.waiter.AppConfig;

/**
 * Created by Chris on 5/13/2017.
 */
public class KitchenHomePage extends AppCompatActivity {
    Button btn;
    GridView gridView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("kitchen homepage");
        setContentView(R.layout.kitchen_homepage);
        gridView=(GridView)findViewById(R.id.homepage_kitchen_gridview);
        btn=(Button)findViewById(R.id.logout_kitchen);

        btn.setVisibility(View.VISIBLE);

        gridView.setAdapter(new HomepageAdapter(KitchenHomePage.this, AppConfig.menuItemId,AppConfig.menuItemName));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView=(TextView)view.findViewById(R.id.kitchen_homepage_id);
                AppConfigKitchen.homepageId=textView.getText().toString();

                startActivity(new Intent(KitchenHomePage.this,KitchenMenuItems.class));
                Toast.makeText(getApplicationContext(), AppConfigKitchen.homepageId+"  "+AppConfigKitchen.userId,Toast.LENGTH_LONG).show();


            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(KitchenHomePage.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

    }
}

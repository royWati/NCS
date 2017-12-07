package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;

import com.example.roywati.ncs.R;

public class MenuItems extends AppCompatActivity {
    GridView gridView;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_items);
        setTitle(AppConfig.sub_menu_title);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.view_cart);
        gridView=(GridView) findViewById(R.id.grid_view_menu_items);
        gridView.setAdapter(new ListItemsAdapter(MenuItems.this,AppConfig.menuCartItemName,AppConfig.menuCartItemId,AppConfig.menuCartItemPrice,
               AppConfig.menuCartItemDescription,fab));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppConfig.add_on_event==true){
                    startActivity(new Intent(MenuItems.this,Homepage.class));
                }else{
                    startActivity(new Intent(MenuItems.this,ViewCart.class));
                }


                //
            }
        });


    }


}

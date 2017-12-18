package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.PrintActivity;

public class MenuItems extends AppCompatActivity {
    GridView gridView;
    RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.menu_items);
        setTitle(AppConfig.sub_menu_title);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.view_cart);
        this.gridView = (GridView) findViewById(R.id.grid_view_menu_items);
        this.gridView.setAdapter(new ListItemsAdapter(this, AppConfig.menuCartItemName, AppConfig.menuCartItemId, AppConfig.menuCartItemPrice, AppConfig.menuCartItemDescription, fab));
        fab.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AppConfig.add_on_event) {
                    MenuItems.this.startActivity(new Intent(MenuItems.this, Homepage.class));
                } else if (AppConfig.checkout_status == 0) {
                    MenuItems.this.startActivity(new Intent(MenuItems.this, ViewCart.class));
                } else {
                    MenuItems.this.startActivity(new Intent(MenuItems.this, PrintActivity.class));
                }
            }
        });
    }
}

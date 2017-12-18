package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.roywati.ncs.defaults.LoginActivity;

public class Homepage extends AppCompatActivity {
    Button btn;
    GridView gridView;
    int[] images = new int[]{R.drawable.new_order, R.drawable.addon, R.drawable.clear, R.drawable.cutlery};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.gridlayout);
        this.btn = (Button) findViewById(R.id.logout_user);
        this.gridView = (GridView) findViewById(R.id.grid_view);
        this.gridView.setAdapter(new HomeCategoryAdapter(this, AppConfig.menuItemId, AppConfig.menuItemName, this.images));
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppConfig.homepageId = ((TextView) view.findViewById(R.id.menu_category_id)).getText().toString();
                int num = Integer.parseInt(AppConfig.homepageId);
                if (num == 1) {
                    AppConfig.clear_a_table_key = false;
                    AppConfig.add_on_event = false;
                    Homepage.this.startActivity(new Intent(Homepage.this, MenuCategoryItems.class));
                } else if (num == 2) {
                    Homepage.this.startActivity(new Intent(Homepage.this, AddOns.class));
                } else if (num == 3) {
                    Homepage.this.startActivity(new Intent(Homepage.this, ClearTable.class));
                } else if (num == 4) {
                    Homepage.this.startActivity(new Intent(Homepage.this, ViewOrders.class));
                }
                Toast.makeText(Homepage.this.getApplicationContext(), AppConfig.homepageId + "  " + AppConfig.userId, 1).show();
            }
        });
        this.btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, LoginActivity.class);
                intent.setFlags(268468224);
                Homepage.this.startActivity(intent);
            }
        });
    }
}

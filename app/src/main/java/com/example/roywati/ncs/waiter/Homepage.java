package com.example.roywati.ncs.waiter;

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

/**
 * Created by Chris on 5/8/2017.
 */
public class Homepage extends AppCompatActivity {
Button btn;
    GridView gridView;
    int[] images={R.drawable.new_order,R.drawable.addon,R.drawable.clear,R.drawable.cutlery};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlayout);


        btn=(Button)findViewById(R.id.logout_user);
        gridView=(GridView)findViewById(R.id.grid_view);

        gridView.setAdapter(new HomeCategoryAdapter(Homepage.this, AppConfig.menuItemId,AppConfig.menuItemName,images));

       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               TextView textView=(TextView)view.findViewById(R.id.menu_category_id);
               AppConfig.homepageId=textView.getText().toString();

                int num=Integer.parseInt(AppConfig.homepageId);
               if(num==1){
                   AppConfig.clear_a_table_key=false;
                   AppConfig.add_on_event=false;
                   startActivity(new Intent(Homepage.this,MenuCategoryItems.class));
               }else if(num==2){
                   startActivity(new Intent(Homepage.this,AddOns.class));
               }else if(num==3){
                   startActivity(new Intent(Homepage.this,ClearTable.class));
               }else if(num==4){
                   startActivity(new Intent(Homepage.this,ViewOrders.class));
               }

               Toast.makeText(getApplicationContext(), AppConfig.homepageId+"  "+AppConfig.userId,Toast.LENGTH_LONG).show();


           }
       });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Homepage.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}

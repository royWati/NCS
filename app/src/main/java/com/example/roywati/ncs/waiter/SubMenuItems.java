package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 5/10/2017.
 */
public class SubMenuItems  extends AppCompatActivity {

    GridView gridView;
Button btn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlayout);

        btn=(Button)findViewById(R.id.logout_user);
        gridView=(GridView)findViewById(R.id.grid_view);
        gridView.setAdapter(new SubMenuItemAdapter(SubMenuItems.this,AppConfig.menuSubItemName,AppConfig.menuSubItemId));
        btn.setVisibility(View.GONE);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txtName=(TextView)view.findViewById(R.id.subCategoryMenuId);
                AppConfig.menuSubItem_id=txtName.getText().toString();
                TextView txtTitle=(TextView)view.findViewById(R.id.subCategoryMenuName);
                AppConfig.sub_menu_title=txtTitle.getText().toString();

                new getMenuItem().execute();
            }
        });
    }

    public class getMenuItem extends AsyncTask<String,String,String> {

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";




        JSONArray MenuItem;
     //   JSONArray MenuCategoryArray;



        protected void onPreExecute(){
            super.onPreExecute();
         //   showProgressBar(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
            jsonObjectData.add(new BasicNameValuePair("sub_menu_item_id",AppConfig.menuSubItem_id));
            jsonObjectData.add(new BasicNameValuePair("branchId",AppConfig.branchId));
            Log.d("subMenuItem",AppConfig.menuSubItem_id);


            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.get_menu_items,
                    "GET",jsonObjectData);

            Log.d("data",jsonObjectResponse.toString());

            try{


                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);

                MenuItem=jsonObjectResponse.getJSONArray("menu_items");

                Log.d("data",AppConfig.orderId);

                AppConfig.menuCartItemId=new String[MenuItem.length()];
                AppConfig.menuCartItemName=new String[MenuItem.length()];
                AppConfig.menuCartItemPrice=new String[MenuItem.length()];
                AppConfig.menuCartItemDescription=new String[MenuItem.length()];


                for(int i=0;i<MenuItem.length();i++){

                    JSONObject jsonObject=MenuItem.getJSONObject(i);

                    Log.d("menu sub category",jsonObject.toString());


                    AppConfig.menuCartItemId[i]=jsonObject.getString("menu_item_id");
                    AppConfig.menuCartItemName[i]=jsonObject.getString("menu_item_name");
                    AppConfig.menuCartItemPrice[i]=jsonObject.getString("price");
                    AppConfig.menuCartItemDescription[i]=jsonObject.getString("item_description");


                    Log.d("subItemid", AppConfig.menuCartItemId[i]);
                    Log.d("subItemname", AppConfig.menuCartItemName[i]);
                    Log.d("subItemPrice", AppConfig.menuCartItemPrice[i]);
                    Log.d("subItemDescr", AppConfig.menuCartItemDescription[i]);



                }
                if(success==2){
                    successState=2;
                }


            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            if(successState==2){

                startActivity(new Intent(SubMenuItems.this,MenuItems.class));

            //    Toast.makeText(getApplicationContext(), AppConfig.menuSubItem_id,Toast.LENGTH_LONG).show();

           //     Toast.makeText(getApplicationContext(),serverMessage+ AppConfig.orderId,Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
      //      showProgressBar(false);
        }
    }

}
package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
 * Created by Chris on 5/8/2017.
 */
public class MenuSubCategory extends AppCompatActivity {
    LinearLayout linearLayout;
    ProgressBar prog;
    GridView gridView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlayout_submenu_category);

        this.setTitle(AppConfig.menuCatName);
        linearLayout=(LinearLayout)findViewById(R.id.linearItems);
        prog=(ProgressBar)findViewById(R.id.progressBarMenu);
        gridView=(GridView)findViewById(R.id.grid_view_sub_cat);

        new getMenuCategory().execute();


    }

    public void showProgressBar(boolean state) {
        if (state == true) {
            linearLayout.setVisibility(View.GONE);
            prog.setVisibility(View.VISIBLE);

        } else {
            linearLayout.setVisibility(View.VISIBLE);
            prog.setVisibility(View.GONE);
        }

    }

    public class getMenuCategory extends AsyncTask<String,String,String> {

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";




      //  JSONArray oderId;
        JSONArray menuSubCategory;



        protected void onPreExecute(){
            super.onPreExecute();
            showProgressBar(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
            jsonObjectData.add(new BasicNameValuePair("menu_category_id",AppConfig.menuCatId));


            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.get_menu_subcategory_url,
                    "GET",jsonObjectData);

            Log.d("data",jsonObjectResponse.toString());

            try{


                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);

                menuSubCategory=jsonObjectResponse.getJSONArray("Menu_category_submenu");


                Log.d("data",AppConfig.orderId);


                AppConfig.menuSubCategoryId=new String[menuSubCategory.length()];
                AppConfig.menuSubCategoryName=new String[menuSubCategory.length()];


                for(int i=0;i<menuSubCategory.length();i++){
                    // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                    JSONObject jsonObject=menuSubCategory.getJSONObject(i);

                    Log.d("menu sub category",jsonObject.toString());


                    AppConfig.menuSubCategoryId[i]=jsonObject.getString("sub_menu_id");
                    AppConfig.menuSubCategoryName[i]=jsonObject.getString("sub_menu_name");

                    Log.d("sub menu name", AppConfig.menuSubCategoryId[i]);
                    Log.d("sub menu name", AppConfig.menuSubCategoryName[i]);
                    //   Log.d("menu name", AppConfig.menuCategoryId[1]);


                }
                if(success==1){
                    successState=1;
                }



            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            if(successState==1){
                gridView.setAdapter(new MenuSubCategoryAdapter(MenuSubCategory.this,AppConfig.menuSubCategoryName,AppConfig.menuSubCategoryId));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView textView=(TextView)view.findViewById(R.id.subCategoryMenuId);
                        AppConfig.menuSubCatId=textView.getText().toString();
                        TextView title=(TextView)view.findViewById(R.id.subCategoryMenuName);

                        AppConfig.sub_menu_title=title.getText().toString();

                       new getSubMenuItem().execute();
                        Toast.makeText(getApplicationContext(), AppConfig.menuSubCatId,Toast.LENGTH_LONG).show();


                    }
                });

                Toast.makeText(getApplicationContext(),serverMessage+ AppConfig.orderId,Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
            showProgressBar(false);
        }
    }


    public class getSubMenuItem extends AsyncTask<String,String,String>{

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";




        JSONArray subMenuItem;
        JSONArray menuItem;

        protected void onPreExecute(){
            super.onPreExecute();
           showProgressBar(true);
        }


        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
            jsonObjectData.add(new BasicNameValuePair("sub_menu_id",AppConfig.menuSubCatId));
            jsonObjectData.add(new BasicNameValuePair("branchId",AppConfig.branchId));

            Log.d("menuSubCatId",AppConfig.menuSubCatId);

            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.sub_menu_items,
                    "GET",jsonObjectData);

            Log.d("data sub category",jsonObjectResponse.toString());

            try{
                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);

                if(success==1){
                    subMenuItem=jsonObjectResponse.getJSONArray("sub_menu_items");


                    Log.d("data",AppConfig.orderId);
                    AppConfig.menuSubItemId=new String[subMenuItem.length()];
                    AppConfig.menuSubItemName=new String[subMenuItem.length()];

                    for(int i=0;i<subMenuItem.length();i++){
                        // JSONObject jsonObject=MenuCategoryArray.getJSONObject(i);
                        JSONObject jsonObject=subMenuItem.getJSONObject(i);

                        Log.d("menu sub category",jsonObject.toString());


                        AppConfig.menuSubItemId[i]=jsonObject.getString("sub_item_id");
                        AppConfig.menuSubItemName[i]=jsonObject.getString("sub_item_name");

                        Log.d("sub item id", AppConfig.menuSubItemId[i]);
                        Log.d("sub item name", AppConfig.menuSubItemName[i]);
                        //   Log.d("menu name", AppConfig.menuCategoryId[1]);


                    }



                    successState=1;
                }else if(success==2){

                    menuItem=jsonObjectResponse.getJSONArray("menu_items");


                    Log.d("data",AppConfig.orderId);
                    AppConfig.menuCartItemId=new String[menuItem.length()];
                    AppConfig.menuCartItemName=new String[menuItem.length()];
                    AppConfig.menuCartItemPrice=new String[menuItem.length()];
                    AppConfig.menuCartItemDescription=new String[menuItem.length()];

                    for(int i=0;i<menuItem.length();i++){

                        JSONObject jsonObject=menuItem.getJSONObject(i);

                        Log.d("menu sub category",jsonObject.toString());


                        AppConfig.menuCartItemId[i]=jsonObject.getString("menu_item_id");
                        AppConfig.menuCartItemName[i]=jsonObject.getString("menu_item_name");
                        AppConfig.menuCartItemPrice[i]=jsonObject.getString("price");
                        AppConfig.menuCartItemDescription[i]=jsonObject.getString("item_description");


                        Log.d("sub item id", AppConfig.menuCartItemId[i]);
                        Log.d("sub item name", AppConfig.menuCartItemName[i]);
                        Log.d("sub item id", AppConfig.menuCartItemPrice[i]);
                        Log.d("sub item name", AppConfig.menuCartItemDescription[i]);



                    }
                    successState=2;
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            Log.d("successState",String.valueOf(successState));
            if(successState==1){

                startActivity(new Intent(MenuSubCategory.this,SubMenuItems.class));
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();

            }else if(successState==2){
                startActivity(new Intent(MenuSubCategory.this,MenuItems.class));
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(),serverMessage,Toast.LENGTH_LONG).show();
            }
           showProgressBar(false);
        }

    }
}

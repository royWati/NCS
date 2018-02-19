package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.defaults.NoDataException;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class MenuSubCategory extends AppCompatActivity {
    GridView gridView;
    LinearLayout linearLayout;
    ProgressBar prog;

    public class getMenuCategory extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        JSONArray menuSubCategory;
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
            MenuSubCategory.this.showProgressBar(true);
        }

        protected String doInBackground(String... strings) {

            try {
                JSONParser jsonParser = new JSONParser();
                List<NameValuePair> jsonObjectData = new ArrayList();
                jsonObjectData.add(new BasicNameValuePair("menu_category_id", AppConfig.menuCatId));
                JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.get_menu_subcategory_url, HttpGet.METHOD_NAME, jsonObjectData);
                Log.d("data", jsonObjectResponse.toString());

                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                this.menuSubCategory = jsonObjectResponse.getJSONArray("Menu_category_submenu");
                Log.d("data", AppConfig.orderId);
                AppConfig.menuSubCategoryId = new String[this.menuSubCategory.length()];
                AppConfig.menuSubCategoryName = new String[this.menuSubCategory.length()];
                for (int i = 0; i < this.menuSubCategory.length(); i++) {
                    JSONObject jsonObject = this.menuSubCategory.getJSONObject(i);
                    Log.d("menu sub category", jsonObject.toString());
                    AppConfig.menuSubCategoryId[i] = jsonObject.getString("sub_menu_id");
                    AppConfig.menuSubCategoryName[i] = jsonObject.getString("sub_menu_name");
                    Log.d("sub menu name", AppConfig.menuSubCategoryId[i]);
                    Log.d("sub menu name", AppConfig.menuSubCategoryName[i]);
                }
                if (success == 1) {
                    this.successState = 1;
                }
            } catch (Exception e) {
                successState=500;
            }
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (this.successState == 1) {
                MenuSubCategory.this.gridView.setAdapter(new MenuSubCategoryAdapter(MenuSubCategory.this, AppConfig.menuSubCategoryName, AppConfig.menuSubCategoryId));
                MenuSubCategory.this.gridView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AppConfig.menuSubCatId = ((TextView) view.findViewById(R.id.subCategoryMenuId)).getText().toString();
                        AppConfig.sub_menu_title = ((TextView) view.findViewById(R.id.subCategoryMenuName)).getText().toString();
                        new getSubMenuItem().execute(new String[0]);
                    //    Toast.makeText(MenuSubCategory.this.getApplicationContext(), AppConfig.menuSubCatId, 1).show();
                    }
                });
              //  Toast.makeText(MenuSubCategory.this.getApplicationContext(), this.serverMessage + AppConfig.orderId, 0).show();
            }else if(successState==500){
                Toast.makeText(getApplicationContext(), "Network Error!!", 1).show();
            } else {
                Toast.makeText(MenuSubCategory.this.getApplicationContext(), this.serverMessage, 1).show();
            }
            MenuSubCategory.this.showProgressBar(false);
        }
    }

    public class getSubMenuItem extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        JSONArray menuItem;
        String serverMessage = "request not sent";
        JSONArray subMenuItem;
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
            MenuSubCategory.this.showProgressBar(true);
        }

        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> jsonObjectData = new ArrayList();
            jsonObjectData.add(new BasicNameValuePair("sub_menu_id", AppConfig.menuSubCatId));
            jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));
            Log.d("menuSubCatId", AppConfig.menuSubCatId);
            JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.sub_menu_items, HttpGet.METHOD_NAME, jsonObjectData);
            Log.d("data sub category", jsonObjectResponse.toString());
            try {
                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                int i;
                JSONObject jsonObject;
                if (success == 1) {
                    this.subMenuItem = jsonObjectResponse.getJSONArray("sub_menu_items");
                    Log.d("data", AppConfig.orderId);
                    AppConfig.menuSubItemId = new String[this.subMenuItem.length()];
                    AppConfig.menuSubItemName = new String[this.subMenuItem.length()];
                    for (i = 0; i < this.subMenuItem.length(); i++) {
                        jsonObject = this.subMenuItem.getJSONObject(i);
                        Log.d("menu sub category", jsonObject.toString());
                        AppConfig.menuSubItemId[i] = jsonObject.getString("sub_item_id");
                        AppConfig.menuSubItemName[i] = jsonObject.getString("sub_item_name");
                        Log.d("sub item id", AppConfig.menuSubItemId[i]);
                        Log.d("sub item name", AppConfig.menuSubItemName[i]);
                    }
                    this.successState = 1;
                } else if (success == 2) {
                    this.menuItem = jsonObjectResponse.getJSONArray("menu_items");
                    Log.d("data", AppConfig.orderId);
                    AppConfig.menuCartItemId = new String[this.menuItem.length()];
                    AppConfig.menuCartItemName = new String[this.menuItem.length()];
                    AppConfig.menuCartItemPrice = new String[this.menuItem.length()];
                    AppConfig.menuCartItemDescription = new String[this.menuItem.length()];
                    for (i = 0; i < this.menuItem.length(); i++) {
                        jsonObject = this.menuItem.getJSONObject(i);
                        Log.d("menu sub category", jsonObject.toString());
                        AppConfig.menuCartItemId[i] = jsonObject.getString("menu_item_id");
                        AppConfig.menuCartItemName[i] = jsonObject.getString("menu_item_name");
                        AppConfig.menuCartItemPrice[i] = jsonObject.getString("price");
                        AppConfig.menuCartItemDescription[i] = jsonObject.getString("item_description");
                        Log.d("sub item id", AppConfig.menuCartItemId[i]);
                        Log.d("sub item name", AppConfig.menuCartItemName[i]);
                        Log.d("sub item id", AppConfig.menuCartItemPrice[i]);
                        Log.d("sub item name", AppConfig.menuCartItemDescription[i]);
                    }
                    this.successState = 2;
                }
            } catch (Exception e) {
                successState=500;
            }
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("successState", String.valueOf(this.successState));
            if (this.successState == 1) {
                MenuSubCategory.this.startActivity(new Intent(MenuSubCategory.this, SubMenuItems.class));
               // Toast.makeText(MenuSubCategory.this.getApplicationContext(), this.serverMessage, 1).show();
            } else if (this.successState == 2) {
                MenuSubCategory.this.startActivity(new Intent(MenuSubCategory.this, MenuItems.class));
              //  Toast.makeText(MenuSubCategory.this.getApplicationContext(), this.serverMessage, 1).show();
            }else if(successState==500){
                Toast.makeText(getApplicationContext(), "Network Error!!", 1).show();
            } else {
                Toast.makeText(MenuSubCategory.this.getApplicationContext(), this.serverMessage, 1).show();
            }
            MenuSubCategory.this.showProgressBar(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.gridlayout_submenu_category);
        setTitle(AppConfig.menuCatName);
        this.linearLayout = (LinearLayout) findViewById(R.id.linearItems);
        this.prog = (ProgressBar) findViewById(R.id.progressBarMenu);
        this.gridView = (GridView) findViewById(R.id.grid_view_sub_cat);
        new getMenuCategory().execute(new String[0]);
    }

    public void showProgressBar(boolean state) {
        if (state) {
            this.linearLayout.setVisibility(8);
            this.prog.setVisibility(0);
            return;
        }
        this.linearLayout.setVisibility(0);
        this.prog.setVisibility(8);
    }
}

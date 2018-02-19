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

public class MenuCategoryItems extends AppCompatActivity {
    GridView gridView;
    LinearLayout linearLayout;
    ProgressBar prog;

    public class getMenuCategory extends AsyncTask<String, String, String> {
        JSONArray MenuCategoryArray;
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        JSONArray oderId;
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
            MenuCategoryItems.this.showProgressBar(true);
        }

        protected String doInBackground(String... strings) {

            try {
                JSONParser jsonParser = new JSONParser();
                List<NameValuePair> jsonObjectData = new ArrayList();
                jsonObjectData.add(new BasicNameValuePair("userhomepage_id", AppConfig.homepageId));
                jsonObjectData.add(new BasicNameValuePair("userId", AppConfig.userId));
                jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));
                JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.get_menu_page, HttpGet.METHOD_NAME, jsonObjectData);
                Log.d("data", jsonObjectResponse.toString());

                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                this.MenuCategoryArray = jsonObjectResponse.getJSONArray("Menu_category");
                this.oderId = jsonObjectResponse.getJSONArray("oderId");
                AppConfig.orderId = this.oderId.getJSONObject(0).getString("orderId");
                Log.d("data", AppConfig.orderId);
                AppConfig.menuCategoryId = new String[this.MenuCategoryArray.length()];
                AppConfig.menuCategoryName = new String[this.MenuCategoryArray.length()];
                for (int i = 0; i < this.MenuCategoryArray.length(); i++) {
                    JSONObject jsonObject = this.MenuCategoryArray.getJSONObject(i);
                    Log.d("menu category", jsonObject.toString());
                    AppConfig.menuCategoryId[i] = jsonObject.getString("menu_category_id");
                    AppConfig.menuCategoryName[i] = jsonObject.getString("menu_category_name");
                    Log.d("menu name", AppConfig.menuCategoryId[i]);
                    Log.d("menu name", AppConfig.menuCategoryName[i]);
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
                MenuCategoryItems.this.gridView.setAdapter(new MenuCategoryAdapter(MenuCategoryItems.this, AppConfig.menuCategoryName, AppConfig.menuCategoryId));
                MenuCategoryItems.this.gridView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView txtName = (TextView) view.findViewById(R.id.menuCategoryName);
                        AppConfig.menuCatId = ((TextView) view.findViewById(R.id.menuCategoryId)).getText().toString();
                        AppConfig.menuCatName = txtName.getText().toString();
                        MenuCategoryItems.this.startActivity(new Intent(MenuCategoryItems.this, MenuSubCategory.class));
                     //   Toast.makeText(MenuCategoryItems.this.getApplicationContext(), AppConfig.menuCatId, 1).show();
                    }
                });
            }else if(successState==500){
                Toast.makeText(getApplicationContext(), "Network Error!!", 1).show();
            } else {
                Toast.makeText(MenuCategoryItems.this.getApplicationContext(), this.serverMessage, 1).show();
            }
            MenuCategoryItems.this.showProgressBar(false);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.gridlayout_menu_category);
        this.linearLayout = (LinearLayout) findViewById(R.id.linearItems);
        this.prog = (ProgressBar) findViewById(R.id.progressBarMenu);
        this.gridView = (GridView) findViewById(R.id.grid_view_cat);
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

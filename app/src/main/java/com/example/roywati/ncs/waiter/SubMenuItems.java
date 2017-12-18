package com.example.roywati.ncs.waiter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class SubMenuItems extends AppCompatActivity {
    Button btn;
    GridView gridView;

    public class getMenuItem extends AsyncTask<String, String, String> {
        JSONArray MenuItem;
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "success";
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            List<NameValuePair> jsonObjectData = new ArrayList();
            jsonObjectData.add(new BasicNameValuePair("sub_menu_item_id", AppConfig.menuSubItem_id));
            jsonObjectData.add(new BasicNameValuePair("branchId", AppConfig.branchId));
            Log.d("subMenuItem", AppConfig.menuSubItem_id);
            JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.get_menu_items, HttpGet.METHOD_NAME, jsonObjectData);
            Log.d("data", jsonObjectResponse.toString());
            try {
                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                this.MenuItem = jsonObjectResponse.getJSONArray("menu_items");
                Log.d("data", AppConfig.orderId);
                AppConfig.menuCartItemId = new String[this.MenuItem.length()];
                AppConfig.menuCartItemName = new String[this.MenuItem.length()];
                AppConfig.menuCartItemPrice = new String[this.MenuItem.length()];
                AppConfig.menuCartItemDescription = new String[this.MenuItem.length()];
                for (int i = 0; i < this.MenuItem.length(); i++) {
                    JSONObject jsonObject = this.MenuItem.getJSONObject(i);
                    Log.d("menu sub category", jsonObject.toString());
                    AppConfig.menuCartItemId[i] = jsonObject.getString("menu_item_id");
                    AppConfig.menuCartItemName[i] = jsonObject.getString("menu_item_name");
                    AppConfig.menuCartItemPrice[i] = jsonObject.getString("price");
                    AppConfig.menuCartItemDescription[i] = jsonObject.getString("item_description");
                    Log.d("subItemid", AppConfig.menuCartItemId[i]);
                    Log.d("subItemname", AppConfig.menuCartItemName[i]);
                    Log.d("subItemPrice", AppConfig.menuCartItemPrice[i]);
                    Log.d("subItemDescr", AppConfig.menuCartItemDescription[i]);
                }
                if (success == 2) {
                    this.successState = 2;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (this.successState == 2) {
                SubMenuItems.this.startActivity(new Intent(SubMenuItems.this, MenuItems.class));
            } else {
                Toast.makeText(SubMenuItems.this.getApplicationContext(), this.serverMessage, 1).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.gridlayout);
        this.btn = (Button) findViewById(R.id.logout_user);
        this.gridView = (GridView) findViewById(R.id.grid_view);
        this.gridView.setAdapter(new SubMenuItemAdapter(this, AppConfig.menuSubItemName, AppConfig.menuSubItemId));
        this.btn.setVisibility(8);
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppConfig.menuSubItem_id = ((TextView) view.findViewById(R.id.subCategoryMenuId)).getText().toString();
                AppConfig.sub_menu_title = ((TextView) view.findViewById(R.id.subCategoryMenuName)).getText().toString();
                new getMenuItem().execute(new String[0]);
            }
        });
    }
}

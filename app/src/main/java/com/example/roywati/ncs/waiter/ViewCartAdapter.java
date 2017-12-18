package com.example.roywati.ncs.waiter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.roywati.ncs.R;
import com.example.roywati.ncs.defaults.JSONParser;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class ViewCartAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    String[] itemDescr;
    String[] itemId;
    String[] itemName;
    String[] itemPrice;

    public class deleteRow extends AsyncTask<String, String, String> {
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
            jsonObjectData.add(new BasicNameValuePair("order_item_id", AppConfig.delete_cart_item));
            Log.d("menuSubCatId", AppConfig.orderId);
            JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.delete_cart, HttpGet.METHOD_NAME, jsonObjectData);
            Log.d("data sub category", jsonObjectResponse.toString());
            try {
                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
                if (success == 1) {
                    this.successState = 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("successState", String.valueOf(this.successState));
            if (this.successState == 1) {
                Intent intent = new Intent(ViewCartAdapter.this.context.getApplicationContext(), ViewCart.class);
                intent.setFlags(32768);
                ViewCartAdapter.this.context.startActivity(intent);
                Toast.makeText(ViewCartAdapter.this.context, this.serverMessage, 1).show();
                return;
            }
            Toast.makeText(ViewCartAdapter.this.context, this.serverMessage, 1).show();
        }
    }

    public ViewCartAdapter(Activity activity, String[] itemName, String[] itemId, String[] itemPrice) {
        this.itemId = itemId;
        this.itemPrice = itemPrice;
        this.itemName = itemName;
        this.context = activity;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return this.itemId.length;
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = this.inflater.inflate(R.layout.custom_view_cart, null);
        TextView name = (TextView) view.findViewById(R.id.menu_cart_item_name_view_cart);
        TextView price = (TextView) view.findViewById(R.id.item_price_view_cart);
        LinearLayout viewColor = (LinearLayout) view.findViewById(R.id.laycolor);
        ((TextView) view.findViewById(R.id.menuitemcart_id_view_cart)).setText(this.itemId[i]);
        name.setText(this.itemName[i]);
        price.setText(this.itemPrice[i]);
        if (i % 2 == 1) {
            viewColor.setBackgroundColor(Color.parseColor("#ffffff"));
            price.setTextColor(Color.parseColor("#542400"));
        }
        ((ImageView) view.findViewById(R.id.remove_item_from_cart)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AppConfig.delete_cart_item = ViewCartAdapter.this.itemId[i];
                new deleteRow().execute(new String[0]);
            }
        });
        return view;
    }
}

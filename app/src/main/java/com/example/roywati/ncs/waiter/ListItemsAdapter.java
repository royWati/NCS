package com.example.roywati.ncs.waiter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.json.JSONObject;

public class ListItemsAdapter extends BaseAdapter {
    Context context;
    FloatingActionButton fab;
    LayoutInflater inflater;
    String[] itemDescr;
    String[] itemId;
    String[] itemName;
    String[] itemPrice;

    public class AddItemToCart extends AsyncTask<String, String, String> {
        String TAG_MESSAGE = "message";
        String TAG_SUCCESS = "error";
        String serverMessage = "request not sent";
        int successState = 0;

        protected void onPreExecute() {
            super.onPreExecute();
            ListItemsAdapter.this.rotateFabButton(true);
        }

        protected String doInBackground(String... strings) {

            try {
                JSONParser jsonParser = new JSONParser();
                List<NameValuePair> jsonObjectData = new ArrayList();
                jsonObjectData.add(new BasicNameValuePair("menu_item_id", AppConfig.menuCatId));
                jsonObjectData.add(new BasicNameValuePair("orderId", AppConfig.orderId));
                JSONObject jsonObjectResponse = jsonParser.makeHttpRequest(AppConfig.protocal + AppConfig.hostname + AppConfig.add_to_cart, HttpGet.METHOD_NAME, jsonObjectData);
                Log.d("orderId and menuCatId", AppConfig.orderId + " " + AppConfig.menuCatId);

                int success = jsonObjectResponse.getInt(this.TAG_SUCCESS);
                this.serverMessage = jsonObjectResponse.getString(this.TAG_MESSAGE);
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
            Log.d("successState", String.valueOf(this.successState));
            if (this.successState == 1) {
               // Toast.makeText(ListItemsAdapter.this.context, this.serverMessage, Toast.LENGTH_SHORT).show();
            }else if(successState==500){
                Toast.makeText(context, "Network Error!!", 1).show();
            } else {
                Toast.makeText(ListItemsAdapter.this.context, this.serverMessage, Toast.LENGTH_SHORT).show();
            }
            ListItemsAdapter.this.rotateFabButton(false);
        }
    }

    public class Holder {
        ImageView imageView;
    }

    public ListItemsAdapter(Activity activity, String[] itemName, String[] itemId, String[] itemPrice, String[] itemDescr, FloatingActionButton fab) {
        this.fab = fab;
        this.itemId = itemId;
        this.itemPrice = itemPrice;
        this.itemName = itemName;
        this.itemDescr = itemDescr;
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
        view = this.inflater.inflate(R.layout.custom_menu_items, null);
        TextView id = (TextView) view.findViewById(R.id.menuitemcart_id);
        TextView name = (TextView) view.findViewById(R.id.menu_cart_item_name);
        TextView price = (TextView) view.findViewById(R.id.item_price);
        TextView descr = (TextView) view.findViewById(R.id.item_Descr_info);
        final ImageView imageView = (ImageView) view.findViewById(R.id.add_menu_item_cart);
        ImageView img = (ImageView) view.findViewById(R.id.more);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.lay);
        if (i % 2 == 0) {
            layout.setBackgroundColor(Color.parseColor("#c69577"));
            price.setTextColor(Color.parseColor("#542400"));
        }
        img.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Builder builder = new Builder(ListItemsAdapter.this.context);
                builder.setCancelable(true);
                builder.setTitle((CharSequence) "Item description");
                builder.setMessage(ListItemsAdapter.this.itemDescr[i]);
                builder.show();
            }
        });
        id.setText(this.itemId[i]);
        name.setText(this.itemName[i]);
        price.setText(this.itemPrice[i]);
        descr.setText(this.itemDescr[i]);
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                imageView.startAnimation(AnimationUtils.loadAnimation(ListItemsAdapter.this.context, R.anim.blink));
                AppConfig.menuCatId = ListItemsAdapter.this.itemId[i];
           //     Toast.makeText(ListItemsAdapter.this.context, AppConfig.menuCatId, 0).show();
                new AddItemToCart().execute(new String[0]);
            }
        });
        return view;
    }

    public void rotateFabButton(boolean state) {
        if (state) {
            Holder holder = new Holder();
            this.fab.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.rotate_clock));
        }
    }
}

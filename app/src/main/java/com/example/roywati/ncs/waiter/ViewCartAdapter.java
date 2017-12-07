package com.example.roywati.ncs.waiter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roywati.ncs.defaults.JSONParser;
import com.example.roywati.ncs.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 5/12/2017.
 */
public class ViewCartAdapter extends BaseAdapter {


    String[] itemName,itemId,itemPrice,itemDescr;
    Context context;
    LayoutInflater inflater;
    public ViewCartAdapter(Activity activity, String[]itemName, String[]itemId, String[]itemPrice){

        this.itemId=itemId;
        this.itemPrice=itemPrice;
        this.itemName=itemName;

        context=activity;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return itemId.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view=inflater.inflate(R.layout.custom_view_cart,null);
        TextView id=(TextView)view.findViewById(R.id.menuitemcart_id_view_cart);
        TextView name=(TextView)view.findViewById(R.id.menu_cart_item_name_view_cart);
        TextView price=(TextView)view.findViewById(R.id.item_price_view_cart);
        LinearLayout viewColor=(LinearLayout)view.findViewById(R.id.laycolor);
        id.setText(itemId[i]);
        name.setText(itemName[i]);
        price.setText(itemPrice[i]);

        if(i%2==1)
        {
            viewColor.setBackgroundColor(Color.parseColor("#ffffff"));


            price.setTextColor(Color.parseColor("#542400"));
        }

        final ImageView imageView=(ImageView)view.findViewById(R.id.remove_item_from_cart);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfig.delete_cart_item=itemId[i];
               new deleteRow().execute();
            }
        });

        return view;
    }

    public class deleteRow extends AsyncTask<String,String,String>{
        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";

        protected void onPreExecute(){
            super.onPreExecute();
            //     showProgressBar(true);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();

            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
            jsonObjectData.add(new BasicNameValuePair("order_item_id",AppConfig.delete_cart_item));

            Log.d("menuSubCatId",AppConfig.orderId);
            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.delete_cart,
                    "GET",jsonObjectData);

            Log.d("data sub category",jsonObjectResponse.toString());

            try{
                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);

                if(success==1){
                    successState=1;
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            Log.d("successState",String.valueOf(successState));
            if(successState==1){

          //    new GetViewCart((Activity) context,ViewCart.listView,ViewCart.txtAmount,ViewCart.order,ViewCart.holder,ViewCart.prog_v);

                Intent  intent = new Intent(context.getApplicationContext(),ViewCart.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);



                //  startActivity(new Intent(MenuSubCategory.this,SubMenuItems.class));
                Toast.makeText(context,serverMessage,Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(context,serverMessage,Toast.LENGTH_LONG).show();
            }
            //   showProgressBar(false);
        }

    }


}


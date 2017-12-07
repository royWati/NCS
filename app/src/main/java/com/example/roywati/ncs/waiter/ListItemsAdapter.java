package com.example.roywati.ncs.waiter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
 * Created by Chris on 5/10/2017.
 */
public class ListItemsAdapter extends BaseAdapter {


    FloatingActionButton fab;
    String[] itemName,itemId,itemPrice,itemDescr;
    Context context;
    LayoutInflater inflater;

    public ListItemsAdapter(Activity activity, String[]itemName, String[]itemId, String[]itemPrice, String[]itemDescr,FloatingActionButton fab){

        this.fab=fab;
        this.itemId=itemId;
        this.itemPrice=itemPrice;
        this.itemName=itemName;
        this.itemDescr=itemDescr;
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

   //     Holder holder=new Holder();
        view=inflater.inflate(R.layout.custom_menu_items,null);
        TextView id=(TextView)view.findViewById(R.id.menuitemcart_id);
        TextView name=(TextView)view.findViewById(R.id.menu_cart_item_name);
        TextView price=(TextView)view.findViewById(R.id.item_price);
        TextView descr=(TextView)view.findViewById(R.id.item_Descr_info);
       final ImageView imageView=(ImageView)view.findViewById(R.id.add_menu_item_cart);
        ImageView img=(ImageView)view.findViewById(R.id.more);
        LinearLayout layout=(LinearLayout)view.findViewById(R.id.lay);
        if(i%2==0){
            layout.setBackgroundColor(Color.parseColor("#c69577"));
            price.setTextColor(Color.parseColor("#542400"));
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);

                builder.setCancelable(true);
                builder.setTitle("Item description");
                builder.setMessage(itemDescr[i]);

                builder.show();
            }
        });

        id.setText(itemId[i]);
        name.setText(itemName[i]);
        price.setText(itemPrice[i]);
        descr.setText(itemDescr[i]);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation1=AnimationUtils.loadAnimation(context,R.anim.blink);
                imageView.startAnimation(animation1);
                AppConfig.menuCatId=itemId[i];
                Toast.makeText(context,AppConfig.menuCatId,Toast.LENGTH_SHORT).show();
                new AddItemToCart().execute();

            }
        });


        return view;
    }

    public void rotateFabButton(boolean state){
        if(state==true){
            Holder holder=new Holder();
            Animation animation= AnimationUtils.loadAnimation(context,R.anim.rotate_clock);
            fab.startAnimation(animation);

        }
    }

    public class AddItemToCart extends AsyncTask<String,String,String>{

        int successState=0;
        String serverMessage="request not sent";

        String TAG_MESSAGE="message";
        String TAG_SUCCESS="success";

            protected void onPreExecute(){
                super.onPreExecute();
                rotateFabButton(true);
            }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser=new JSONParser();
            List<NameValuePair> jsonObjectData=new ArrayList<NameValuePair>();
            jsonObjectData.add(new BasicNameValuePair("menu_item_id",AppConfig.menuCatId));
            jsonObjectData.add(new BasicNameValuePair("orderId",AppConfig.orderId));

            JSONObject jsonObjectResponse=jsonParser.makeHttpRequest(AppConfig.protocal+AppConfig.hostname+AppConfig.add_to_cart,
                    "GET",jsonObjectData);

            Log.d("orderId and menuCatId",AppConfig.orderId+" "+AppConfig.menuCatId);

            try{
                int success=jsonObjectResponse.getInt(TAG_SUCCESS);
                serverMessage=jsonObjectResponse.getString(TAG_MESSAGE);
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

            Log.d("successState",String.valueOf(successState));
            if(successState==1){
                Toast.makeText(context,serverMessage,Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(context,serverMessage,Toast.LENGTH_LONG).show();
            }
           rotateFabButton(false);
        }

    }

    public class Holder{
        ImageView imageView;
    }
}

package com.example.roywati.ncs.kitchen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.roywati.ncs.R;

/**
 * Created by Chris on 5/15/2017.
 */
public class MenuItemSelectedAdapter extends BaseAdapter {

    String[] itemName,quantity;
    Context context;
    public static LayoutInflater inflater=null;
    public MenuItemSelectedAdapter(Activity activity, String[]itemName, String[]quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        context = activity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


        @Override
    public int getCount() {
        return itemName.length;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.custom_kitchen_order,null);
        TextView id=(TextView)view.findViewById(R.id.menu_cart_item_name);
        TextView name=(TextView)view.findViewById(R.id.item_quantity);

        id.setText(itemName[i]);
        name.setText(quantity[i]);

        return view;
    }
}

package com.example.roywati.ncs.kitchen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.roywati.ncs.R;

public class MenuItemSelectedAdapter extends BaseAdapter {
    public static LayoutInflater inflater = null;
    Context context;
    String[] itemName;
    String[] quantity;

    public MenuItemSelectedAdapter(Activity activity, String[] itemName, String[] quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.context = activity;
        inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.itemName.length;
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_kitchen_order, null);
        TextView name = (TextView) view.findViewById(R.id.item_quantity);
        ((TextView) view.findViewById(R.id.menu_cart_item_name)).setText(this.itemName[i]);
        name.setText(this.quantity[i]);
        return view;
    }
}

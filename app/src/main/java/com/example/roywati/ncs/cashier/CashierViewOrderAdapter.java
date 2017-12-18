package com.example.roywati.ncs.cashier;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.roywati.ncs.R;

public class CashierViewOrderAdapter extends BaseAdapter {
    public static LayoutInflater inflater = null;
    Context context;
    String[] itemName;
    String[] quantity;
    LinearLayout viewColor;

    public CashierViewOrderAdapter(Activity activity, String[] itemName, String[] quantity) {
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
        view = inflater.inflate(R.layout.custom_unpaid_orders_layout, null);
        TextView id = (TextView) view.findViewById(R.id.order_num_cash_unpaid);
        TextView name = (TextView) view.findViewById(R.id.item_price_cash_unpaid);
        this.viewColor = (LinearLayout) view.findViewById(R.id.viewColor);
        id.setText(this.itemName[i]);
        name.setText(this.quantity[i]);
        if (i % 2 == 0) {
            this.viewColor.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        return view;
    }
}

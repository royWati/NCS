package com.example.roywati.ncs.waiter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.roywati.ncs.R;

public class viewOrderAdapter extends BaseAdapter {
    public static LayoutInflater inflater = null;
    String[] checkout;
    Context context;
    String[] order;
    String[] status;
    String[] table;

    public viewOrderAdapter(Activity activity, String[] order, String[] table, String[] status, String[] checkout) {
        this.order = order;
        this.table = table;
        this.status = status;
        this.checkout = checkout;
        this.context = activity;
        inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.order.length;
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_view_waiter_order, null);
        TextView tbl = (TextView) view.findViewById(R.id.table_view_orders);
        TextView sts = (TextView) view.findViewById(R.id.view_order_status);
        TextView check = (TextView) view.findViewById(R.id.checkout_time);
        ((TextView) view.findViewById(R.id.order_no)).setText(this.order[i]);
        tbl.setText(this.table[i]);
        sts.setText(this.status[i]);
        check.setText(this.checkout[i]);
        return view;
    }
}

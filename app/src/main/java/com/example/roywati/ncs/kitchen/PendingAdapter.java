package com.example.roywati.ncs.kitchen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.roywati.ncs.R;

public class PendingAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    String[] orderId;
    String[] tableName;

    public PendingAdapter(Activity activity, String[] orderId, String[] tableName) {
        this.orderId = orderId;
        this.tableName = tableName;
        this.context = activity;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return this.orderId.length;
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = this.inflater.inflate(R.layout.custom_pending_orders, null);
        TextView table = (TextView) view.findViewById(R.id.table_name_pending);
        ImageView imageView = (ImageView) view.findViewById(R.id.pending);
        ((TextView) view.findViewById(R.id.order_item_no_pending)).setText(this.orderId[i]);
        table.setText(this.tableName[i]);
        return view;
    }
}

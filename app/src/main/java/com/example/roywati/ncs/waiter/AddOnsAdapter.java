package com.example.roywati.ncs.waiter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.roywati.ncs.R;

/**
 * Created by Chris on 5/13/2017.
 */
public class AddOnsAdapter extends BaseAdapter {

    String[] sub_catId,sub_catName,table_name;
    Context context;
    public static LayoutInflater inflater=null;

    public AddOnsAdapter(Activity activity,String[]sub_catId,String[]sub_catName,String[]table_name){
        this.sub_catId=sub_catId;
        this.table_name=table_name;
        this.sub_catName=sub_catName;
        context=activity;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return sub_catId.length;
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
        view=inflater.inflate(R.layout.custom_kitchen_homepage,null);
        TextView table_selected=(TextView)view.findViewById(R.id.kitchen_homepage_name);
        TextView id=(TextView)view.findViewById(R.id.order_no_selected);
        TextView name=(TextView)view.findViewById(R.id.kitchen_homepage_id);

        table_selected.setText(table_name[i]+"-"+sub_catName[i]);
        id.setText(sub_catId[i]);
        name.setText(sub_catName[i]);

        return view;
    }
}

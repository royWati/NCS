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
 * Created by Chris on 5/13/2017.
 */
public class HomepageAdapter extends BaseAdapter {

    String[] sub_catId,sub_catName;
    Context context;
    public static LayoutInflater inflater=null;
    public HomepageAdapter(Activity activity,String[]sub_catId,String[]sub_catName){
        this.sub_catId=sub_catId;
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
        TextView id=(TextView)view.findViewById(R.id.kitchen_homepage_id);
        TextView name=(TextView)view.findViewById(R.id.kitchen_homepage_name);

        id.setText(sub_catId[i]);
        name.setText(sub_catName[i]);

        return view;
    }
}

package com.example.roywati.ncs.waiter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.roywati.ncs.R;

public class MenuSubCategoryAdapter extends BaseAdapter {
    public static LayoutInflater inflater = null;
    Context context;
    String[] sub_catId;
    String[] sub_catName;

    public MenuSubCategoryAdapter(Activity activity, String[] sub_catName, String[] sub_catId) {
        this.sub_catId = sub_catId;
        this.sub_catName = sub_catName;
        this.context = activity;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return this.sub_catId.length;
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_sub_menu_category, null);
        TextView name = (TextView) view.findViewById(R.id.subCategoryMenuName);
        ((TextView) view.findViewById(R.id.subCategoryMenuId)).setText(this.sub_catId[i]);
        name.setText(this.sub_catName[i]);
        return view;
    }
}

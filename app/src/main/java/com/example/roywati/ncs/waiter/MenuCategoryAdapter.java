package com.example.roywati.ncs.waiter;

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

public class MenuCategoryAdapter extends BaseAdapter {
    public static LayoutInflater inflater = null;
    Context context;
    String[] menuCatId;
    String[] menuCatName;

    public MenuCategoryAdapter(Activity activity, String[] menuCatName, String[] menuCatId) {
        this.menuCatName = menuCatName;
        this.menuCatId = menuCatId;
        this.context = activity;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return this.menuCatId.length;
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_menu_category, null);
        TextView name = (TextView) view.findViewById(R.id.menuCategoryName);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.lau);
        ((TextView) view.findViewById(R.id.menuCategoryId)).setText(this.menuCatId[i]);
        name.setText(this.menuCatName[i]);
        if (i % 2 == 0) {
            layout.setBackgroundColor(Color.parseColor("#c69577"));
        }
        return view;
    }
}

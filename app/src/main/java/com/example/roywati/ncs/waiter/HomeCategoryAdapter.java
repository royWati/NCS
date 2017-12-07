package com.example.roywati.ncs.waiter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roywati.ncs.R;

/**
 * Created by Chris on 5/8/2017.
 */
public class HomeCategoryAdapter extends BaseAdapter {

    String[] itemId,itemName;
    int[]images;
    Context context;
    public static LayoutInflater inflater=null;

    public HomeCategoryAdapter(Homepage menuActivity, String[]itemId, String[]itemName, int[]images){
        this.itemId=itemId;
        this.images=images;
        this.itemName=itemName;
        context=menuActivity;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return itemId.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        view=inflater.inflate(R.layout.custom_gridview,null);
        TextView id=(TextView)view.findViewById(R.id.menu_category_id);
        TextView name=(TextView)view.findViewById(R.id.menu_name);
        ImageView imageView=(ImageView)view.findViewById(R.id.menu_action_image);

        imageView.setImageResource(images[position]);

        id.setText(itemId[position]);
        name.setText(itemName[position]);

        return view;
    }
}

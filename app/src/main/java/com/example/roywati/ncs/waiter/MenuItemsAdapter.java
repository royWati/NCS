package com.example.roywati.ncs.waiter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roywati.ncs.R;

/**
 * Created by Chris on 5/9/2017.
 */
public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.ListViewHolder> {

    String[] itemName,itemId,itemPrice,itemDescr;
    Context context;
    LayoutInflater inflater;

    public MenuItemsAdapter(Activity activity,String[]itemName,String[]itemId,String[]itemPrice,String[]itemDescr){

        this.itemId=itemId;
        this.itemPrice=itemPrice;
        this.itemName=itemName;
        this.itemDescr=itemDescr;
        context=activity;
        inflater=LayoutInflater.from(context);

    }


    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View contentView=inflater.inflate(R.layout.custom_menu_items,parent,false);
        ListViewHolder viewHolder=new ListViewHolder(contentView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        holder.menuItemId.setText(itemId[position]);
        holder.item_descr.setText(itemDescr[position]);
        holder.menuItemName.setText(itemName[position]);
        holder.item_price.setText(itemPrice[position]);

        holder.cart_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfig.menuCatId=holder.menuItemId.getText().toString();
                Toast.makeText(context,AppConfig.menuCatId,Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemId.length;
    }
    public class ListViewHolder extends RecyclerView.ViewHolder
    {
        TextView menuItemName,menuItemId,item_descr,item_price;
        ImageView cart_item;


        public ListViewHolder(View itemView)
        {
            super(itemView);
            menuItemName=(TextView)itemView.findViewById(R.id.menu_cart_item_name);
            menuItemId=(TextView)itemView.findViewById(R.id.menuitemcart_id);
            item_descr=(TextView)itemView.findViewById(R.id.item_Descr_info);
            item_price=(TextView)itemView.findViewById(R.id.item_price);
           cart_item=(ImageView)itemView.findViewById(R.id.add_menu_item_cart);


        }


    }
}

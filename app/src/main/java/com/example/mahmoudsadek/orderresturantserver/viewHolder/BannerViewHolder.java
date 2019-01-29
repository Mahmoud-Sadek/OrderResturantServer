package com.example.mahmoudsadek.orderresturantserver.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmoudsadek.orderresturantserver.Interface.ItemClickListener;
import com.example.mahmoudsadek.orderresturantserver.R;
import com.example.mahmoudsadek.orderresturantserver.common.Common;

/**
 * Created by Mahmoud Sadek on 11/29/2018.
 */

public class BannerViewHolder extends RecyclerView.ViewHolder implements
        View.OnCreateContextMenuListener{

    public TextView banner_name;
    public ImageView banner_image;


    public BannerViewHolder(View itemView) {
        super(itemView);
        banner_name = itemView.findViewById(R.id.banner_name);
        banner_image = itemView.findViewById(R.id.banner_image);
        itemView.setOnCreateContextMenuListener(this);

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");

        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);

    }
}

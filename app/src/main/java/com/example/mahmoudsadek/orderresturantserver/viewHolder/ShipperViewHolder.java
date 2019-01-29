package com.example.mahmoudsadek.orderresturantserver.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmoudsadek.orderresturantserver.Interface.ItemClickListener;
import com.example.mahmoudsadek.orderresturantserver.R;
import com.example.mahmoudsadek.orderresturantserver.common.Common;

/**
 * Created by Mahmoud Sadek on 11/29/2018.
 */

public class ShipperViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener {

    public TextView shipper_name, shipper_phone;
    public Button btn_edit, btn_remove;
    private ItemClickListener itemClickListener;

    public ShipperViewHolder(View itemView) {
        super(itemView);
        shipper_name = itemView.findViewById(R.id.shipper_name);
        shipper_phone = itemView.findViewById(R.id.shipper_phone);
        btn_edit = itemView.findViewById(R.id.btnEdit);
        btn_remove = itemView.findViewById(R.id.btnRemove);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}

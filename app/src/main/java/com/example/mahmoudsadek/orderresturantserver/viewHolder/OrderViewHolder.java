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
 * Created by Mahmoud Sadek on 6/18/2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOrderID, txtOrderStatus, txtOrderPhone, txtOrderAddress, textOrderDate;
    public Button btnEdit, btnRemove, btnDetail, btnDirection, btnCall;



    public OrderViewHolder(View itemView) {
        super(itemView);
        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderID = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        textOrderDate = itemView.findViewById(R.id.order_date);

        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnRemove = itemView.findViewById(R.id.btnRemove);
        btnDetail = itemView.findViewById(R.id.btnDetail);
        btnDirection = itemView.findViewById(R.id.btnDirection);
        btnCall = itemView.findViewById(R.id.btnCall);

    }

}


package com.example.mahmoudsadek.orderresturantserver.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mahmoudsadek.orderresturantserver.R;
import com.example.mahmoudsadek.orderresturantserver.model.Order;

import java.util.List;

/**
 * Created by Mahmoud Sadek on 8/16/2018.
 */
class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView name, quantity, price, discount;
    public MyViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.product_name);
        quantity = itemView.findViewById(R.id.product_quantity);
        price = itemView.findViewById(R.id.product_price);
        discount = itemView.findViewById(R.id.product_discount);
    }
}
public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder>{
    List<Order> myOrders;

    public OrderDetailAdapter(List<Order> foods) {
        this.myOrders = foods;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = myOrders.get(position);
        holder.name.setText(String.format("Name : %s", order.getProductName()));
        holder.quantity.setText(String.format("Quantity : %s", order.getQuantity()));
        holder.price.setText(String.format("Price : %s", order.getPrice()));
        holder.discount.setText(String.format("Discount : %s", order.getDicount()));
    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}

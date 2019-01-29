package com.example.mahmoudsadek.orderresturantserver;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mahmoudsadek.orderresturantserver.common.Common;
import com.example.mahmoudsadek.orderresturantserver.model.DataMessage;
import com.example.mahmoudsadek.orderresturantserver.model.MyResponse;
import com.example.mahmoudsadek.orderresturantserver.model.Request;
import com.example.mahmoudsadek.orderresturantserver.model.Token;
import com.example.mahmoudsadek.orderresturantserver.remote.APIService;
import com.example.mahmoudsadek.orderresturantserver.viewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ref_request;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    MaterialSpinner materialSpinner, shipperSpinner;
    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        // init service
        mService = Common.getFCMClient();

        //int Firebase
        database = FirebaseDatabase.getInstance();
        ref_request = database.getReference("Requests");
        //Load
        recyclerView = (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();
    }

    private void loadOrders() {

        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(ref_request, Request.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_item, parent, false);

                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, final int position, @NonNull final Request model) {

                holder.txtOrderID.setText(adapter.getRef(position).getKey());
                // convert timeStamp to Actual date then set on textView
                holder.textOrderDate.setText(Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));
                holder.txtOrderPhone.setText(model.getPhone());
                holder.txtOrderAddress.setText(model.getAddress());
                holder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));

                holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(), adapter.getItem(position));

                    }
                });
                holder.btnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + model.getPhone()));
                        startActivity(intent);

                    }
                });
                holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*new AlertDialog.Builder(OrderStatusActivity.this)
                                .setMessage("Are you sure you want to delete?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // start code from here

                                        deleteOrder(adapter.getRef(position).getKey());

                                        // end code here


                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();*/
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatusActivity.this);
                        alertDialog.setTitle("Are you sure you want to delete?");


                        LayoutInflater inflater = LayoutInflater.from(getBaseContext());

                        View view = inflater.inflate(R.layout.delete_order_dialog, null);

                        final EditText delete_order = view.findViewById(R.id.delete_order);

                        alertDialog.setView(view);
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                if (TextUtils.isEmpty(delete_order.getText())) {
                                    Toast.makeText(OrderStatusActivity.this, "must enter comment ", Toast.LENGTH_SHORT).show();
                                } else {


                                    final android.app.AlertDialog waitingDialog = new SpotsDialog(OrderStatusActivity.this);

                                    waitingDialog.show();
                                    deleteOrder(adapter.getRef(position).getKey(), model, delete_order.getText().toString());

                                }

                            }
                        });

                        alertDialog.show();

                    }
                });
                holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent orderDetail = new Intent(OrderStatusActivity.this, OrderDetailActivity.class);
                        Common.currentRequest = model;
                        orderDetail.putExtra("OrderId", adapter.getRef(position).getKey());
                        startActivity(orderDetail);

                    }
                });
                holder.btnDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent trackingOrder = new Intent(OrderStatusActivity.this, TrackingOrderActivity.class);
                        Common.currentRequest = model;
                        startActivity(trackingOrder);

                    }
                });


            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }


    private void deleteOrder(String key, Request model, String text) {

        model.setComment(text);
        model.setStatus("removed");
        ref_request.child(key).setValue(model);
        adapter.notifyDataSetChanged();
    }

    private void showUpdateDialog(String key, final Request item) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatusActivity.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please choose status");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout, null);

        materialSpinner = view.findViewById(R.id.statusSpinner);
        materialSpinner.setItems("Placed", "On my way", "Shipping");

        shipperSpinner = view.findViewById(R.id.shipperSpinner);
        //Load All Shippers Phone

        final List<String> shipperList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.SHIPPERS_TABLE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot shipperSnapshot : dataSnapshot.getChildren()) {
                            shipperList.add(shipperSnapshot.getKey());
                        }
                        shipperSpinner.setItems(shipperList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        alertDialog.setView(view);

        final String localKey = key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(materialSpinner.getSelectedIndex()));

                if (item.getStatus().equals("2")) {
                    //Copy Item To Orders Need Ship Table
                    FirebaseDatabase.getInstance().getReference(Common.ORDERS_NEED_SHIP_TABLE)
                            .child(shipperSpinner.getItems().get(shipperSpinner.getSelectedIndex()).toString())
                            .child(localKey)
                            .setValue(item);
                    ref_request.child(localKey).setValue(item);
                    adapter.notifyDataSetChanged();

                    sendOrderShipRequestToShipper(shipperSpinner.getItems().get(shipperSpinner.getSelectedIndex()).toString(), item);

                } else {
                    ref_request.child(localKey).setValue(item);
                    adapter.notifyDataSetChanged();

                    sendOrderStatusToUser(localKey, item);
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void sendOrderShipRequestToShipper(final String shipperPhone, Request item) {
        DatabaseReference ref_tokens = database.getReference("Tokens");
        ref_tokens.child(shipperPhone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Token severToken = dataSnapshot.getValue(Token.class);

                            Map<String, String> dataSender = new HashMap<>();
                            dataSender.put("title", "SADEK RESTAURANT");
                            dataSender.put("message", "Your have new order need ship");
                            DataMessage dataMessage = new DataMessage(severToken.getToken(), dataSender);

                            mService.sendNotification(dataMessage)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if (response.code() == 200) {
                                                if (response.body().success == 1) {
                                                    Toast.makeText(OrderStatusActivity.this, "Sent to shipper", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(OrderStatusActivity.this, "Failed to send notification", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("Error", "onFailure: " + t.getMessage());
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void sendOrderStatusToUser(final String localKey, Request item) {
        DatabaseReference ref_tokens = database.getReference("Tokens");
        ref_tokens.child(item.getPhone())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Token severToken = dataSnapshot.getValue(Token.class);

                            //Create raw payload to send
                            /*Notification notification = new Notification("SADEK RESTAURANT", "Your Order " + localKey + " was updated");
                            Sender content = new Sender(severToken.getToken(), notification);*/

                            Map<String, String> dataSender = new HashMap<>();
                            dataSender.put("title", "SADEK RESTAURANT");
                            dataSender.put("message", "Your Order " + localKey + " was updated");
                            DataMessage dataMessage = new DataMessage(severToken.getToken(), dataSender);

                            mService.sendNotification(dataMessage)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if (response.code() == 200) {
                                                if (response.body().success == 1) {
                                                    Toast.makeText(OrderStatusActivity.this, "Thank You , Order Place", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(OrderStatusActivity.this, "Order was updated but Failed to send notification", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("Error", "onFailure: " + t.getMessage());
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}

package com.example.mahmoudsadek.orderresturantserver;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mahmoudsadek.orderresturantserver.common.Common;
import com.example.mahmoudsadek.orderresturantserver.model.Banner;
import com.example.mahmoudsadek.orderresturantserver.model.Shipper;
import com.example.mahmoudsadek.orderresturantserver.viewHolder.BannerViewHolder;
import com.example.mahmoudsadek.orderresturantserver.viewHolder.ShipperViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ShipperManagmentActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ref_shipperList;

    FloatingActionButton fab;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    FirebaseRecyclerAdapter<Shipper, ShipperViewHolder> adapter;
    MaterialEditText edtShipperName, edtShipperPhone, edtShipperPassword;

    Shipper newShipper;
    Uri saveUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper_managment);

        //int Firebase
        database = FirebaseDatabase.getInstance();
        ref_shipperList = database.getReference(Common.SHIPPERS_TABLE);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddShipperDialog();
            }
        });

        //Load Food
        recyclerView = findViewById(R.id.recycler_shippers);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        LoadListShipper();
    }

    private void LoadListShipper() {
        FirebaseRecyclerOptions<Shipper> options = new FirebaseRecyclerOptions.Builder<Shipper>()
                .setQuery(ref_shipperList, Shipper.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Shipper, ShipperViewHolder>(options) {
            @Override
            public ShipperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shipper_item, parent, false);

                return new ShipperViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ShipperViewHolder viewHolder, final int position, @NonNull final Shipper model) {
                viewHolder.shipper_name.setText(model.getName());
                viewHolder.shipper_phone.setText(model.getPhone());
                viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditDialog(adapter.getRef(position).getKey(), model);
                    }
                });
                viewHolder.btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeShipper(adapter.getRef(position).getKey());
                    }
                });
                final Shipper local = model;


            }

        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    private void removeShipper(String key) {
        ref_shipperList.child(key).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ShipperManagmentActivity.this, "Remmoved Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShipperManagmentActivity.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog(String key, Shipper model) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShipperManagmentActivity.this);
        alertDialog.setTitle("Update Shipper");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View addMenuLayout = inflater.inflate(R.layout.add_new_shipper_layout, null);

        edtShipperName = addMenuLayout.findViewById(R.id.edtName);
        edtShipperPhone = addMenuLayout.findViewById(R.id.edtPhone);
        edtShipperPassword = addMenuLayout.findViewById(R.id.edtPassword);
        alertDialog.setView(addMenuLayout);
        alertDialog.setIcon(R.drawable.ic_local_shipping_black_24dp);

        edtShipperName.setText(model.getName());
        edtShipperPhone.setText(model.getPhone());
        edtShipperPassword.setText(model.getPassword());

        //Set button
        alertDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                final android.app.AlertDialog waitingDialog = new SpotsDialog(ShipperManagmentActivity.this);
                waitingDialog.show();

                Map<String, Object> update = new HashMap<>();
                update.put("name", edtShipperName.getText().toString());
                update.put("phone", edtShipperPhone.getText().toString());
                update.put("password", edtShipperPassword.getText().toString());

                newShipper = new Shipper(edtShipperName.getText().toString(), edtShipperPhone.getText().toString(), edtShipperPassword.getText().toString());
                if (newShipper != null) {
                    ref_shipperList.child(edtShipperPhone.getText().toString()).updateChildren(update)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Snackbar.make(fab, "Shipper Updated " + newShipper.getName() + " was added", Snackbar.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(fab, "Failed " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showAddShipperDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShipperManagmentActivity.this);
        alertDialog.setTitle("Add new Shipper");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View addMenuLayout = inflater.inflate(R.layout.add_new_shipper_layout, null);

        edtShipperName = addMenuLayout.findViewById(R.id.edtName);
        edtShipperPhone = addMenuLayout.findViewById(R.id.edtPhone);
        edtShipperPassword = addMenuLayout.findViewById(R.id.edtPassword);
        alertDialog.setView(addMenuLayout);
        alertDialog.setIcon(R.drawable.ic_local_shipping_black_24dp);

        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                final android.app.AlertDialog waitingDialog = new SpotsDialog(ShipperManagmentActivity.this);
                waitingDialog.show();
                newShipper = new Shipper(edtShipperName.getText().toString(), edtShipperPhone.getText().toString(), edtShipperPassword.getText().toString());
                if (newShipper != null) {
                    ref_shipperList.child(edtShipperPhone.getText().toString()).setValue(newShipper)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar.make(fab, "New Shipper " + newShipper.getName() + " was added", Snackbar.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(fab, "Failed " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}

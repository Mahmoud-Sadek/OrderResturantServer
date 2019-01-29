package com.example.mahmoudsadek.orderresturantserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mahmoudsadek.orderresturantserver.common.Common;
import com.example.mahmoudsadek.orderresturantserver.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignInActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;
    DatabaseReference ref_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.bnSignIn);

        //Inti Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref_user = database.getReference("User");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    signInUser(edtPhone.getText().toString(), edtPassword.getText().toString());
                }else {
                    Toast.makeText(SignInActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void signInUser(String phone, String password) {
        final ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
        mDialog.setMessage("Please Waiting ...");
        mDialog.show();

        final String localPhone = phone;
        final String localPass = password;
        ref_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDialog.dismiss();
                //check user exist in database
                if (dataSnapshot.child(localPhone).exists()) {
                    //get User data
                    User user = dataSnapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
                    if (Boolean.parseBoolean(user.getIsStuff())) {
                        if (user.getPassword().equals(localPass)) {
                            Intent home = new Intent(SignInActivity.this, HomeActivity.class);
                            Common.currentUser = user;
                            startActivity(home);
                            finish();
                        } else {
                            Toast.makeText(SignInActivity.this, "Wrong Password !!", Toast.LENGTH_SHORT).show();
                        }
                    }else
                        Toast.makeText(SignInActivity.this, "Please Login with stuff account !!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SignInActivity.this, "User Not Exist In Database !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

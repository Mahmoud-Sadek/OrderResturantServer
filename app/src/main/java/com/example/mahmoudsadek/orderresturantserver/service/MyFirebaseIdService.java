package com.example.mahmoudsadek.orderresturantserver.service;

import com.example.mahmoudsadek.orderresturantserver.common.Common;
import com.example.mahmoudsadek.orderresturantserver.model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Mahmoud Sadek on 8/16/2018.
 */

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        if (Common.currentUser != null)
            updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        if (Common.currentUser != null) {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref_tokens = db.getReference("Tokens");
            Token token = new Token(tokenRefreshed, true);
            ref_tokens.child(Common.currentUser.getPhone()).setValue(token);
        }
    }
}

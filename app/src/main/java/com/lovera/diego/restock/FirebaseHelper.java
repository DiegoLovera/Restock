package com.lovera.diego.restock;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseHelper extends FirebaseInstanceIdService {
    public FirebaseHelper() {
        super();
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null){
            //insert to the database
            if (RestockApp.ACTUAL_USER != null){
                FirebaseDatabase.getInstance().getReference("User")
                        .child(RestockApp.ACTUAL_USER.getUid())
                        .child("Token").setValue(refreshedToken);
            }
        }
    }
}

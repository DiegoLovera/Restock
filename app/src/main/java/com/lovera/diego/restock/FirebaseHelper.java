package com.lovera.diego.restock;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseHelper extends FirebaseMessagingService {
    public FirebaseHelper() {
        super();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
                new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String newToken = instanceIdResult.getToken();
                        Log.e("newToken",newToken);
                        if (RestockApp.ACTUAL_USER != null){
                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(RestockApp.ACTUAL_USER.getUid())
                                    .child("Token")
                                    .setValue(newToken);
                        }
                    }
                });
        Log.e("NEW_TOKEN",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}

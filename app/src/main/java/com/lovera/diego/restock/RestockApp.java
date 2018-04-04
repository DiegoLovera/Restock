package com.lovera.diego.restock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RestockApp extends AppCompatActivity {
    //region Fields
    public static FirebaseUser ACTUAL_USER;
    //endregion

    //region onCreate
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //endregion
    //region onStart
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            ACTUAL_USER = mAuth.getCurrentUser();
            startActivity(new Intent(this, MainActivity.class));
        } else{
            startActivity(new Intent(this, LandingActivity.class));
        }
    }
    //endregion
    //region onStop
    @Override
    protected void onStop() {
        super.onStop();
    }
    //endregion
    //region onDestroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //endregion
}

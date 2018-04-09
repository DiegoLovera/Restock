package com.lovera.diego.restock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lovera.diego.restock.models.Order;
import com.lovera.diego.restock.models.OrderContent;
import com.lovera.diego.restock.models.Product;

import java.util.ArrayList;
import java.util.List;

public class RestockApp extends AppCompatActivity {
    //region Fields
    public static FirebaseUser ACTUAL_USER;
    public static Order ACTUAL_ORDER;
    public static List<OrderContent> ACTUAL_ORDER_CONTENT;
    public static List<Product> ACTUAL_PRODUCT_LIST;
    //endregion

    //region onCreate
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ACTUAL_ORDER == null){
            ACTUAL_ORDER = new Order();
        }
        if (ACTUAL_ORDER_CONTENT == null){
            ACTUAL_ORDER_CONTENT = new ArrayList<>();
        }
        if (ACTUAL_PRODUCT_LIST == null){
            ACTUAL_PRODUCT_LIST = new ArrayList<>();
        }
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

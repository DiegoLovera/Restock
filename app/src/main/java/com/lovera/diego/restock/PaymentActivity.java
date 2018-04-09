package com.lovera.diego.restock;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lovera.diego.restock.models.OrderContent;

public class PaymentActivity extends AppCompatActivity {

    private DatabaseReference mOrderRef;
    private DatabaseReference mOrderContentRef;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mOrderRef = FirebaseDatabase.getInstance().getReference().child("Order");
        mOrderContentRef = FirebaseDatabase.getInstance().getReference().child("OrderContent");

        TextView textViewTotalToPay = findViewById(R.id.text_total_amount_payment);
        textViewTotalToPay.setText(String.valueOf(calculateTotal()));

        Button buttonContinuePay = findViewById(R.id.button_continue_payment);
        buttonContinuePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestockApp.ACTUAL_ORDER.setUser(RestockApp.ACTUAL_USER.getUid());
                String Key = mOrderRef.push().getKey();
                mOrderRef.child(Key).setValue(RestockApp.ACTUAL_ORDER);

                for (OrderContent orderContent : RestockApp.ACTUAL_ORDER_CONTENT){
                    orderContent.setOrder(Key);
                    mOrderContentRef.push().setValue(orderContent);
                }
                Toast.makeText(v.getContext(), "Successful order", Toast.LENGTH_LONG).show();
                startActivity(new Intent(v.getContext(), MainActivity.class));
            }
        });

    }

    private int calculateTotal(){
        int total = 0;
        for (OrderContent actual : RestockApp.ACTUAL_ORDER_CONTENT){
            total += Integer.parseInt(actual.getTotal());
        }
        return total;
    }
}

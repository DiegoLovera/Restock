package com.lovera.diego.restock;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lovera.diego.restock.models.Order;
import com.lovera.diego.restock.models.OrderContent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private DatabaseReference mOrderRef;
    private DatabaseReference mOrderContentRef;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOrderRef = FirebaseDatabase.getInstance().getReference().child("Order");
        mOrderContentRef = FirebaseDatabase.getInstance().getReference().child("OrderContent");

        TextView textViewTotalToPay = findViewById(R.id.text_total_amount_payment);
        textViewTotalToPay.setText(String.valueOf(calculateTotal()));

        Button buttonContinuePay = findViewById(R.id.button_continue_payment);
        buttonContinuePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestockApp.ACTUAL_ORDER.setUser(RestockApp.ACTUAL_USER.getUid());
                RestockApp.ACTUAL_ORDER.setTotal(String.valueOf(calculateTotal()));

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 1);
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                String formatted = format1.format(cal.getTime());
                RestockApp.ACTUAL_ORDER.setDate(formatted);
                String Key = mOrderRef.push().getKey();
                RestockApp.ACTUAL_ORDER.setUuid(Key);

                mOrderRef.child(Key).setValue(RestockApp.ACTUAL_ORDER);

                for (OrderContent orderContent : RestockApp.ACTUAL_ORDER_CONTENT){
                    orderContent.setOrder(Key);
                    mOrderContentRef.push().setValue(orderContent);
                }
                Toast.makeText(v.getContext(), "Successful order", Toast.LENGTH_LONG).show();
                RestockApp.ACTUAL_ORDER_CONTENT.clear();
                RestockApp.ACTUAL_PRODUCT_LIST.clear();
                RestockApp.ACTUAL_ORDER = new Order();
                RestockApp.ACTUAL_ORDER.setUser(RestockApp.ACTUAL_USER.getUid());
                startActivity(new Intent(v.getContext(), MainActivity.class));
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int calculateTotal(){
        int total = 0;
        for (OrderContent actual : RestockApp.ACTUAL_ORDER_CONTENT){
            total += Integer.parseInt(actual.getTotal());
        }
        return total;
    }
}

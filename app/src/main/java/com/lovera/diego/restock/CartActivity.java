package com.lovera.diego.restock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lovera.diego.restock.adapters.CartItemAdapter;
import com.lovera.diego.restock.models.OrderContent;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.ItemClickListener {
    //region Fields
    private RecyclerView mRecyclerView;
    private CartItemAdapter mCartItemAdapter;
    private TextView mTitleTotalText;
    private Button mButtonNext;
    //endregion
    //region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.recycler_view_cart_activity);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCartItemAdapter = new CartItemAdapter(this, RestockApp.ACTUAL_ORDER_CONTENT);
        mCartItemAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mCartItemAdapter);
        mTitleTotalText = findViewById(R.id.text_total_amount);
        mTitleTotalText.setText(String.valueOf(calculateTotal()));
        mCartItemAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mTitleTotalText.setText(String.valueOf(calculateTotal()));
            }
        });

        mButtonNext = findViewById(R.id.button_continue_cart);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), MapOrderActivity.class));
            }
        });
    }
    //endregion

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //region onCartItemClick
    @Override
    public void onCartItemClick(View view, int position) {

    }
    //endregion
    //region calculateTotal
    private int calculateTotal(){
        int total = 0;
        for (OrderContent actual : RestockApp.ACTUAL_ORDER_CONTENT){
            total += Integer.parseInt(actual.getTotal());
        }
        return total;
    }
    //endregion
}
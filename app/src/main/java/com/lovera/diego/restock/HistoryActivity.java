package com.lovera.diego.restock;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lovera.diego.restock.Listeners.RequestListener;
import com.lovera.diego.restock.adapters.HistoryItemAdapter;
import com.lovera.diego.restock.models.Order;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements HistoryItemAdapter.ItemClickListener {

    private RecyclerView mRecyclerViewHistory;
    private HistoryItemAdapter mHistoryItemAdapter;
    private List<Order> mOrderList;
    private RequestListener mRequestListener;
    //TODO Cambiar ele hecho de que cada que un valor cambio carga de nuevo todo el adapter, solo deberia cambiar el dato que haya cambiado
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOrderList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
        Query query = reference.orderByChild("User").equalTo(RestockApp.ACTUAL_USER.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mOrderList.clear();
                    for (DataSnapshot typeSnapshot : dataSnapshot.getChildren()) {
                        Order order = typeSnapshot.getValue(Order.class);
                        if (order != null){
                            switch (order.getStatus()) {
                                case "3":
                                    mRequestListener.OnRequestStatusChanged(order);
                                    break;
                            }
                            mOrderList.add(order);
                        }
                    }
                    setRecyclerHistoryAdapter();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRecyclerViewHistory = findViewById(R.id.recycler_view_history_activity);
        mRecyclerViewHistory.setHasFixedSize(true);
        mRecyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
    }

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
    public void onHistoryItemClick(View view, int position) {

    }
    //endregion

    private void setRecyclerHistoryAdapter(){
        mHistoryItemAdapter = new HistoryItemAdapter(this, mOrderList);
        mHistoryItemAdapter.setClickListener(this);
        mRecyclerViewHistory.setAdapter(mHistoryItemAdapter);
    }
}
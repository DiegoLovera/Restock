package com.lovera.diego.restock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lovera.diego.restock.adapters.HistoryItemAdapter;
import com.lovera.diego.restock.models.Order;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements HistoryItemAdapter.ItemClickListener {

    private RecyclerView mRecyclerViewHistory;
    private HistoryItemAdapter mHistoryItemAdapter;
    private List<Order> mOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mOrderList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
        Query query = reference.orderByChild("User").equalTo(RestockApp.ACTUAL_USER.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mOrderList.clear();
                    for (DataSnapshot typeSnapshot : dataSnapshot.getChildren()) {
                        mOrderList.add(typeSnapshot.getValue(Order.class));
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
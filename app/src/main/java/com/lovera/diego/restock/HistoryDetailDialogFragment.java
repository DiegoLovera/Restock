package com.lovera.diego.restock;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lovera.diego.restock.adapters.DetailProductItemAdapter;
import com.lovera.diego.restock.common.Serializer;
import com.lovera.diego.restock.models.Order;
import com.lovera.diego.restock.models.OrderContent;

import java.util.ArrayList;
import java.util.List;

public class HistoryDetailDialogFragment extends DialogFragment {

    private Order mOrder;
    private List<OrderContent> mOrderContentList;
    private RecyclerView mRecyclerViewDetail;
    private DetailProductItemAdapter mDetailProductItemAdaper;
    private MapView mMapView;
    private GoogleMap mGoogleMap;

    public HistoryDetailDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_detail_dialog, container, false);
        mOrderContentList = new ArrayList<>();
        if (getArguments() != null) {
            mOrder = Serializer.Deserialize(getArguments().getString("orderJson"), Order.class);
        }
        TextView textTotalPay = view.findViewById(R.id.text_total_dialog_fragment);
        textTotalPay.setText(mOrder.getTotal());
        TextView mTextStatus = view.findViewById(R.id.text_status_order_detail_dialog);
        switch (mOrder.getStatus()) {
            case "1":
                mTextStatus.setTextColor(view.getContext().getResources().getColor(R.color.red));
                mTextStatus.setText("Pending");
                break;
            case "2":
                mTextStatus.setTextColor(view.getContext().getResources().getColor(R.color.orange));
                mTextStatus.setText("Incoming");
                break;
            case "3":
                mTextStatus.setTextColor(view.getContext().getResources().getColor(R.color.green));
                mTextStatus.setText("Delivered");
                break;
        }

        mRecyclerViewDetail = view.findViewById(R.id.recyclerview_histoy_detail_dialog);
        mRecyclerViewDetail.setHasFixedSize(true);
        mRecyclerViewDetail.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mMapView = view.findViewById(R.id.mapView_history_detail);
        mMapView.onCreate(null);
        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsInitializer.initialize(view.getContext());
                double lat = Double.parseDouble(mOrder.getLat());
                double lng = Double.parseDouble(mOrder.getLng());
                mGoogleMap = googleMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng));
                googleMap.addMarker(marker);

                CameraPosition camera = CameraPosition.builder().target(new LatLng(lat,lng)).zoom(18).bearing(0).tilt(45).build();

                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OrderContent");
        Query query = reference.orderByChild("Order").equalTo(mOrder.getUuid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot typeSnapshot : dataSnapshot.getChildren()) {
                        mOrderContentList.add(typeSnapshot.getValue(OrderContent.class));
                    }
                    setRecyclerHistoryAdapter(view);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void setRecyclerHistoryAdapter(View v){
        mDetailProductItemAdaper = new DetailProductItemAdapter(v.getContext(), mOrderContentList);
        mDetailProductItemAdaper.setClickListener(new DetailProductItemAdapter.ItemClickListener() {
            @Override
            public void onDetailProductItemClick(View view, int position) {

            }
        });
        mRecyclerViewDetail.setAdapter(mDetailProductItemAdaper);
    }
}

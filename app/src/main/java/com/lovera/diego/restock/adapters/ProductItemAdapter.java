package com.lovera.diego.restock.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.lovera.diego.restock.R;
import com.lovera.diego.restock.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ViewHolder> {

    private ArrayList<Product> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public ProductItemAdapter(Context context, ArrayList<Product> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.recyclerview_product_item, parent, false);
            return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            Product actualProduct = mData.get(position);
            holder.mProductName.setText(actualProduct.getName());
            holder.mProductPrice.setText(actualProduct.getPrice());
            holder.mProductDescription.setText(actualProduct.getDescription());
    }

    // total number of cells
    @Override
    public int getItemCount() {
            return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mProductName;
        TextView mProductPrice;
        TextView mProductDescription;
        Button mButtonAdd;
        Spinner mSpinnerQuantity;

        ViewHolder(View itemView) {
            super(itemView);
            this.mProductName = itemView.findViewById(R.id.ProductItemName);
            this.mProductPrice = itemView.findViewById(R.id.ProductItemPrice);
            this.mProductDescription = itemView.findViewById(R.id.ProductItemDescription);
            this.mButtonAdd = itemView.findViewById(R.id.ProductItemAdd);
            this.mSpinnerQuantity = itemView.findViewById(R.id.ProductItemSpinnerQuantity);

            String[] arraySpinner = new String[] {
                    "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "More"
            };

            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_item, arraySpinner);
            adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
            mSpinnerQuantity.setAdapter(adapter);

            this.mButtonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: ADD ACTION TO DE ADD BUTTON
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }

    // convenience method for getting data at click position
//    String getItem(int id) {
//        //return mData[id];
//    }

}

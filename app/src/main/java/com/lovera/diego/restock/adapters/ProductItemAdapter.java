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
    //region Fields
    private ArrayList<Product> mData;
    private LayoutInflater mInflater;
    //endregion

    //region Constructors
    public ProductItemAdapter(Context context, ArrayList<Product> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
    }
    //endregion

    //region onCreateViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.recyclerview_product_item, parent, false);
            return new ViewHolder(view);
    }
    //endregion
    //region onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Product actualProduct = mData.get(position);
            holder.mProductName.setText(actualProduct.getName());
            holder.mProductPrice.setText(actualProduct.getPrice());
            holder.mProductDescription.setText(actualProduct.getDescription());
    }
    //endregion
    //region getItemCount
    @Override
    public int getItemCount() {
            return mData.size();
    }
    //endregion

    //region Class ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //region Fields
        TextView mProductName;
        TextView mProductPrice;
        TextView mProductDescription;
        Button mButtonAdd;
        Button mButtonMore;
        //endregion
        //region Constructors
        ViewHolder(View itemView) {
            super(itemView);
            this.mProductName = itemView.findViewById(R.id.ProductItemName);
            this.mProductPrice = itemView.findViewById(R.id.ProductItemPrice);
            this.mProductDescription = itemView.findViewById(R.id.ProductItemDescription);
            this.mButtonAdd = itemView.findViewById(R.id.ProductItemAdd);
            this.mButtonMore = itemView.findViewById(R.id.ProductItemMore);

            this.mButtonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: ADD ACTION TO DE ADD BUTTON
                }
            });
        }
        //endregion
        //region onClick
        @Override
        public void onClick(View v) {

        }
        //endregion
    }
    //endregion
}
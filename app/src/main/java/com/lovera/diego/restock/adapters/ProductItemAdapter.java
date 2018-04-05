package com.lovera.diego.restock.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lovera.diego.restock.ProductDialogFragment;
import com.lovera.diego.restock.R;
import com.lovera.diego.restock.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ViewHolder> {
    //region Fields
    private ArrayList<Product> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    //endregion

    //region Constructors
    public ProductItemAdapter(Context context, ArrayList<Product> data) {
            this.mContext = context;
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
        holder.mProduct = mData.get(position);
        holder.mContext = this.mContext;
        holder.mProductName.setText(holder.mProduct.getName());
        holder.mProductPrice.setText(holder.mProduct.getPrice());
        holder.mProductDescription.setText(holder.mProduct.getDescription());
        holder.mProductJson = holder.mProduct.getUuid();
        Picasso.get()
                .load(holder.mProduct.getImage())
                .error(R.drawable.side_nav_bar)
                .into(holder.mImageProduct);
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
        Product mProduct;
        TextView mProductName;
        TextView mProductPrice;
        TextView mProductDescription;
        Button mButtonAdd;
        Button mButtonMore;
        Context mContext;
        String mProductJson;
        ImageView mImageProduct;
        //endregion
        //region Constructors
        ViewHolder(final View itemView) {
            super(itemView);
            this.mProductName = itemView.findViewById(R.id.ProductItemName);
            this.mProductPrice = itemView.findViewById(R.id.ProductItemPrice);
            this.mProductDescription = itemView.findViewById(R.id.ProductItemDescription);
            this.mButtonAdd = itemView.findViewById(R.id.ProductItemAdd);
            this.mButtonMore = itemView.findViewById(R.id.ProductItemMore);
            this.mImageProduct = itemView.findViewById(R.id.image_product_item);

            this.mButtonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: ADD ACTION TO DE ADD BUTTON
                }
            });

            this.mButtonMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    bundle.putString("productJson", gson.toJson(mProduct));
                    FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                    ProductDialogFragment productDialogFragment = new ProductDialogFragment();
                    productDialogFragment.setArguments(bundle);
                    productDialogFragment.show(manager, "productInformation");
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
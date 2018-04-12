package com.lovera.diego.restock.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.lovera.diego.restock.ProductDialogFragment;
import com.lovera.diego.restock.R;
import com.lovera.diego.restock.RestockApp;
import com.lovera.diego.restock.common.Serializer;
import com.lovera.diego.restock.models.OrderContent;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mProduct = mData.get(position);
        holder.mContext = this.mContext;
        holder.mProductName.setText(holder.mProduct.getName());
        holder.mProductPrice.setText(holder.mProduct.getPrice());
        holder.mRatingBar.setMax(5);
        holder.mRatingBar.setNumStars(5);
        holder.mTextRatingPlain.setText(holder.mProduct.getRating());
        holder.mRatingBar.setRating(Float.parseFloat(holder.mProduct.getRating()));
        holder.mProductDescription.setText(holder.mProduct.getDetail());
        holder.mProductJson = holder.mProduct.getUuid();
        Picasso.get()
                .load(holder.mProduct.getImage())
                .error(R.drawable.side_nav_bar)
                .into(holder.mImageProduct);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.quantity, android.R.layout.select_dialog_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        holder.mSpinner.setAdapter(adapter);
        holder.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                holder.mTotal = String.valueOf(Integer.valueOf(holder.mProduct.getPrice()) * Integer.valueOf(parent.getSelectedItem().toString()));
                holder.mProductPrice.setText(holder.mTotal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (position + 1 == getItemCount()) {
            // set bottom margin to 72dp.
            setBottomMargin(holder.itemView, (int) (85 * Resources.getSystem().getDisplayMetrics().density));
        } else {
            // reset bottom margin back to zero. (your value may be different)
            setBottomMargin(holder.itemView, 0);
        }
    }
    //endregion
    //region setBottonMargin
    private static void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }
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
        Spinner mSpinner;
        String mTotal;
        RatingBar mRatingBar;
        TextView mTextRatingNumber;
        TextView mTextRatingPlain;
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
            this.mSpinner = itemView.findViewById(R.id.spinner);
            this.mRatingBar = itemView.findViewById(R.id.ratingBar_product);
            this.mTextRatingNumber = itemView.findViewById(R.id.text_rating_number);
            this.mTextRatingPlain = itemView.findViewById(R.id.text_rating_plain);
            int numero = (int) (Math.random() * 100) + 1;
            String numberText = "(" + String.valueOf(numero) + ")";
            mTextRatingNumber.setText(numberText);

            this.mButtonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RestockApp.ACTUAL_ORDER_CONTENT.add(
                            new OrderContent(
                                    "",
                                    mProduct.getUuid(),
                                    mSpinner.getSelectedItem().toString(),
                                    mTotal
                            )
                    );
                    RestockApp.ACTUAL_PRODUCT_LIST.add(mProduct);
                    Snackbar.make(v, R.string.message_product_added, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            });

            this.mButtonMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("productJson", Serializer.Serialize(mProduct));
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
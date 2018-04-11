package com.lovera.diego.restock.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lovera.diego.restock.R;
import com.lovera.diego.restock.models.OrderContent;
import com.lovera.diego.restock.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailProductItemAdapter extends RecyclerView.Adapter<DetailProductItemAdapter.ViewHolder> {
    //region Fields
    private List<OrderContent> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    private Product mProduct;
    //endregion

    //region Constructors
    public DetailProductItemAdapter(Context context, List<OrderContent> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }
    //endregion

    //region onCreateViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_detail_history_product, parent, false);
        return new ViewHolder(view);
    }
    //endregion
    //region onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mOrderContent = mData.get(position);
        holder.mTextTotal.setText(holder.mOrderContent.getTotal());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Product");
        Query query = reference.child(holder.mOrderContent.getProduct());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mProduct = dataSnapshot.getValue(Product.class);
                    if (mProduct != null){
                        holder.mTextName.setText(mProduct.getName());
                        holder.mTextQuantity.setText(mProduct.getQuantity());
                        Picasso.get()
                                .load(mProduct.getImage())
                                .error(R.drawable.side_nav_bar)
                                .into(holder.mImageProduct);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //endregion
    //region getItemCount
    @Override
    public int getItemCount() {
        return mData.size();
    }
    //endregion
    //region getItem
    public OrderContent getItem(int id) {
        return mData.get(id);
    }
    //endregion
    //region setClickListener
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    //endregion

    //region Interface ItemClickListener
    public interface ItemClickListener {
        void onDetailProductItemClick(View view, int position);
    }
    //endregion
    //region Class ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //region Fields
        OrderContent mOrderContent;
        TextView mTextName;
        TextView mTextQuantity;
        TextView mTextTotal;
        ImageView mImageProduct;
        //endregion

        //region Constructor
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextName = itemView.findViewById(R.id.text_name_detail_item);
            mTextQuantity = itemView.findViewById(R.id.text_quantity_detail_item);
            mTextTotal = itemView.findViewById(R.id.text_total_detail_item);
            mImageProduct = itemView.findViewById(R.id.image_product_detail_item);
        }
        //endregion
        //region onClick
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onDetailProductItemClick(view, getAdapterPosition());
        }
        //endregion
    }
    //endregion
}

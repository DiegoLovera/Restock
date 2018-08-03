package com.lovera.diego.restock.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.lovera.diego.restock.R;
import com.lovera.diego.restock.RestockApp;
import com.lovera.diego.restock.models.OrderContent;
import com.lovera.diego.restock.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    //region Fields
    private List<OrderContent> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    //endregion

    //region Constructors
    public CartItemAdapter(Context context, List<OrderContent> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }
    //endregion

    //region onCreateViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_cart_item, parent, false);
        return new ViewHolder(view);
    }
    //endregion
    //region onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mActualOrderContent = mData.get(position);
        holder.mActualProduct = RestockApp.ACTUAL_PRODUCT_LIST.get(position);
        holder.mOrderTitle.setText(holder.mActualProduct.getName());
        holder.mOrderTotal.setText(holder.mActualOrderContent.getTotal());

        Picasso.get()
                .load(holder.mActualProduct.getImage())
                .error(R.drawable.side_nav_bar)
                .into(holder.mImageProduct);

        holder.mOrderQuantity.setSelection(Integer.valueOf(holder.mActualOrderContent.getQuantity()) - 1);
        holder.mOrderQuantity.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionSpinner, long id) {
                holder.mOrderTotal.setText(String.valueOf(Integer.valueOf(holder.mActualProduct.getPrice()) * Integer.valueOf(parent.getSelectedItem().toString())));
                holder.mActualOrderContent.setTotal(holder.mOrderTotal.getText().toString());
                holder.mActualOrderContent.setQuantity(holder.mOrderQuantity.getSelectedItem().toString());
                RestockApp.ACTUAL_ORDER_CONTENT.get(position).setQuantity(holder.mActualOrderContent.getQuantity());
                RestockApp.ACTUAL_ORDER_CONTENT.get(position).setTotal(holder.mActualOrderContent.getTotal());
                notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        void onCartItemClick(View view, int position);
    }
    //endregion
    //region Class ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //region Fields
        Product mActualProduct;
        OrderContent mActualOrderContent;
        TextView mOrderTitle;
        TextView mOrderTotal;
        Spinner mOrderQuantity;
        ImageButton mDeleteButton;
        ImageView mImageProduct;
        boolean mFirstTime = true;
        AdapterView.OnItemSelectedListener mItemSelectedListener;
        //endregion

        //region Constructor
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mOrderTitle = itemView.findViewById(R.id.title_product_title_cart);
            mOrderTotal = itemView.findViewById(R.id.text_total_to_pay_cart_item);
            mOrderQuantity = itemView.findViewById(R.id.spinner_item_cart_list);
            mDeleteButton = itemView.findViewById(R.id.button_delete_order_cart_list);
            mImageProduct = itemView.findViewById(R.id.image_product_cart_list);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RestockApp.ACTUAL_PRODUCT_LIST.remove(getAdapterPosition());
                    RestockApp.ACTUAL_ORDER_CONTENT.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    Snackbar.make(v, "Product deleted", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            });

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                    R.array.quantity, android.R.layout.select_dialog_item);
            adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
            mOrderQuantity.setAdapter(adapter);
        }
        //endregion
        //region onClick
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onCartItemClick(view, getAdapterPosition());
        }
        //endregion
    }
    //endregion
}

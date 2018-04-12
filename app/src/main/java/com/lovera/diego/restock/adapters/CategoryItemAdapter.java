package com.lovera.diego.restock.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovera.diego.restock.R;
import com.lovera.diego.restock.models.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ViewHolder> {
    //region Fields
    private ArrayList<Category> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    //endregion

    //region Constructors
    public CategoryItemAdapter(Context context, ArrayList<Category> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    //endregion

    //region onCreateViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_category_item, parent, false);
        return new ViewHolder(view);
    }
    //endregion
    //region onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category actualCategory = mData.get(position);
        holder.mCategoryTittle.setText(actualCategory.getName());
        Picasso.get()
                .load(actualCategory.getImageUrl())
                .error(R.drawable.craft_preview)
                .into(holder.mCategoryImage);
    }
    //endregion
    //region getItemCount
    @Override
    public int getItemCount() {
        return mData.size();
    }
    //endregion
    //region getItem
    public Category getItem(int id) {
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
        void onCategoryItemClick(View view, int position);
    }
    //endregion
    //region Class ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //region Fields
        ImageView mCategoryImage;
        TextView mCategoryTittle;
        //endregion

        //region Constructor
        ViewHolder(View itemView) {
            super(itemView);
            mCategoryTittle = itemView.findViewById(R.id.MainItemTittle);
            mCategoryImage = itemView.findViewById(R.id.image_category_adapter);
            itemView.setOnClickListener(this);
        }
        //endregion
        //region onClick
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onCategoryItemClick(view, getAdapterPosition());
        }
        //endregion
    }
    //endregion
}
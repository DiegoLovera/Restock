package com.lovera.diego.restock.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lovera.diego.restock.R;
import com.lovera.diego.restock.models.Type;

import java.util.ArrayList;

public class TypeItemAdapter extends RecyclerView.Adapter<TypeItemAdapter.ViewHolder> {

    private ArrayList<Type> mData;
    private LayoutInflater mInflater;
    private TypeItemAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public TypeItemAdapter(Context context, ArrayList<Type> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public TypeItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_type_item, parent, false);
        return new TypeItemAdapter.ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(TypeItemAdapter.ViewHolder holder, int position) {
        Type actualType = mData.get(position);
        holder.mTypeTittle.setText(actualType.getName());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTypeTittle;

        ViewHolder(View itemView) {
            super(itemView);
            mTypeTittle = itemView.findViewById(R.id.TypeItemTittle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onTypeItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Type getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(TypeItemAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onTypeItemClick(View view, int position);
    }
}

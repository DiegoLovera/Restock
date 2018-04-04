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
    //region Fields
    private ArrayList<Type> mData;
    private LayoutInflater mInflater;
    private TypeItemAdapter.ItemClickListener mClickListener;
    //endregion

    //region Constructors
    public TypeItemAdapter(Context context, ArrayList<Type> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    //endregion

    //region onCreateViewHolder
    @NonNull
    @Override
    public TypeItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_type_item, parent, false);
        return new TypeItemAdapter.ViewHolder(view);
    }
    //endregion
    //region onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull TypeItemAdapter.ViewHolder holder, int position) {
        Type actualType = mData.get(position);
        holder.mTypeTittle.setText(actualType.getName());
    }
    //endregion
    //region getItemCount
    @Override
    public int getItemCount() {
        return mData.size();
    }
    //endregion
    //region getItem
    public Type getItem(int id) {
        return mData.get(id);
    }
    //endregion
    //region setClickListener
    public void setClickListener(TypeItemAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    //endregion

    //region Interface ItemClickListener
    public interface ItemClickListener {
        void onTypeItemClick(View view, int position);
    }
    //endregion
    //region Class ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //region Fields
        TextView mTypeTittle;
        //endregion
        //region Constructors
        ViewHolder(View itemView) {
            super(itemView);
            mTypeTittle = itemView.findViewById(R.id.TypeItemTittle);
            itemView.setOnClickListener(this);
        }
        //endregion
        //region onClick
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onTypeItemClick(view, getAdapterPosition());
        }
        //endregion
    }
    //endregion
}

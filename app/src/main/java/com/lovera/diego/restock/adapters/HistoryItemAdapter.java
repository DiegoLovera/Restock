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
import android.widget.TextView;

import com.lovera.diego.restock.HistoryDetailDialogFragment;
import com.lovera.diego.restock.R;
import com.lovera.diego.restock.common.Serializer;
import com.lovera.diego.restock.models.Order;

import java.util.List;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ViewHolder> {
    //region Fields
    private List<Order> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    //endregion

    //region Constructors
    public HistoryItemAdapter(Context context, List<Order> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }
    //endregion

    //region onCreateViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_history_item, parent, false);
        return new ViewHolder(view);
    }
    //endregion
    //region onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mOrder = mData.get(position);
        holder.mTextDate.setText(holder.mOrder.getDate());
        holder.mTextPay.setText(holder.mOrder.getTotal());
        switch (holder.mOrder.getStatus()) {
            case "1":
                holder.mTextStatus.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.mTextStatus.setText("Pending");
                break;
            case "2":
                holder.mTextStatus.setTextColor(mContext.getResources().getColor(R.color.orange));
                holder.mTextStatus.setText("Incoming");
                break;
            case "3":
                holder.mTextStatus.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.mTextStatus.setText("Delivered");
                break;
        }
        holder.mButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("orderJson", Serializer.Serialize(holder.mOrder));
                FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                HistoryDetailDialogFragment historyDetailDialogFragment = new HistoryDetailDialogFragment();
                historyDetailDialogFragment.setArguments(bundle);
                historyDetailDialogFragment.show(manager, "OrderDetail");
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
    public Order getItem(int id) {
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
        void onHistoryItemClick(View view, int position);
    }
    //endregion
    //region Class ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //region Fields
        TextView mTextDate;
        TextView mTextPay;
        TextView mTextStatus;
        Button mButtonDetail;
        Order mOrder;
        //endregion

        //region Constructor
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTextDate = itemView.findViewById(R.id.text_date_history);
            mTextPay = itemView.findViewById(R.id.text_pay_history);
            mTextStatus = itemView.findViewById(R.id.text_status_history);
            mButtonDetail = itemView.findViewById(R.id.button_detail_history);
        }
        //endregion
        //region onClick
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onHistoryItemClick(view, getAdapterPosition());
        }
        //endregion
    }
    //endregion
}

